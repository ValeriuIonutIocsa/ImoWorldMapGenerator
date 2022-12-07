package com.personal.imo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.personal.imo.country_codes.CountryCodeMappingParser;
import com.personal.imo.country_codes.mappings.CountryCodeMapping;
import com.utils.io.PathUtils;
import com.utils.io.ResourceFileUtils;
import com.utils.io.StreamUtils;
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
			String resultsXmlFilePathString = args[0];
			resultsXmlFilePathString =
					PathUtils.computeNormalizedPath("results XML", resultsXmlFilePathString);
			if (resultsXmlFilePathString != null) {

				final List<XmlCountry> xmlCountryList = new ArrayList<>();
				fillXmlCountryList(resultsXmlFilePathString, xmlCountryList);

				final List<String> countryCodeList = new ArrayList<>();
				fillCountryCodeList(xmlCountryList, countryCodeList);

				String outputHtmlFilePathString = args[1];
				outputHtmlFilePathString =
						PathUtils.computeNormalizedPath("output HTML", outputHtmlFilePathString);
				if (outputHtmlFilePathString != null) {

					writeOutputHtmlFile(countryCodeList, outputHtmlFilePathString);
					Logger.printFinishMessage(start);
				}
			}
		}
	}

	private static void fillXmlCountryList(
			final String resultsXmlFilePathString,
			final List<XmlCountry> xmlCountryList) {

		try {
			Logger.printProgress("parsing input XML file:");
			Logger.printLine(resultsXmlFilePathString);

			final Document document = XmlDomUtils.openDocument(resultsXmlFilePathString);
			final Element documentElement = document.getDocumentElement();

			final List<Element> countryElementList =
					XmlDomUtils.getElementsByTagName(documentElement, "country");
			for (final Element countryElement : countryElementList) {

				final String longCountryCode = countryElement.getAttribute("code");

				final Element nameElement = XmlDomUtils.getFirstElementByTagName(countryElement, "name");
				final String countryName = nameElement.getTextContent();

				final XmlCountry xmlCountry = new XmlCountry(countryName, longCountryCode);
				xmlCountryList.add(xmlCountry);
			}

		} catch (final Exception exc) {
			Logger.printError("failed to parse input XML file");
			Logger.printException(exc);
		}
	}

	private static void fillCountryCodeList(
			final List<XmlCountry> xmlCountryList,
			final List<String> countryCodeList) {

		final CountryCodeMappingParser countryCodeMappingParser = new CountryCodeMappingParser();
		countryCodeMappingParser.work();

		final List<CountryCodeMapping> countryCodeMappingList =
				countryCodeMappingParser.getCountryCodeMappingList();

		final Map<String, String> longToShortCountryCodeMap = new HashMap<>();
		final Map<String, String> countryNameToCodeMap = new HashMap<>();
		for (final CountryCodeMapping countryCodeMapping : countryCodeMappingList) {

			final String longCountryCode = countryCodeMapping.getLongCountryCode();
			final String countryCode = countryCodeMapping.getCountryCode();
			longToShortCountryCodeMap.put(longCountryCode, countryCode);

			final String countryName = countryCodeMapping.getCountryName();
			countryNameToCodeMap.put(countryName, countryCode);
		}

		for (final XmlCountry xmlCountry : xmlCountryList) {

			final String longCountryCode = xmlCountry.getLongCountryCode();
			String countryCode = longToShortCountryCodeMap.get(longCountryCode);
			if (countryCode == null) {

				final String countryName = xmlCountry.getCountryName();
				countryCode = countryNameToCodeMap.get(countryName);
				if (countryCode == null) {

					Logger.printWarning("found no mapping for country " + countryName +
							", long country code " + longCountryCode);

				} else {
					countryCodeList.add(countryCode);
				}

			} else {
				countryCodeList.add(countryCode);
			}
		}
	}

	private static void writeOutputHtmlFile(
			final List<String> countryCodeList,
			final String outputHtmlFilePathString) {

		try {
			Logger.printProgress("writing output HTML file:");
			Logger.printLine(outputHtmlFilePathString);

			final boolean success = FactoryFolderCreator.getInstance()
					.createParentDirectories(outputHtmlFilePathString, true);
			if (success) {

				try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
						ResourceFileUtils.resourceFileToInputStream("com/personal/imo/template.html")));
						final PrintStream printStream = StreamUtils.openPrintStream(outputHtmlFilePathString)) {

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
