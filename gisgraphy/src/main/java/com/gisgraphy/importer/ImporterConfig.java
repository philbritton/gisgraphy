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
/**
 *
 */
package com.gisgraphy.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.helper.CommentedProperties;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *         Represents a configuration for importers For more informations for
 *         this options see the User Guide
 */
public class ImporterConfig {
	
	 public static final String PROPERTIES_CONFIG_FILE_CLASSPATH = "/env.properties";

	public static final String OPENSTREETMAP_FILL_ISIN_FIELD_NAME = "importerConfig.openstreetmap.fill.isin.field";

	/**
     * A list of options is separated by this. e.g : a list of regexp options
     */
    public static final String OPTION_SEPARATOR = ";";

    /**
     * The relative path of the directory that contains importer metadata
     */
    public static final String IMPORTER_METADATA_RELATIVE_PATH = "IMPORTER-METADATA-DO_NOT_REMOVE";

    /**
     * the name of the file that gives the information if the import is done or
     * not
     */
    public static final String ALREADY_DONE_FILE_NAME = "importNotAlreadyDone";

    /**
     * The default feature code if no one is specified
     */
    public final static String DEFAULT_FEATURE_CODE = "UNK";
    /**
     * The default feature class if no one is specified
     */
    public final static String DEFAULT_FEATURE_CLASS = "UNK";

    /**
     * The default regexp if no one is specified in the env.properties file
     */
    public final static String BASE_ACCEPT_REGEX = ImporterConfig.DEFAULT_FEATURE_CLASS + "[.].+;.[.]" + ImporterConfig.DEFAULT_FEATURE_CODE + ";A[.]ADM.?;A[.]PCL.?;";

    /**
     * The regexp to use to import all the city
     */
    public final static String DEFAULT_ACCEPT_REGEX_CITY = BASE_ACCEPT_REGEX + "P[.]PPL[A-Z&&[^QW]];P[.]PPL$;P[.]STLMT$";

    /**
     * The regexp to use to import all the feature class / code
     */
    public final static String ACCEPT_ALL_REGEX_OPTION = ".*;";

    /**
     * Default value for {@link #maxInsertsBeforeFlush}
     */
    public final static int DEFAULT_MAX_INSERT_BEFORE_FLUSH = 1000;

    /**
     * How many lines do we have to process before flushing
     * 
     * @see #DEFAULT_MAX_INSERT_BEFORE_FLUSH
     */
    private int maxInsertsBeforeFlush = DEFAULT_MAX_INSERT_BEFORE_FLUSH;

    public final static String OPENSTREETMAP_DEFAULT_FILES_TO_DOWNLOAD = "allcountries.tar.bz2";
    
    public final static String OPENSTREETMAP_HOUSENUMBER_DEFAULT_FILES_TO_DOWNLOAD = "allcountries.tar.bz2";

    public final static String GEONAMES_ALTERNATENAME_ZIP_FILE="alternateNames.zip";
    
    public final static String GEONAMES_COMPRESSED_FILE_EXTENSION=".zip";
    
    public final static String OPENSTREETAMP_COMPRESSED_FILE_EXTENSION=".tar.bz2";
    
    public final static String GEONAMES_DEFAULT_FILES_TO_DOWNLOAD = "allCountries.zip"+OPTION_SEPARATOR+GEONAMES_ALTERNATENAME_ZIP_FILE;

    /**
     * Default option if the Adm1 file has already been processed
     * 
     * @see AdmExtracterStrategyOptions
     */
    public AdmExtracterStrategyOptions DEFAULT_ADM1_EXTRACTER_STRATEGY_OPTION = AdmExtracterStrategyOptions.reprocess;
    /**
     * Default option if the adm2 file has already been processed
     * 
     * @see AdmExtracterStrategyOptions
     */
    public AdmExtracterStrategyOptions DEFAULT_ADM2_EXTRACTER_STRATEGY_OPTION = AdmExtracterStrategyOptions.reprocess;

    /**
     * Default option if the Adm3 file has already been processed
     * 
     * @see AdmExtracterStrategyOptions
     */
    public AdmExtracterStrategyOptions DEFAULT_ADM3_EXTRACTER_STRATEGY_OPTION = AdmExtracterStrategyOptions.reprocess;
    /**
     * Default option if the adm4 file has already been processed
     * 
     * @see AdmExtracterStrategyOptions
     */
    public AdmExtracterStrategyOptions DEFAULT_ADM4_EXTRACTER_STRATEGY_OPTION = AdmExtracterStrategyOptions.reprocess;

    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(ImporterConfig.class);

   

    private boolean wrongNumberOfFieldsThrows = false;

    private boolean missingRequiredFieldThrows = false;

    private boolean importGisFeatureEmbededAlternateNames = false;

    private String geonamesDir;

    private String openStreetMapDir;
    
    private String openStreetMapHouseNumberDir;

    private String geonamesZipCodeDir;

    private String openstreetMapDownloadURL;
    
    private String openstreetMaphouseNumberDownloadURL;

    private String geonamesDownloadURL;

    private String geonamesZipCodeDownloadURL;

    private boolean retrieveFiles = false;

    private String geonamesFilesToDownload = "";

