package com.personal.imo;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class AppStartImoWorldMapGeneratorTest {

	@Test
	void testMain() {

		final String resultsXmlFilePathString;
		final String outputHtmlFilePathString;
		final int input = Integer.parseInt("2");
		if (input == 1) {
			resultsXmlFilePathString = Paths.get("inputs", "IMO_Team_2022.xml").toString();
			outputHtmlFilePathString = Paths.get("outputs", "IMO_Team_2022_WorldMap.html").toString();
		} else if (input == 2) {
			resultsXmlFilePathString = Paths.get("inputs", "IMO_Team_2021.xml").toString();
			outputHtmlFilePathString = Paths.get("outputs", "IMO_Team_2021_WorldMap.html").toString();
		} else {
			throw new RuntimeException();
		}

		final String[] args = new String[] {
				resultsXmlFilePathString,
				outputHtmlFilePathString
		};
		AppStartImoWorldMapGenerator.main(args);
	}
}
