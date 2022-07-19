package com.utils.xml.stax;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.utils.log.Logger;
import com.utils.string.exc.SilentException;

public abstract class AbstractXmlStAXReader implements XmlStAXReader {

	private final BufferedInputStream bufferedInputStream;
	private final XMLEventReader xmlEventReader;

	protected AbstractXmlStAXReader(
			final InputStream inputStream) {

		BufferedInputStream bufferedInputStream = null;
		XMLEventReader xmlEventReader = null;
		try {
			bufferedInputStream = new BufferedInputStream(inputStream);

			System.setProperty(
					"javax.xml.stream.XMLInputFactory",
					"com.sun.xml.internal.stream.XMLInputFactoryImpl");
			final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
			xmlEventReader = xmlInputFactory.createXMLEventReader(bufferedInputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to create the XML reader");
			Logger.printException(exc);
		}
		this.bufferedInputStream = bufferedInputStream;
		this.xmlEventReader = xmlEventReader;
	}

	protected AbstractXmlStAXReader(
			final Path xmlFilePath) {

		BufferedInputStream bufferedInputStream = null;
		XMLEventReader xmlEventReader = null;
		try {
			bufferedInputStream = new BufferedInputStream(Files.newInputStream(xmlFilePath));

			System.setProperty("javax.xml.stream.XMLInputFactory",
					"com.sun.xml.internal.stream.XMLInputFactoryImpl");
			final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
			xmlEventReader = xmlInputFactory.createXMLEventReader(bufferedInputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to create the XML reader");
			Logger.printException(exc);
		}
		this.bufferedInputStream = bufferedInputStream;
		this.xmlEventReader = xmlEventReader;
	}

	@Override
	public final boolean readXml() {

		boolean success = false;
		if (xmlEventReader != null) {

			try {
				final Stack<String> pathInXml = new Stack<>();
				while (xmlEventReader.hasNext()) {

					final XMLEvent xmlEvent = xmlEventReader.nextEvent();
					if (xmlEvent.isStartElement()) {

						final StartElement startElement = xmlEvent.asStartElement();
						final String localPart = startElement.getName().getLocalPart();
						pathInXml.push(localPart);
						parseXmlEvent(pathInXml, startElement);

					} else if (xmlEvent.isEndElement()) {
						parseXmlEvent(pathInXml, xmlEvent);
						pathInXml.pop();

					} else {
						parseXmlEvent(pathInXml, xmlEvent);
					}
				}
				success = true;

			} catch (final SilentException ignored) {
			} catch (final Exception exc) {
				Logger.printError("failed to read XML file");
				Logger.printException(exc);

			} finally {
				closeStreams();
			}
		}
		return success;
	}

	protected abstract void parseXmlEvent(
			Stack<String> pathInXml,
			XMLEvent xmlEvent) throws SilentException;

	private void closeStreams() {

		try {
			xmlEventReader.close();
			bufferedInputStream.close();
		} catch (final Exception ignored) {
		}
	}
}
