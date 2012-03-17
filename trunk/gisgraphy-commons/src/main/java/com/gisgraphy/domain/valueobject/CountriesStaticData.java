package com.gisgraphy.domain.valueobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class CountriesStaticData {
	public final static Map<String, String> countriesnameToCountryCodeMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 4762318033721437587L;

		{
			put("Afghanistan", "AF");
			put("Aland Islands", "AX");
			put("Albania", "AL");
			put("Algeria", "DZ");
			put("American Samoa", "AS");
			put("Andorra", "AD");
			put("Angola", "AO");
			put("Anguilla", "AI");
			put("Antarctica", "AQ");
			put("Antigua and Barbuda", "AG");
			put("Argentina", "AR");
			put("Armenia", "AM");
			put("Aruba", "AW");
			put("Australia", "AU");
			put("Austria", "AT");
			put("Azerbaijan", "AZ");
			put("Bahamas", "BS");
			put("Bahrain", "BH");
			put("Bangladesh", "BD");
			put("Barbados", "BB");
			put("Belarus", "BY");
			put("Belgium", "BE");
			put("Belize", "BZ");
			put("Benin", "BJ");
			put("Bermuda", "BM");
			put("Bhutan", "BT");
			put("Bolivia", "BO");
			put("Bosnia and Herzegovina", "BA");
			put("Botswana", "BW");
			put("Bouvet Island", "BV");
			put("Brazil", "BR");
			put("British Indian Ocean Territory", "IO");
			put("British Virgin Islands", "VG");
			put("Brunei", "BN");
			put("Bulgaria", "BG");
			put("Burkina Faso", "BF");
			put("Burundi", "BI");
			put("Cambodia", "KH");
			put("Cameroon", "CM");
			put("Canada", "CA");
			put("Cape Verde", "CV");
			put("Cayman Islands", "KY");
			put("Central African Republic", "CF");
			put("Chad", "TD");
			put("Chile", "CL");
			put("China", "CN");
			put("Christmas Island", "CX");
			put("Cocos Islands", "CC");
			put("Colombia", "CO");
			put("Comoros", "KM");
			put("Cook Islands", "CK");
			put("Costa Rica", "CR");
			put("Croatia", "HR");
			put("Cuba", "CU");
			put("Cyprus", "CY");
			put("Czech Republic", "CZ");
			put("Democratic Republic of the Congo", "CD");
			put("Denmark", "DK");
			put("Djibouti", "DJ");
			put("Dominica", "DM");
			put("Dominican Republic", "DO");
			put("East Timor", "TL");
			put("Ecuador", "EC");
			put("Egypt", "EG");
			put("El Salvador", "SV");
			put("Equatorial Guinea", "GQ");
			put("Eritrea", "ER");
			put("Estonia", "EE");
			put("Ethiopia", "ET");
			put("Falkland Islands", "FK");
			put("Faroe Islands", "FO");
			put("Fiji", "FJ");
			put("Finland", "FI");
			put("France", "FR");
			put("French Guiana", "GF");
			put("French Polynesia", "PF");
			put("French Southern Territories", "TF");
			put("Gabon", "GA");
			put("Gambia", "GM");
			put("Georgia", "GE");
			put("Germany", "DE");
			put("Ghana", "GH");
			put("Gibraltar", "GI");
			put("Greece", "GR");
			put("Greenland", "GL");
			put("Grenada", "GD");
			put("Guadeloupe", "GP");
			put("Guam", "GU");
			put("Guatemala", "GT");
			put("Guernsey", "GG");
			put("Guinea", "GN");
			put("Guinea-Bissau", "GW");
			put("Guyana", "GY");
			put("Haiti", "HT");
			put("Heard Island and McDonald Islands", "HM");
			put("Honduras", "HN");
			put("Hong Kong", "HK");
			put("Hungary", "HU");
			put("Iceland", "IS");
			put("India", "IN");
			put("Indonesia", "ID");
			put("Iran", "IR");
			put("Iraq", "IQ");
			put("Ireland", "IE");
			put("Isle of Man", "IM");
			put("Israel", "IL");
			put("Italy", "IT");
			put("Ivory Coast", "CI");
			put("Jamaica", "JM");
			put("Japan", "JP");
			put("Jersey", "JE");
			put("Jordan", "JO");
			put("Kazakhstan", "KZ");
			put("Kenya", "KE");
			put("Kiribati", "KI");
			put("Kosovo", "XK");
			put("Kuwait", "KW");
			put("Kyrgyzstan", "KG");
			put("Laos", "LA");
			put("Latvia", "LV");
			put("Lebanon", "LB");
			put("Lesotho", "LS");
			put("Liberia", "LR");
			put("Libya", "LY");
			put("Liechtenstein", "LI");
			put("Lithuania", "LT");
			put("Luxembourg", "LU");
			put("Macao", "MO");
			put("Macedonia", "MK");
			put("Madagascar", "MG");
			put("Malawi", "MW");
			put("Malaysia", "MY");
			put("Maldives", "MV");
			put("Mali", "ML");
			put("Malta", "MT");
			put("Marshall Islands", "MH");
			put("Martinique", "MQ");
			put("Mauritania", "MR");
			put("Mauritius", "MU");
			put("Mayotte", "YT");
			put("Mexico", "MX");
			put("Micronesia", "FM");
			put("Moldova", "MD");
			put("Monaco", "MC");
			put("Mongolia", "MN");
			put("Montenegro", "ME");
			put("Montserrat", "MS");
			put("Morocco", "MA");
			put("Mozambique", "MO");
			put("Myanmar", "MM");
			put("Namibia", "NA");
			put("Nauru", "NR");
			put("Nepal", "NP");
			put("Netherlands", "NL");
			put("Netherlands Antilles", "AN");
			put("New Caledonia", "NC");
			put("New Zealand", "NZ");
			put("Nicaragua", "NI");
			put("Niger", "NE");
			put("Nigeria", "NG");
			put("Niue", "NU");
			put("Norfolk Island", "NF");
			put("Northern Mariana Islands", "MP");
			put("North Korea", "KP");
			put("Norway", "NO");
			put("Oman", "OM");
			put("Pakistan", "PK");
			put("Palau", "PW");
			put("Palestinian Territory", "PS");
			put("Panama", "PA");
			put("Papua New Guinea", "PG");
			put("Paraguay", "PY");
			put("Peru", "PE");
			put("Philippines", "PH");
			put("Pitcairn", "PN");
			put("Poland", "PL");
			put("Portugal", "PT");
			put("Puerto Rico", "PR");
			put("Qatar", "QA");
			put("Republic of the Congo", "CG");
			put("Reunion", "RE");
			put("Romania", "RO");
			put("Russia", "RU");
			put("Rwanda", "RW");
			put("Saint Barthélemy", "BL");
			put("Saint Helena", "SH");
			put("Saint Kitts and Nevis", "KN");
			put("Saint Lucia", "LC");
			put("Saint Martin", "MF");
			put("Saint Pierre and Miquelon", "PM");
			put("Saint Vincent and the Grenadines", "VC");
			put("Samoa", "WS");
			put("San Marino", "SM");
			put("Sao Tome and Principe", "ST");
			put("Saudi Arabia", "SA");
			put("Senegal", "SN");
			put("Serbia", "RS");
			put("Serbia and Montenegro", "CS");
			put("Seychelles", "SC");
			put("Sierra Leone", "SL");
			put("Singapore", "SG");
			put("Slovakia", "SK");
			put("Slovenia", "SI");
			put("Solomon Islands", "SB");
			put("Somalia", "SO");
			put("South Africa", "ZA");
			put("South Georgia and the South Sandwich Islands", "GS");
			put("South Korea", "KR");
			put("Spain", "ES");
			put("Sri Lanka", "LK");
			put("Sudan", "SD");
			put("Suriname", "SR");
			put("Svalbard and Jan Mayen", "SJ");
			put("Swaziland", "SZ");
			put("Sweden", "SE");
			put("Switzerland", "CH");
			put("Syria", "SY");
			put("Taiwan", "TW");
			put("Tajikistan", "TJ");
			put("Tanzania", "TZ");
			put("Thailand", "TH");
			put("Togo", "TG");
			put("Tokelau", "TK");
			put("Tonga", "TO");
			put("Trinidad and Tobago", "TT");
			put("Tunisia", "TN");
			put("Turkey", "TR");
			put("Turkmenistan", "TM");
			put("Turks and Caicos Islands", "TC");
			put("Tuvalu", "TV");
			put("Uganda", "UG");
			put("Ukraine", "UA");
			put("United Arab Emirates", "AE");
			put("United Kingdom", "GB");
			put("United States", "US");
			put("United States Minor Outlying Islands", "UM");
			put("Uruguay", "UY");
			put("U.S. Virgin Islands", "VI");
			put("Uzbekistan", "UZ");
			put("Vanuatu", "VU");
			put("Vatican", "VA");
			put("Venezuela", "VE");
			put("Vietnam", "VN");
			put("Wallis and Futuna", "WF");
			put("Western Sahara", "EH");
			put("Yemen", "YE");
			put("Zambia", "ZM");
			put("Zimbabwe", "ZW");

		}
	};

	public static ArrayList<String> sortedCountriesName = new ArrayList<String>(CountriesStaticData.countriesnameToCountryCodeMap.keySet()) {
		private static final long serialVersionUID = 1671688713929233996L;
		{
			Collections.sort(this);

		}
	};
	
	public static int getNumberOfCountries(){
		return sortedCountriesName.size();
	}

	public static String getCountryCodeFromCountryName(String countryname) {
		return countriesnameToCountryCodeMap.get(countryname);
	}
	
	/**
	 * @param position the position in the sorted list (starts from 0)
	 * @return the country code for the given position, in the sorted @link {@value #sortedCountriesName}
	 * @throws IllegalArgumentException if position is negative
	 * @throws IndexOutOfBoundsException if position is too high
	 *  * @see {@link #getNumberOfCountries()}
	 */
	public static String getCountryCodeFromPosition(int position) {
		if (position<0){
			throw new IllegalArgumentException("position should be positive or null");
		}
		return getCountryCodeFromCountryName(getCountryNameFromPosition(position));
	}
	
	/**
	 * @param position the position in the {@value #sortedCountriesName} (starts from 0)
	 * @return the country name for the given position, in the sorted @link {@value #sortedCountriesName}
	 *  * @throws IllegalArgumentException if position is negative
	 * @throws IndexOutOfBoundsException if position is too high
	 * @see {@link #getNumberOfCountries()}
	 */
	public static String getCountryNameFromPosition(int position) {
		if (position<0){
			throw new IllegalArgumentException("position should be positive or null");
		}
		return sortedCountriesName.get(position);
	}

	/**
	 * @param countryCode the country code (iso 3166 alpha2)
	 * @return the country name
	 */
	public static String getCountryNameFromCountryCode(String countryCode) {
		for (Entry<String, String> entry : countriesnameToCountryCodeMap.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(countryCode)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * @param countryCode the country code (iso 3166 alpha2)
	 * @return the position in the sortedCountryList. starts at 0
	 */
	public static int getPositionFromCountryCode(String countryCode) {
		int position = 0;
		String countryNameToSearch = getCountryNameFromCountryCode(countryCode);
		for (String country : sortedCountriesName) {
			if (country.equalsIgnoreCase(countryNameToSearch)) {
				return position;
			}
			position++;
		}
		return 0;
	}
}
