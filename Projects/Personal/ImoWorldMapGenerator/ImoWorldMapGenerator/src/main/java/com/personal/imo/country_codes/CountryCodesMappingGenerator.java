package com.personal.imo.country_codes;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.personal.imo.country_codes.mappings.CountryCodeMapping;
import com.personal.imo.country_codes.mappings.FactoryCountryCodeMapping;
import com.utils.io.PathUtils;
import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;

final class CountryCodesMappingGenerator {

	private CountryCodesMappingGenerator() {
	}

	public static void main(
			final String[] args) {

		final String countryCodeMappingsFilePathString = PathUtils.computePath("src", "main", "resources",
				"com", "personal", "imo", "country_codes", "default_country_code_mapping.csv");

		Logger.printProgress("generating Java country code mappings to:");
		Logger.printLine(countryCodeMappingsFilePathString);

		final List<CountryCodeMapping> countryCodeMappingList = new ArrayList<>();
		for (final String countryCode : Locale.getISOCountries()) {

			final Locale locale = new Locale("en", countryCode);
			final String countryName = locale.getDisplayCountry();
			final String longCountryCode = locale.getISO3Country();
			final CountryCodeMapping countryCodeMapping =
					FactoryCountryCodeMapping.newInstance(countryName, longCountryCode, countryCode);
			countryCodeMappingList.add(countryCodeMapping);
		}

		countryCodeMappingList.sort(Comparator.comparing(CountryCodeMapping::getCountryName));

		final boolean success = FactoryFolderCreator.getInstance()
				.createParentDirectories(countryCodeMappingsFilePathString, true);
		if (success) {

			try (final PrintStream printStream = StreamUtils.openPrintStream(countryCodeMappingsFilePathString)) {

				for (final CountryCodeMapping countryCodeMapping : countryCodeMappingList) {
					countryCodeMapping.writeToCsv(printStream);
				}

			} catch (final Exception exc) {
				Logger.printError("failed to generate country code mappings file");
				Logger.printException(exc);
			}
		}
	}
}
