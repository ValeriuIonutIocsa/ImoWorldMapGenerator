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
			if (linePartArray.length == 2) {

				final String longCountryCode = linePartArray[0];
				final String countryCode = linePartArray[1];
				countryCodeMapping = newInstance(longCountryCode, countryCode);

			} else {
				countryCodeMapping = null;
			}
		} else {
			countryCodeMapping = null;
		}
		return countryCodeMapping;
	}

	public static CountryCodeMapping newInstance(
			final String longCountryCode,
			final String countryCode) {
		return new CountryCodeMapping(longCountryCode, countryCode);
	}
}