    private String openStreetMapFilesToDownload = "";
    
    private String openStreetMapHouseNumberFilesToDownload = "";

    private boolean geonamesImporterEnabled = true;

    private boolean openstreetmapImporterEnabled = true;
    
    private boolean openstreetmapHouseNumberImporterEnabled = true;

    private boolean openStreetMapFillIsIn = true;

    /**
     * @return true if the importer should process the import of Geonames data
     */
    public boolean isGeonamesImporterEnabled() {
	return geonamesImporterEnabled;
    }

    /**
     * @param geonamesImporterEnabled
     *            enable or disable Geonames importer
     * @see ImporterConfig#isGeonamesImporterEnabled()
     */
    @Required
    public void setGeonamesImporterEnabled(boolean geonamesImporterEnabled) {
	this.geonamesImporterEnabled = geonamesImporterEnabled;
    }

    /**
     * @return true if the importer should process the import of Openstreetmap
     *         data
     * @see ImporterConfig#isGeonamesImporterEnabled()
     */
    public boolean isOpenstreetmapImporterEnabled() {
	return openstreetmapImporterEnabled;
    }
    
    /**
     * @return true if the importer should process the import of Openstreetmap house number
     *         data
     * @see ImporterConfig#isGeonamesImporterEnabled()
     */
    public boolean isOpenstreetmapHouseNumberImporterEnabled() {
	return  openstreetmapHouseNumberImporterEnabled;
    }
    
   

    /**
     * @param openstreetmapImporterEnabled
     *            enable or disable Openstreetmap importer
     * @see ImporterConfig#isOpenstreetmapImporterEnabled()
     */
    @Required
    public void setOpenstreetmapImporterEnabled(boolean openstreetmapImporterEnabled) {
	this.openstreetmapImporterEnabled = openstreetmapImporterEnabled;
    }
    
    /**
     * @param openstreetmapHouseNumberImporterEnabled
     *            enable or disable Openstreetmap housenumber importer
     * @see ImporterConfig#isOpenstreetmapImporterEnabled()
     */
    @Required
    public void setOpenstreetmapHouseNumberImporterEnabled(boolean openstreetmapHouseNumberImporterEnabled) {
	this.openstreetmapHouseNumberImporterEnabled = openstreetmapHouseNumberImporterEnabled;
    }

    /**
     * @return A list of string with the files to be download, processed from
     *         {@link #geonamesFilesToDownload}
     */
    public List<String> getGeonamesDownloadFilesListFromOption() {
	return splitSemiColmunStringToList(geonamesFilesToDownload);
    }

    private List<String> splitSemiColmunStringToList(String stringToSplit) {
	List<String> list = new ArrayList<String>();
	if (stringToSplit != null && stringToSplit.length() != 0) {
	    String[] splited = stringToSplit.split(OPTION_SEPARATOR);
	    for (int i = 0; i < splited.length; i++) {
		list.add(splited[i]);
	    }
	}
	return list;
    }

    /**
     * @return A list of string with the files to be download, processed from
     *         {@link #openStreetMapFilesToDownload}
     */
    public List<String> getOpenStreetMapDownloadFilesListFromOption() {
	return splitSemiColmunStringToList(openStreetMapFilesToDownload);
    }
    
    /**
     * @return A list of string with the files to be download, processed from
     *         {@link #openStreetMapHouseNumberFilesToDownload}
     */
    public List<String> getOpenStreetMapHouseNumberDownloadFilesListFromOption() {
	return splitSemiColmunStringToList(openStreetMapHouseNumberFilesToDownload);
    }

    private String adm1FileName;

    private String adm2FileName;

    private String adm3FileName;

    private String adm4FileName;

    private String languageFileName;

    private String countriesFileName;

    private String alternateNamesFileName;

    private String acceptRegExString = "*";

    private boolean tryToDetectAdmIfNotFound = true;

    private boolean syncAdmCodesWithLinkedAdmOnes = true;

    private AdmExtracterStrategyOptions adm1ExtracterStrategyIfAlreadyExists;

    private AdmExtracterStrategyOptions adm2ExtracterStrategyIfAlreadyExists;

    private AdmExtracterStrategyOptions adm3ExtracterStrategyIfAlreadyExists;

    private AdmExtracterStrategyOptions adm4ExtracterStrategyIfAlreadyExists;

    private String alternateNameFeaturesFileName;

    private String alternateNameAdm1FileName;

    private String alternateNameAdm2FileName;

    private String alternateNameCountryFileName;

    /**
     * What should we do if the Adm file for the specified level has already
     * been processed It is a wrapper method around
     * {@link #DEFAULT_ADM3_EXTRACTER_STRATEGY_OPTION} and
     * {@link #DEFAULT_ADM4_EXTRACTER_STRATEGY_OPTION}
     */
    public AdmExtracterStrategyOptions getAdmExtracterStrategyOptionsForAdm(int admLevel) {
	if (admLevel == 1) {
	    return adm1ExtracterStrategyIfAlreadyExists;
	} else if (admLevel == 2) {
	    return adm2ExtracterStrategyIfAlreadyExists;
	} else if (admLevel == 3) {
	    return adm3ExtracterStrategyIfAlreadyExists;
	} else if (admLevel == 4) {
	    return adm4ExtracterStrategyIfAlreadyExists;
	} else {
	    throw new RuntimeException(" can not get AdmExtracterStrategyOptions For Adm with level " + admLevel);
	}
    }

