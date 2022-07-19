package com.personal.imo;

import com.utils.string.StrUtils;

class XmlCountry {

	private final String countryName;
	private final String longCountryCode;

	XmlCountry(
			final String countryName,
			final String longCountryCode) {

		this.countryName = countryName;
		this.longCountryCode = longCountryCode;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	String getCountryName() {
		return countryName;
	}

	String getLongCountryCode() {
		return longCountryCode;
	}
}
