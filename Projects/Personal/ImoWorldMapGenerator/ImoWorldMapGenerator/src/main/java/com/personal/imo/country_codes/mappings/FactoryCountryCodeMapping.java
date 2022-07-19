package com.personal.imo.country_codes.mappings;

import org.apache.commons.lang3.StringUtils;

public final class FactoryCountryCodeMapping {

	private FactoryCountryCodeMapping() {
	}

	public static CountryCodeMapping newInstance(
			final String line) {

		final CountryCodeMapping countryCodeMapping;
		if (StringUtils.isNotBlank(line)) {

			final String[] linePartArray = StringUtils.split(line, ':');
			if (linePartArray.length == 3) {

				final String countryName = linePartArray[0];
				final String longCountryCode = linePartArray[1];
				final String countryCode = linePartArray[2];
				countryCodeMapping = newInstance(countryName, longCountryCode, countryCode);

			} else {
				countryCodeMapping = null;
			}

		} else {
			countryCodeMapping = null;
		}
		return countryCodeMapping;
	}

	public static CountryCodeMapping newInstance(
			final String countryName,
			final String longCountryCode,
			final String countryCode) {
		return new CountryCodeMapping(countryName, longCountryCode, countryCode);
	}
}