    /**
     * @return The option
     * @see #setAcceptRegExString(String)
     */
    public String getAcceptRegExString() {
	return this.acceptRegExString;
    }

    /**
     * @return The option
     * @see #setSyncAdmCodesWithLinkedAdmOnes(boolean)
     */
    public boolean isSyncAdmCodesWithLinkedAdmOnes() {
	return this.syncAdmCodesWithLinkedAdmOnes;
    }

    /**
     * @return The option
     * @see #setTryToDetectAdmIfNotFound(boolean)
     */
    public boolean isTryToDetectAdmIfNotFound() {
	return this.tryToDetectAdmIfNotFound;
    }

    /**
     * List of regular expressions for feature class and feature code to be
     * import.<br>
     * <br>
     * <u>Important</u> : The regular expressions must match
     * featureClass.featureCode and are separated by {@link #OPTION_SEPARATOR}.<br>
     * <br>
     * The gisFeature which match "A.ADM." and "A.PCL." regex are automaticaly
     * imported (Administrative division and country).<br>
     * Examples :
     * <ul>
     * <li>.* : import all gisfeatures, no matter their feature class and
     * feature code</li>
     * <li> {@link #DEFAULT_ACCEPT_REGEX_CITY} : import Israeli settlements and
     * all the cities except destroyed and abandoned city</li>
     * <li>V.FRST. : import all the forests</li>
     * </ul>
     * 
     * @param acceptRegExString
     *            the option
     */
    @Required
    public void setAcceptRegExString(String acceptRegExString) {
	if (acceptRegExString == null || acceptRegExString.trim().equals("") || acceptRegExString.equals(ACCEPT_ALL_REGEX_OPTION)) {
	    logger.warn("the option acceptRegExString is not set and will be set to his default value : " + ACCEPT_ALL_REGEX_OPTION);
	    this.acceptRegExString = ACCEPT_ALL_REGEX_OPTION;
	    return;
	}
	this.acceptRegExString = BASE_ACCEPT_REGEX + acceptRegExString;
	logger.info("acceptRegExString=" + this.acceptRegExString);

    }

    /**
     * The linked Adm may not be the same as the one which would be found with
     * the ADMcodes from the csv file if TryToDetectAdmIfNotFound is set to
     * true. in this case error corecting is done. tis option determine if the
     * ADMXcode must be equals to the linked ADM or if they must be equals to
     * the value in the CSVFile note that the admXnames are always sync with the
     * Linked Adm if true : the AdmXcodes of the imported GisFeature will be the
     * gisFeature.getAdm.getAdmXcode.<br>
     * <br>
     * if false : the AdmXcode for a GisFeature will be the values of the CSV
     * dump file. That means : If the option tryToDetectAdmIfNotFound is set to
     * true : the Adm will be suggest if the AdmXcodes values of the CSV dump
     * file doesn't correspond to an already known Adm. In that case the
     * suggested Adm will have AdmXcodes different from the CSV dump file ones.
     * This option allow you to set The AdmXcodes for the gisFeature with the
     * detected Adm value instead of the CSV file ones.<br/>
     * In other words : AdmXcodes of the linked Adm and AdmXcodes of the
     * gisFeature will always be the same if this option is true. it is
     * recommended to let it to true
     * 
     * @param setAdmCodesWithLinkedAdmObject
     *            The option to set
     */
    @Required
    public void setSyncAdmCodesWithLinkedAdmOnes(boolean setAdmCodesWithLinkedAdmObject) {
	this.syncAdmCodesWithLinkedAdmOnes = setAdmCodesWithLinkedAdmObject;
	logger.info("setAdmCodesWithLinkedAdmObject=" + setAdmCodesWithLinkedAdmObject);
    }

    /**
     * If this option is set to true : The importer will try to detect Adm for
     * features if the AdmXcodes values does not correspond to a known Adm. it
     * is a process of error correction if set to false error correction is
     * disabled
     * 
     * @param tryToDetectAdmIfNotFound
     *            The option
     */
    @Required
    public void setTryToDetectAdmIfNotFound(boolean tryToDetectAdmIfNotFound) {
	this.tryToDetectAdmIfNotFound = tryToDetectAdmIfNotFound;
	logger.info("tryToDetectAdmIfNotFound=" + tryToDetectAdmIfNotFound);
    }

    /**
     * @return The option
     * @see #setAdm1ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions)
     */
    public AdmExtracterStrategyOptions getAdm1ExtracterStrategyIfAlreadyExists() {
	return this.adm1ExtracterStrategyIfAlreadyExists;
    }

