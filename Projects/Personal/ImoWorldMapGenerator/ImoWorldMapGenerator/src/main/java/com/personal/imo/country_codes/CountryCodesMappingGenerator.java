package com.personal.imo.country_codes;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.personal.imo.country_codes.mappings.CountryCodeMapping;
import com.personal.imo.country_codes.mappings.FactoryCountryCodeMapping;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;

final class CountryCodesMappingGenerator {

	private CountryCodesMappingGenerator() {
	}

	public static void main(
            final String[] args) {

		final Path countryCodeMappingsFilePath = Paths.get("src", "main", "resources",
				"com", "personal", "imo", "country_codes", "country_code_mapping.csv").toAbsolutePath();

		Logger.printProgress("generating Java country code mappings to:");
		Logger.printLine(countryCodeMappingsFilePath);

		final List<CountryCodeMapping> countryCodeMappingList = new ArrayList<>();
		for (final String countryCode : Locale.getISOCountries()) {

			final Locale locale = new Locale("en", countryCode);
            final String longCountryCode = locale.getISO3Country();
			final CountryCodeMapping countryCodeMapping =
					FactoryCountryCodeMapping.newInstance(longCountryCode, countryCode);
			countryCodeMappingList.add(countryCodeMapping);
		}

		countryCodeMappingList.sort(Comparator.comparing(CountryCodeMapping::getLongCountryCode));

		final boolean success = FactoryFolderCreator.getInstance()
				.createParentDirectories(countryCodeMappingsFilePath, true);
		if (success) {

			try (final PrintStream printStream = new PrintStream(
					new BufferedOutputStream(Files.newOutputStream(countryCodeMappingsFilePath)))) {

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
