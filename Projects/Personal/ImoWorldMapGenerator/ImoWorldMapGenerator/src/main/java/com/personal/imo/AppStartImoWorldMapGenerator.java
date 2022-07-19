package com.personal.imo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.personal.imo.country_codes.CountryCodeMappingParser;
import com.personal.imo.country_codes.mappings.CountryCodeMapping;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.xml.dom.XmlDomUtils;

final class AppStartImoWorldMapGenerator {

	private AppStartImoWorldMapGenerator() {
	}

	public static void main(
			final String[] args) {

		final Instant start = Instant.now();
		Logger.printProgress("starting ImoWorldMapGenerator");

		if (args == null || args.length < 2) {
			Logger.printError("insufficient arguments");

		} else {
			final String resultsXmlFilePathString = args[0];
			final Path resultsXmlFilePath =
					PathUtils.tryParsePath("results XML", resultsXmlFilePathString);
			if (resultsXmlFilePath != null) {

				final List<String> longCountryCodeList = new ArrayList<>();
				fillLongCountryCodeList(resultsXmlFilePath, longCountryCodeList);

				final List<String> countryCodeList = new ArrayList<>();
				fillCountryCodeList(longCountryCodeList, countryCodeList);

				final String outputHtmlFilePathString = args[1];
				final Path outputHtmlFilePath =
						PathUtils.tryParsePath("output HTML", outputHtmlFilePathString);
				if (outputHtmlFilePath != null) {

					writeOutputHtmlFile(countryCodeList, outputHtmlFilePath);
					Logger.printFinishMessage(start);
				}
			}
		}
	}

	private static void fillLongCountryCodeList(
			final Path resultsXmlFilePath,
			final List<String> longCountryCodeList) {

		try {
			Logger.printProgress("parsing input XML file:");
			Logger.printLine(resultsXmlFilePath);

			final Document document = XmlDomUtils.openDocument(resultsXmlFilePath);
			final Element documentElement = document.getDocumentElement();

			final List<Element> countryElementList =
					XmlDomUtils.getElementsByTagName(documentElement, "country");
			for (final Element countryElement : countryElementList) {

				final String longCountryCode = countryElement.getAttribute("code");
				longCountryCodeList.add(longCountryCode);
			}

		} catch (final Exception exc) {
			Logger.printError("failed to fill long country code list");
			Logger.printException(exc);
		}
	}

	private static void fillCountryCodeList(
            final List<String> longCountryCodeList,
            final List<String> countryCodeList) {

		final CountryCodeMappingParser countryCodeMappingParser = new CountryCodeMappingParser();
		countryCodeMappingParser.work();

		final List<CountryCodeMapping> countryCodeMappingList =
				countryCodeMappingParser.getCountryCodeMappingList();

		final Map<String, String> longToShortCountryCodeMap = new HashMap<>();
		for (final CountryCodeMapping countryCodeMapping : countryCodeMappingList) {

			final String longCountryCode = countryCodeMapping.getLongCountryCode();
			final String countryCode = countryCodeMapping.getCountryCode();
			longToShortCountryCodeMap.put(longCountryCode, countryCode);
		}

		for (final String longCountryCode : longCountryCodeList) {

			final String countryCode = longToShortCountryCodeMap.getOrDefault(longCountryCode, null);
			if (countryCode == null) {
				Logger.printWarning("found no mapping for long country code " + longCountryCode);
			} else {
				countryCodeList.add(countryCode);
			}
		}
	}

	private static void writeOutputHtmlFile(
			final List<String> countryCodeList,
			final Path outputHtmlFilePath) {

		try {
			Logger.printProgress("writing output HTML file:");
			Logger.printLine(outputHtmlFilePath);

			final boolean success = FactoryFolderCreator.getInstance()
					.createParentDirectories(outputHtmlFilePath, true);
			if (success) {

				try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
						IoUtils.resourceFileToInputStream("com/personal/imo/template.html")));
						final PrintStream printStream = new PrintStream(
								new BufferedOutputStream(Files.newOutputStream(outputHtmlFilePath)))) {

					String line;
					while ((line = bufferedReader.readLine()) != null) {

						if (line.contains("%%COUNTRY_CODES%%")) {

							writeCountryCodesArea(countryCodeList, printStream);

						} else {
							printStream.print(line);
							printStream.println();
						}
					}
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to write output HTML file");
			Logger.printException(exc);
		}
	}

	private static void writeCountryCodesArea(
			final List<String> countryCodeList,
			final PrintStream printStream) {

		for (int i = 0; i < countryCodeList.size(); i++) {

			final String countryCode = countryCodeList.get(i);

			printStream.print("\t{");
			printStream.println();

			printStream.print("\t\t\"id\": \"");
			printStream.print(countryCode);
			printStream.print("\",");
			printStream.println();

			printStream.print("\t\t\"showAsSelected\": true");
			printStream.println();

			if (i < countryCodeList.size() - 1) {
				printStream.print("\t},");
			} else {
				printStream.print("\t}");
			}
			printStream.println();
		}
	}
}
