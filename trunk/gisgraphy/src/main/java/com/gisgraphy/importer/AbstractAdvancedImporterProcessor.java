/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.backportconcurrent.ThreadPoolTaskExecutor;

import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.valueobject.ImporterStatus;

/**
 * Import the street from an (pre-processed) openStreet map data file in with
 * all the processor capacity.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public abstract class AbstractAdvancedImporterProcessor extends AbstractSimpleImporterProcessor {

	@Autowired
	protected IIdGenerator idGenerator;

	protected AtomicInteger totalReadLineMultithreaded = new AtomicInteger();

	protected ThreadPoolTaskExecutor threadPoolTaskExecutor;

	protected FileSpliter fileSpliter = new FileSpliter();

	protected List<File> splitedFiles = new ArrayList<File>();

	/**
	 * The logger
	 */
	protected static final Logger logger = LoggerFactory.getLogger(AbstractAdvancedImporterProcessor.class);

	protected ImporterException exception = null;
	private boolean threadPoolTaskExecutorInitialize = false;

	@Override
	protected void setup() {
		super.setup();
		idGenerator.sync();
		splitFiles();
		// force calculation of numberOfLineToProcess because the original files
		// have been deleted : if it has not been calculated yet, the value will
		// be wrong
		logger.info("counting lines");
		this.numberOfLinesToProcess = countLines(getSplitedFilesAsArray());
		logger.info("end of counting lines");
	}

	protected abstract List<File> getSplitedFiles();

	protected void splitFiles() {
		List<File> splitedFilesAlreadyPresent = getSplitedFiles();
		if (splitedFilesAlreadyPresent.size() == 0) {
			logger.info("we need to split files");
			for (File file : getFiles()) {
				int nblines = fileSpliter.countLines(file.getAbsolutePath());
				int splitlength = getSplitLength(nblines);
				logger.info("Will split " + file.getAbsolutePath() + " in many file of " + splitlength + " lines");
				try {
					splitedFiles.addAll(fileSpliter.SplitByLength(file, splitlength));
				} catch (FileNotFoundException e) {
					throw new RuntimeException("we can not split file " + file.getAbsolutePath() + " because it does not exists ");
				}
			}
		} else {
			logger.info("we don't need to split files");
			splitedFiles = splitedFilesAlreadyPresent;
		}
	}

	protected int getSplitLength(int nblines) {
		int splitlength = (nblines / (getNumberOfProcessors() * 2)) + getNumberOfProcessors();
		return splitlength;
	}

	@Override
	public void process() {
		if (shouldBeSkipped()) {
			logger.info(this.getClass().getSimpleName() + " should be skiped");
			this.status = ImporterStatus.SKIPPED;
			return;
		}
		this.status = ImporterStatus.PROCESSING;
		setup();
		threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(getNumberOfTaskExecutors());
		threadPoolTaskExecutor.setMaxPoolSize(getNumberOfTaskExecutors());
		threadPoolTaskExecutor.setQueueCapacity(10000);
		threadPoolTaskExecutor.initialize();
		threadPoolTaskExecutorInitialize = true;
		for (File file : splitedFiles) {
			Runnable task = createTask(file);
			logger.info("creating task " + task);
			try {
				Thread.sleep(2000);//for progressive starting
			} catch (InterruptedException e) {
			}
			threadPoolTaskExecutor.execute(task);
		}
		try {
			logger.info("set keepalive");
			threadPoolTaskExecutor.setKeepAliveSeconds(Integer.MAX_VALUE);
		} catch (Exception e) {
			this.status = ImporterStatus.ERROR;
			this.statusMessage = "An error occurred when processing " + this.getClass().getSimpleName() + " : " + e.getMessage();
			logger.error(statusMessage, e);
			throw new ImporterException(statusMessage, e.getCause());
		}
		try {
			while (threadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount() != 0) {
				if (exception != null) {
					throw exception;
				}
				logger.info("wait for " + this.getClass().getSimpleName() + " to be finished ("+threadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount()+" active threads)");
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			logger.warn(this.getClass().getSimpleName() + "has been interrupted");
		} finally {
			try {
				tearDown();
				this.status = this.status == ImporterStatus.PROCESSING ? ImporterStatus.PROCESSED : this.status;
				if (this.status != ImporterStatus.ERROR) {
					this.statusMessage = "";
				}
				clean();
			} catch (Exception e) {
				this.status = ImporterStatus.ERROR;
				String teardownErrorMessage = "An error occured on teardown (the import is done but maybe not optimzed) :" + e.getMessage();
				this.statusMessage = this.statusMessage != "" ? this.statusMessage + " and " + teardownErrorMessage : teardownErrorMessage;
				logger.error(statusMessage);
			}
		}
		logger.info(this.getClass().getSimpleName() + " is finished");

	}

	private void clean() {
		try {
			threadPoolTaskExecutor.destroy();
			threadPoolTaskExecutor.getThreadPoolExecutor().shutdownNow();
			threadPoolTaskExecutor = null;
		} catch (Exception e) {
			logger.error("an error has occured when cleaning thread executor"+e.getMessage(),e);
		}
	}

	public abstract Runnable createTask(File file);

	/**
	 * @param increment
	 * @return the incremented value
	 */
	@Override
	public int incrementReadedFileLine(int increment) {
		return totalReadLineMultithreaded.addAndGet(increment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#processData
	 * (java.lang.String)
	 */
	@Override
	protected void processData(String line) throws ImporterException {
		throw new RuntimeException("The processData method should not be called in multithread mode");
	}

	@Override
	public String getCurrentFileName() {
		if (threadPoolTaskExecutor != null && threadPoolTaskExecutorInitialize) {
			return threadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount() + " " + internationalisationService.getString("global.files");
		} else {
			return "?";
		}
	}

	@Override
	public long getReadFileLine() {
		return getTotalReadLine();
	}

	@Override
	public long getTotalReadLine() {
		// we need to do this because we flush every transaction commit and at
		// the end of the importer lines processed < number of lines commited
		return this.totalReadLineMultithreaded.get() > getNumberOfLinesToProcess() ? getNumberOfLinesToProcess() : this.totalReadLineMultithreaded.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#
	 * shouldIgnoreComments()
	 */
	@Override
	protected boolean shouldIgnoreComments() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#
	 * shouldIgnoreFirstLine()
	 */
	@Override
	protected boolean shouldIgnoreFirstLine() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#
	 * getNumberOfColumns()
	 */
	@Override
	protected int getNumberOfColumns() {
		throw new RuntimeException("The getNumberOfColumns method should not be called in multithread mode");
	}

	public int getNumberOfProcessors() {
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();
		logger.info(numberOfProcessors + " processor(s) has been detected");
		return numberOfProcessors;
	}

	public int getNumberOfTaskExecutors() {
		int numberOfExecutors = getNumberOfProcessors();
		if (numberOfExecutors > 2) {
			numberOfExecutors = numberOfExecutors + (numberOfExecutors / 2);
		}
		logger.info(numberOfExecutors + " exectutors(s) will be used");
		return numberOfExecutors;
	}

	public void setIdGenerator(IIdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear
	 * ()
	 */
	@Override
	protected void flushAndClear() {
		throw new RuntimeException("The flushAndClear method should not be called in multithread mode except if we share the flushAndClearBetwen tasks");
	}

	public long getNumberOfLinesToProcess() {
		if (this.numberOfLinesToProcess == 0 && this.status == ImporterStatus.PROCESSING) {
			// it may not have been calculated yet
			this.numberOfLinesToProcess = countLines(getSplitedFilesAsArray());
		}
		return this.numberOfLinesToProcess;
	}

	private File[] getSplitedFilesAsArray() {
		return splitedFiles.toArray(new File[splitedFiles.size()]);
	}

}
