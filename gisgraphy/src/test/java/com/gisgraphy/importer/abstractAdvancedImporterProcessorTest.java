/*******************************************************************************
 * Gisgraphy Project 
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *  
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 *  
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 *  
 *   Copyright 2008  Gisgraphy project 
 * 
 *   David Masclet <davidmasclet@gisgraphy.com>
 ******************************************************************************/
package com.gisgraphy.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jstester.util.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.valueobject.NameValueDTO;

public class abstractAdvancedImporterProcessorTest {

    public class defaultAdvancedProcessor extends AbstractAdvancedImporterProcessor {

	public List<NameValueDTO<Integer>> rollback() {
	    return null;
	}

	@Override
	protected void setCommitFlushMode() {
	}

	@Override
	protected File[] getFiles() {
	    return new File[0];
	}

	@Override
	public Runnable createTask(File file) {
	    return null;
	}

	@Override
	public int getNumberOfProcessors() {
	    return 1;
	}


	@Override
	protected List<File> getSplitedFiles() {
		return new ArrayList<File>();
	}
    };

    @Test
    public void setUpShouldSplitFile() throws FileNotFoundException {
	@SuppressWarnings("serial")
	final File file = new File("path") {
	    @Override
	    public String getAbsolutePath() {
		return "path";
	    }
	};
	int numberOfLines = 1001;
	final int numberOfProcessor = 5;
	AbstractAdvancedImporterProcessor processor = new defaultAdvancedProcessor() {

	    public List<NameValueDTO<Integer>> rollback() {
		return null;
	    }

	    @Override
	    protected void setCommitFlushMode() {
	    }

	    @Override
	    protected File[] getFiles() {
		File[] files = new File[] { file };
		return files;
	    }

	    @Override
	    public Runnable createTask(File file) {
		return null;
	    }

	    @Override
	    public int getNumberOfProcessors() {
		return numberOfProcessor;
	    }
	};
	FileSpliter fileSpliter = EasyMock.createMock(FileSpliter.class);
	EasyMock.expect(fileSpliter.countLines("path")).andReturn(numberOfLines);
	int splitLength = processor.getSplitLength(numberOfLines);
	List<File> splitedFiles = new ArrayList<File>();
	splitedFiles.add(new File("splited"));
	EasyMock.expect(fileSpliter.SplitByLength(file, splitLength)).andReturn(splitedFiles);
	EasyMock.replay(fileSpliter);

	IIdGenerator idgenerator = EasyMock.createMock(IIdGenerator.class);
	idgenerator.sync();
	EasyMock.replay(idgenerator);
	processor.fileSpliter = fileSpliter;
	processor.idGenerator = idgenerator;

	processor.setup();

	EasyMock.verify(fileSpliter);
	EasyMock.verify(idgenerator);
	Assert.assertEquals(splitedFiles, processor.splitedFiles);

    }

    @Test
    public void getNumberOfTaskexecutors() {
	AbstractAdvancedImporterProcessor processor = new defaultAdvancedProcessor() {

	    @Override
	    public int getNumberOfProcessors() {
		return 1;
	    }
	};
	Assert.assertEquals(1, processor.getNumberOfTaskExecutors());
	processor = new defaultAdvancedProcessor() {

	    @Override
	    public int getNumberOfProcessors() {
		return 2;
	    }
	};
	Assert.assertEquals(2, processor.getNumberOfTaskExecutors());
	processor = new defaultAdvancedProcessor() {

	    @Override
	    public int getNumberOfProcessors() {
		return 3;
	    }
	};
	Assert.assertEquals(4, processor.getNumberOfTaskExecutors());
	processor = new defaultAdvancedProcessor() {

	    @Override
	    public int getNumberOfProcessors() {
		return 8;
	    }
	};
	Assert.assertEquals(12, processor.getNumberOfTaskExecutors());
    }

}
