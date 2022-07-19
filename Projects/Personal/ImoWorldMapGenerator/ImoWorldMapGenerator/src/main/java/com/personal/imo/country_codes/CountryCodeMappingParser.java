package com.personal.imo.country_codes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.personal.imo.country_codes.mappings.CountryCodeMapping;
import com.personal.imo.country_codes.mappings.FactoryCountryCodeMapping;
import com.utils.io.IoUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class CountryCodeMappingParser {

	private final List<CountryCodeMapping> countryCodeMappingList;

	public CountryCodeMappingParser() {

		countryCodeMappingList = new ArrayList<>();
	}

	public void work() {

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(IoUtils.resourceFileToInputStream(
						"com/personal/imo/country_codes/country_code_mapping.csv")))) {

			String line;
			while ((line = bufferedReader.readLine()) != null) {

				final CountryCodeMapping countryCodeMapping =
						FactoryCountryCodeMapping.newInstance(line);
				if (countryCodeMapping != null) {
					countryCodeMappingList.add(countryCodeMapping);
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public List<CountryCodeMapping> getCountryCodeMappingList() {
		return countryCodeMappingList;
	}
}