    /**
     * What should we do if the Adm1 file has already been processed
     * 
     * @see #DEFAULT_ADM1_EXTRACTER_STRATEGY_OPTION
     * @param adm1ExtracterStrategy
     *            The option
     */
    @Required
    public void setAdm1ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions adm1ExtracterStrategy) {
	this.adm1ExtracterStrategyIfAlreadyExists = adm1ExtracterStrategy;
    }

    /**
     * @return The option
     * @see #setAdm2ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions)
     */
    public AdmExtracterStrategyOptions getAdm2ExtracterStrategyIfAlreadyExists() {
	return this.adm2ExtracterStrategyIfAlreadyExists;
    }

    /**
     * What should we do if the Adm2 file has already been processed
     * 
     * @see #DEFAULT_ADM2_EXTRACTER_STRATEGY_OPTION
     * @param adm2ExtracterStrategy
     *            The option
     */
    @Required
    public void setAdm2ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions adm2ExtracterStrategy) {
	this.adm2ExtracterStrategyIfAlreadyExists = adm2ExtracterStrategy;
    }

    /**
     * @return The option
     * @see #setAdm3ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions)
     */
    public AdmExtracterStrategyOptions getAdm3ExtracterStrategyIfAlreadyExists() {
	return this.adm3ExtracterStrategyIfAlreadyExists;
    }

    /**
     * What should we do if the Adm3 file has already been processed
     * 
     * @see #DEFAULT_ADM3_EXTRACTER_STRATEGY_OPTION
     * @param adm3ExtracterStrategy
     *            The option
     */
    @Required
    public void setAdm3ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions adm3ExtracterStrategy) {
	this.adm3ExtracterStrategyIfAlreadyExists = adm3ExtracterStrategy;
    }

    /**
     * @return the option
     * @see ImporterConfig#setAdm4ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions)
     */
    public AdmExtracterStrategyOptions getAdm4ExtracterStrategyIfAlreadyExists() {
	return this.adm4ExtracterStrategyIfAlreadyExists;
    }

    /**
     * What should we do if the Adm4 file has already been processed
     * 
     * @see #DEFAULT_ADM3_EXTRACTER_STRATEGY_OPTION
     * @param adm4ExtracterStrategy
     *            The option
     */
    @Required
    public void setAdm4ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions adm4ExtracterStrategy) {
	adm4ExtracterStrategyIfAlreadyExists = adm4ExtracterStrategy;
    }

    /**
     * @return The option
     * @see #setMissingRequiredFieldThrows(boolean)
     * @see MissingRequiredFieldException
     */
    public boolean isMissingRequiredFieldThrows() {
	return this.missingRequiredFieldThrows;
    }

    /**
     * Set to true this options force the import process to stop if a required
     * field is missing.<br>
     * Set to false it ignore the error and try to continue (recommended)
     * 
     * @param missingRequiredFieldThrows
     *            The option
     * @see MissingRequiredFieldException
     */
    @Required
    public void setMissingRequiredFieldThrows(boolean missingRequiredFieldThrows) {
	this.missingRequiredFieldThrows = missingRequiredFieldThrows;
    }

    /**
     * @return The option
     * @see #setWrongNumberOfFieldsThrows(boolean)
     */
    public boolean isWrongNumberOfFieldsThrows() {
	return this.wrongNumberOfFieldsThrows;
    }

    /**
     * Set to true this option force the import process to stop if an error is
     * throw.<br>
     * Set to false it ignore the error and try to continue (recommended)
     * 
     * @param wrongNumberOfFieldsThrows
     *            The option
     */
    @Required
    public void setWrongNumberOfFieldsThrows(boolean wrongNumberOfFieldsThrows) {
	this.wrongNumberOfFieldsThrows = wrongNumberOfFieldsThrows;
    }

    /**
     * @return The option
     * @see #setImportGisFeatureEmbededAlternateNames(boolean)
     */
    public boolean isImportGisFeatureEmbededAlternateNames() {
	return importGisFeatureEmbededAlternateNames;
    }

    /**
     * Set to true the alternate names of the country dump are imported. Set to
     * false it will import the alternate names from the alternatenames dump
     * file
     * 
     * @param importGisFeatureEmbededAlternateNames
     *            The option
     */
    @Required
    public void setImportGisFeatureEmbededAlternateNames(boolean importGisFeatureEmbededAlternateNames) {
	this.importGisFeatureEmbededAlternateNames = importGisFeatureEmbededAlternateNames;
    }

    /**
     * @return The option
     * @see #setOpenStreetMapDir(String)
     */
    public String getOpenStreetMapDir() {
	return this.openStreetMapDir;
    }
    
    /**
     * @return The option
     * @see #setOpenStreetMapHouseNumberDir(String)
     */
    public String getOpenStreetMapHouseNumberDir() {
	return this.openStreetMapHouseNumberDir;
    }

    /**
     * The directory where the openStreetMap files will be retrieved and
     * processed. It must ends with / or \ according to the System
     * 
     * @param importeropenStreetMapDir
     *            the option
     */
    @Required
    public void setOpenStreetMapDir(String importeropenStreetMapDir) {
	if (!importeropenStreetMapDir.endsWith(File.separator)) {
	    logger.debug(openStreetMapDir + " does not end with " + File.separator);
	    this.openStreetMapDir = importeropenStreetMapDir + File.separator;
	} else {
	    this.openStreetMapDir = importeropenStreetMapDir;
	}
	logger.debug("set openStreetMapDir to " + this.openStreetMapDir);
    }
    
    

    /**
     * The directory where the openStreetMap files for house number will be retrieved and
     * processed. It must ends with / or \ according to the System
     * 
     * @param importeropenStreetMapHouseNumberDir
     *            the option
     */
    @Required
    public void setOpenStreetMapHouseNumberDir(String importeropenStreetMapHouseNumberDir) {
	if (!importeropenStreetMapHouseNumberDir.endsWith(File.separator)) {
	    logger.debug(openStreetMapHouseNumberDir + " does not end with " + File.separator);
	    this.openStreetMapHouseNumberDir = importeropenStreetMapHouseNumberDir + File.separator;
	} else {
	    this.openStreetMapHouseNumberDir = importeropenStreetMapHouseNumberDir;
	}
	logger.debug("set openStreetMapHouseNumberDir to " + this.openStreetMapHouseNumberDir);
    }

    /**
     * The directory where the openStreetMap files will be retrieved and
     * processed. It must ends with / or \ according to the System
     * 
     * @param geonamesZipCodeDir
     *            the option
     */
    @Required
    public void setGeonamesZipCodeDir(String geonamesZipCodeDir) {
	if (!geonamesZipCodeDir.endsWith(File.separator)) {
	    logger.debug(geonamesZipCodeDir + " does not end with " + File.separator);
	    this.geonamesZipCodeDir = geonamesZipCodeDir + File.separator;
	} else {
	    this.geonamesZipCodeDir = geonamesZipCodeDir;
	}
	logger.debug("set geonamesZipCodeDir to " + this.geonamesZipCodeDir);
    }

    public String getGeonamesZipCodeDir() {
	return geonamesZipCodeDir;
    }

    /**
     * The directory where the Geonames files will be retrieved and processed.
     * It must ends with / or \ according to the System
     * 
     * @param importerGeonamesDir
     *            the option
     */
    @Required
    public void setGeonamesDir(String importerGeonamesDir) {
	if (!importerGeonamesDir.endsWith(File.separator)) {
	    logger.debug(importerGeonamesDir + " does not end with " + File.separator);
	    this.geonamesDir = importerGeonamesDir + File.separator;
	} else {
	    this.geonamesDir = importerGeonamesDir;
	}
	logger.debug("set geonamesDir to " + this.geonamesDir);
    }

    /**
     * @return The option
     * @see #setGeonamesDir(String)
     */
    public String getGeonamesDir() {
	return this.geonamesDir;
    }

    /**
     * @return The option
     * @see #setOpenstreetMapDownloadURL(String)
     */
    public String getOpenstreetMapDownloadURL() {
	return openstreetMapDownloadURL;
    }
    
    
    /**
     * @return The option
     * @see #setOpenstreetMaphouseNumberDownloadURL(String)
     */
    public String getOpenstreetMaphouseNumberDownloadURL() {
	return openstreetMaphouseNumberDownloadURL;
    }

    /**
     * The HTTP URL of the directory Where openstreetmap files are to be
     * download from
     * 
     * @param openstreetMapDownloadURL
     *            The option
     */
    @Required
    public void setOpenstreetMapDownloadURL(String openstreetMapDownloadURL) {
	if (!openstreetMapDownloadURL.endsWith("/")) {
	    this.openstreetMapDownloadURL = openstreetMapDownloadURL + "/";
	} else {
	    this.openstreetMapDownloadURL = openstreetMapDownloadURL;
	}
	logger.debug("set openstreetMapDownloadURL to " + this.openstreetMapDownloadURL);
    }
    
    /**
     * The HTTP URL of the directory Where openstreetmap house number files are to be
     * download from
     * 
     * @param openstreetMaphouseNumberDownloadURL
     *            The option
     */
    @Required
    public void setOpenstreetMaphouseNumberDownloadURL(String openstreetMaphouseNumberDownloadURL) {
	if (!openstreetMaphouseNumberDownloadURL.endsWith("/")) {
	    this.openstreetMaphouseNumberDownloadURL = openstreetMaphouseNumberDownloadURL + "/";
	} else {
	    this.openstreetMaphouseNumberDownloadURL = openstreetMaphouseNumberDownloadURL;
	}
	logger.debug("set openstreetMaphouseNumberDownloadURL to " + this.openstreetMaphouseNumberDownloadURL);
    }

    /**
     * @return The option
     * @see #setGeonamesDownloadURL(String)
     */
    public String getGeonamesDownloadURL() {
	return geonamesDownloadURL;
    }

    /**
     * The HTTP URL of the directory Where Geonames files are to be download
     * from
     * 
     * @param importerGeonamesDownloadURL
     *            The option
     */
    @Required
    public void setGeonamesDownloadURL(String importerGeonamesDownloadURL) {
	if (!importerGeonamesDownloadURL.endsWith("/")) {
	    this.geonamesDownloadURL = importerGeonamesDownloadURL + "/";
	} else {
	    this.geonamesDownloadURL = importerGeonamesDownloadURL;
	}
	logger.debug("set geonamesDownloadURL to " + this.geonamesDownloadURL);
    }

    /**
     * @return The option
     * @see #setGeonamesDownloadURL(String)
     */
    public String getGeonamesZipCodeDownloadURL() {
	return geonamesZipCodeDownloadURL;
    }

    /**
     * The HTTP URL of the directory Where Geonames zip files are to be download
     * from
     * 
     * @param geonamesZipCodeDownloadURL
     *            The option
     */
    @Required
    public void setGeonamesZipCodeDownloadURL(String geonamesZipCodeDownloadURL) {
	if (!geonamesZipCodeDownloadURL.endsWith("/")) {
	    this.geonamesZipCodeDownloadURL = geonamesZipCodeDownloadURL + "/";
	} else {
	    this.geonamesZipCodeDownloadURL = geonamesZipCodeDownloadURL;
	}
	logger.debug("set geonamesZipCodeDownloadURL to " + this.geonamesZipCodeDownloadURL);
    }

    /**
     * @return The option
     * @see #setRetrieveFiles(boolean)
     */
    public boolean isRetrieveFiles() {
	return this.retrieveFiles;
    }

    /**
     * Whether we should download the geonames file or use the one already
     * present in the {@link #geonamesDir}
     * 
     * @param retrieveFiles
     *            The options
     */
    @Required
    public void setRetrieveFiles(boolean retrieveFiles) {
	this.retrieveFiles = retrieveFiles;
    }

    /**
     * @return The option
     * @see #setOpenStreetMapFilesToDownload(String)
     */
    public String getOpenStreetMapFilesToDownload() {
	return this.openStreetMapFilesToDownload;
    }
    
    
    /**
     * @return The option
     * @see #setOpenStreetMapHouseNumberFilesToDownload(String)
     */
    public String getOpenStreetMapHouseNumberFilesToDownload() {
	return this.openStreetMapHouseNumberFilesToDownload;
    }

    /**
     * The list of the Openstreetmap to be download from the
     * {@link #openstreetMapDownloadURL}. the several files will be separated by
     * {@link #OPTION_SEPARATOR}. if null or empty, will be set to {
     * {@link #OPENSTREETMAP_DEFAULT_FILES_TO_DOWNLOAD}
     * 
     * @param openStreetMapFilesToDownload
     *            The openstreetmap filesToDownload to set
     */
    @Required
    public void setOpenStreetMapFilesToDownload(String openStreetMapFilesToDownload) {
	if (openStreetMapFilesToDownload == null || openStreetMapFilesToDownload.trim().equals("")) {
	    logger.warn("the option openStreetMapFilesToDownload is not set and will be set to his default value : " + OPENSTREETMAP_DEFAULT_FILES_TO_DOWNLOAD);
	    this.openStreetMapFilesToDownload = OPENSTREETMAP_DEFAULT_FILES_TO_DOWNLOAD;
	} else {
	    this.openStreetMapFilesToDownload = openStreetMapFilesToDownload;
	    logger.info("openStreetMapFilesToDownload=" + openStreetMapFilesToDownload);
	}
    }
    
    /**
     * The list of the Openstreetmap house number files to be download from the
     * {@link #openstreetMaphouseNumberDownloadURL}. the several files will be separated by
     * {@link #OPTION_SEPARATOR}. if null or empty, will be set to {
     * {@link #OPENSTREETMAP_HOUSENUMBER_DEFAULT_FILES_TO_DOWNLOAD}
     * 
     * @param openStreetMapFilesToDownload
     *            The openstreetmap filesToDownload to set
     */
    @Required
    public void setOpenStreetMapHouseNumberFilesToDownload(String openStreetMapHouseNumberFilesToDownload) {
	if (openStreetMapHouseNumberFilesToDownload == null || openStreetMapHouseNumberFilesToDownload.trim().equals("")) {
	    logger.warn("the option openStreetMapHouseNumberFilesToDownload is not set and will be set to his default value : " + OPENSTREETMAP_HOUSENUMBER_DEFAULT_FILES_TO_DOWNLOAD);
	    this.openStreetMapHouseNumberFilesToDownload = OPENSTREETMAP_HOUSENUMBER_DEFAULT_FILES_TO_DOWNLOAD;
	} else {
	    this.openStreetMapHouseNumberFilesToDownload = openStreetMapHouseNumberFilesToDownload;
	    logger.info("openStreetMapHouseNumberFilesToDownload=" + openStreetMapHouseNumberFilesToDownload);
	}
    }

    /**
     * @return The option
     * @see #setGeonamesFilesToDownload(String)
     */
    public String getGeonamesFilesToDownload() {
	return this.geonamesFilesToDownload;
    }

    /**
     * The list of the Geonames files to be download from the
     * {@link #geonamesDownloadURL}. the several files will be separated by
     * {@link #OPTION_SEPARATOR}, il not set or null, defaulting to {@link #GEONAMES_DEFAULT_FILES_TO_DOWNLOAD}
     * 
     * @param geonamesFilesToDownload
     *            the filesToDownload to set
     */
    @Required
    public void setGeonamesFilesToDownload(String geonamesFilesToDownload) {
	if (geonamesFilesToDownload == null || geonamesFilesToDownload.trim().equals("")) {
	    logger.warn("the option geonamesFilesToDownload is not set and will be set to his default value : " + GEONAMES_DEFAULT_FILES_TO_DOWNLOAD);
	    this.geonamesFilesToDownload = GEONAMES_DEFAULT_FILES_TO_DOWNLOAD;
	} else {
	    this.geonamesFilesToDownload = geonamesFilesToDownload;
	    logger.info("geonamesFilesToDownload=" + geonamesFilesToDownload);
	}
    }

    /**
     * @return the option
     * @see #getAdm1FileName()
     */
    public String getAdm1FileName() {
	return this.adm1FileName;
    }

    /**
     * The name of the Geonames dump file containing the ADM with level 1
     * 
     * @param adm1FileName
     *            The option
     */
    @Required
    public void setAdm1FileName(String adm1FileName) {
	this.adm1FileName = adm1FileName;
    }

    /**
     * @return The option
     * @see #getAdm2FileName()
     */
    public String getAdm2FileName() {
	return this.adm2FileName;
    }

    /**
     * The name of the Geonames dump file containing the ADM with level 2
     * 
     * @param adm2FileName
     *            The option
     */
    @Required
    public void setAdm2FileName(String adm2FileName) {
	this.adm2FileName = adm2FileName;
    }

    /**
     * @return The option
     * @see #getAdm3FileName()
     */
    public String getAdm3FileName() {
	return this.adm3FileName;
    }

    /**
     * The name of the Geonames dump file containing the ADM with level 3
     * 
     * @param adm3FileName
     *            the adm3FileName to set
     */
    @Required
    public void setAdm3FileName(String adm3FileName) {
	this.adm3FileName = adm3FileName;
    }

    /**
     * @return The option
     * @see #getAdm4FileName()
     */
    public String getAdm4FileName() {
	return adm4FileName;
    }

    /**
     * The name of the Geonames dump file containing the ADM with level 4
     * 
     * @param adm4FileName
     *            The option
     */
    @Required
    public void setAdm4FileName(String adm4FileName) {
	this.adm4FileName = adm4FileName;
    }

    /**
     * @return The Option
     * @see #getCountriesFileName()
     */
    public String getCountriesFileName() {
	return this.countriesFileName;
    }

    /**
     * The name of the Geonames dump file containing the countries informations
     * 
     * @param countryFileName
     *            The option
     */
    @Required
    public void setCountriesFileName(String countryFileName) {
	this.countriesFileName = countryFileName;
    }

    /**
     * @return The option
     * @see #setLanguageFileName(String)
     */
    public String getLanguageFileName() {
	return this.languageFileName;
    }

    /**
     * The name of the Geonames dump file containing the language informations
     * 
     * @param languageFileName
     *            The option
     */
    @Required
    public void setLanguageFileName(String languageFileName) {
	this.languageFileName = languageFileName;
    }

    /**
     * @return The option
     * @see #getAlternateNamesFileName()
     */
    public String getAlternateNamesFileName() {
	return alternateNamesFileName;
    }

    /**
     * The name of the Geonames dump file containing the alternate names
     * 
     * @param alternateNamesFileName
     *            The option
     */
    @Required
    public void setAlternateNamesFileName(String alternateNamesFileName) {
	this.alternateNamesFileName = alternateNamesFileName;
    }

    /**
     * Optional setting that allows to specify the number of inserts that can be
     * done before flushing. This is useful since most ORM technologies use a
     * so-called Level-2 cache that will store all the persisted data until they
     * are either comitted or flushed...default value is
     * {@link #DEFAULT_MAX_INSERT_BEFORE_FLUSH}
     * 
     * @param maxInsertsBeforeFlush
     *            The option
     */
    @Required
    public void setMaxInsertsBeforeFlush(int maxInsertsBeforeFlush) {
	this.maxInsertsBeforeFlush = maxInsertsBeforeFlush;
    }

    /**
     * @return The option
     * @see #setMaxInsertsBeforeFlush(int)
     */
    public int getMaxInsertsBeforeFlush() {
	return this.maxInsertsBeforeFlush;
    }

    /**
     * @param directoryPath
     *            The directory to check. it can be absolute or relative
     * @return true if the path is a directory (not a file) AND exists AND is
     *         writable
     */
    private boolean isDirectoryAccessible(String directoryPath) {
	File dir = new File(directoryPath);
	return dir.exists() && dir.isDirectory() && dir.canWrite();
    }

    /**
     * @return true if the directory with the file to import exists and is
     *         accessible
     */
    public boolean isGeonamesDownloadDirectoryAccessible() {
	return isDirectoryAccessible(getGeonamesDir());
    }

    /**
     * @return true if the directory with the file to import exists and is
     *         accessible
     */
    public boolean isOpenStreetMapDownloadDirectoryAccessible() {
	return isDirectoryAccessible(getOpenStreetMapDir());
    }
    
    /**
     * @return true if the directory with the file to import exists and is
     *         accessible
     */
    public boolean isOpenStreetMapHouseNumberDownloadDirectoryAccessible() {
	return isDirectoryAccessible(getOpenStreetMapHouseNumberDir());
    }

    /**
     * @return true if the regexp of the feature class/ code are correct
     */
    public boolean isRegexpCorrects() {
	return ImporterHelper.compileRegex(getAcceptRegExString()) != null;
    }

    /**
     * @return true if the config is Ok to process the import
     */
    public boolean isConfigCorrectForImport() {
	return isRegexpCorrects() && isGeonamesDownloadDirectoryAccessible() && isOpenStreetMapDownloadDirectoryAccessible() && isOpenStreetMapHouseNumberDownloadDirectoryAccessible();
    }
    
   
    /**
     * @return the path to the file that give the information if the import is
     *         done or not
     */
    public String getAlreadyDoneFilePath() {
	return getImporterMetadataDirectoryPath() + ALREADY_DONE_FILE_NAME;
    }

    /**
     * @return the path to the file that give the information if the import is
     *         done or not
     */
    public String getImporterMetadataDirectoryPath() {
	return getGeonamesDir() + IMPORTER_METADATA_RELATIVE_PATH + File.separator;
    }

    /**
     * Create the importerMetadataDirectory
     * 
     * @return the path to the importerMetadataDirectory
     */
    public String createImporterMetadataDirIfItDoesnTExist() {
	if (!isGeonamesDownloadDirectoryAccessible()) {
	    if (!new File(getGeonamesDir()).mkdir()) {
		throw new RuntimeException("the geonameDirectory doesn't exists and we can not create it ");
	    }
	}
	String dirpath = getGeonamesDir() + IMPORTER_METADATA_RELATIVE_PATH + File.separator;
	File directory = new File(dirpath);
	if (!directory.exists()) {
	    if (!directory.mkdir()) {
		throw new RuntimeException("Can not create ImporterMetadataDirectory");
	    }
	}
	return dirpath;
    }

    /**
     * Get the name of the file where the alternate names of features that are
     * not adm1, adm2, or country are
     * 
     * @see #setAlternateNameFeaturesFileName(String)
     * @return The name of the file
     */
    public String getAlternateNameFeaturesFileName() {
	return alternateNameFeaturesFileName;
    }

    /**
     * Set the name of the file where the alternate names of features that are
     * not adm1, adm2, or country are
     * 
     * @see #getAlternateNameFeaturesFileName()
     * @param alternateNameFeaturesFileName
     *            The name of the file to set
     */
    @Required
    public void setAlternateNameFeaturesFileName(String alternateNameFeaturesFileName) {
	this.alternateNameFeaturesFileName = alternateNameFeaturesFileName;
    }

    /**
     * Get the name of the file where the alternate names of adm with level 1
     * are
     * 
     * @see #setAlternateNameAdm1FileName(String)
     * @return The name of the file
     */
    public String getAlternateNameAdm1FileName() {
	return alternateNameAdm1FileName;
    }

    /**
     * Set the name of the file where the alternate names of adm with level 1
     * are
     * 
     * @see #getAlternateNameAdm1FileName()
     * @param alternateNameAdm1FileName
     *            The name of the file to set
     */
    @Required
    public void setAlternateNameAdm1FileName(String alternateNameAdm1FileName) {
	this.alternateNameAdm1FileName = alternateNameAdm1FileName;
    }

    /**
     * Get the name of the file where the alternate names of adm with level 2
     * are
     * 
     * @see #setAlternateNameAdm2FileName(String)
     * @return The name of the file
     */
    public String getAlternateNameAdm2FileName() {
	return alternateNameAdm2FileName;
    }

    /**
     * Set the name of the file where the alternate names of adm with level 2
     * are
     * 
     * @see #getAlternateNameAdm2FileName()
     * @param alternateNameAdm2FileName
     *            The name of the file to set
     */
    @Required
    public void setAlternateNameAdm2FileName(String alternateNameAdm2FileName) {
	this.alternateNameAdm2FileName = alternateNameAdm2FileName;
    }

    /**
     * Get the name of the file where the alternate names of countries are
     * 
     * @see #setAlternateNameCountryFileName(String)
     * @return The name of the file
     */
    public String getAlternateNameCountryFileName() {
	return alternateNameCountryFileName;
    }

    /**
     * Set the name of the file where the alternate names of countries are
     * 
     * @see #getAlternateNameCountryFileName()
     * @param alternateNameCountryFileName
     *            The name of the file to set
     */
    @Required
    public void setAlternateNameCountryFileName(String alternateNameCountryFileName) {
	this.alternateNameCountryFileName = alternateNameCountryFileName;
    }

    /**
     * if we search for the nearest city in geonames data to fill the is_in
     * field this increase the time of the importer but strongly increase the
     * relevance of the geocoder
     */
    public boolean isOpenStreetMapFillIsIn() {
	return openStreetMapFillIsIn;
    }

    /**
     * @see #isOpenStreetMapFillIsIn()
     */
    public void setOpenStreetMapFillIsIn(boolean openStreetMapFillIsIn) {
	this.openStreetMapFillIsIn = openStreetMapFillIsIn;
	CommentedProperties.editPropertyFromClassPathRessource(PROPERTIES_CONFIG_FILE_CLASSPATH, OPENSTREETMAP_FILL_ISIN_FIELD_NAME, String.valueOf(openStreetMapFillIsIn));
    }

}
