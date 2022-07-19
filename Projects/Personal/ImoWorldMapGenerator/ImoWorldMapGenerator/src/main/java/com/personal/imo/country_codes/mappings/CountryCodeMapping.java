package com.personal.imo.country_codes.mappings;

import java.io.PrintStream;

import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class CountryCodeMapping {

	private final String countryName;
	private final String longCountryCode;
	private final String countryCode;

	CountryCodeMapping(
			final String countryName,
			final String longCountryCode,
			final String countryCode) {

		this.countryName = countryName;
		this.longCountryCode = longCountryCode;
		this.countryCode = countryCode;
	}

	public void writeToCsv(
			final PrintStream printStream) {

		if (countryName.contains(":")) {
			Logger.printWarning("country name contains separator :");
		}
		printStream.print(countryName);
		printStream.print(':');
		printStream.print(longCountryCode);
		printStream.print(':');
		printStream.print(countryCode);
		printStream.println();
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getCountryName() {
		return countryName;
	}

	public String getLongCountryCode() {
		return longCountryCode;
	}

	public String getCountryCode() {
		return countryCode;
	}
}
