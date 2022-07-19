package com.utils.xml.stax;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.jupiter.api.Test;

import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;

class XmlStAXWriterTest {

	@Test
	void testWrite() throws Exception {

		final Path tempXmlFilePath =
				Paths.get(PathUtils.ROOT_PATH, "tmp", "xml_stax_writer_test.xml");
		new AbstractXmlStAXWriter(tempXmlFilePath, "    ") {

			@Override
			protected void write() {

				writeStartDocument();

				writeStartElement("test1");

				writeStartElement("test2");
				writeAttribute("attribute", "a>b<c");

				writeStartElement("test3");
				writeAttribute("before_attribute", "before");
				writeAttribute("after_attribute", "after");

				writeEndElement("test3");

				writeStartElement("test4");

				writeEndElement("test4");

				writeEndElement("test2");

				writeEndElement("test1");

				writeEndDocument();
			}

		}.writeXml();

		final String fileToString = IoUtils.fileToString(tempXmlFilePath);
		Logger.printLine(fileToString);

		new AbstractXmlStAXReader(tempXmlFilePath) {

			@Override
			protected void parseXmlEvent(
					final Stack<String> pathInXml,
					final XMLEvent xmlEvent) {

				if (xmlEvent.isStartElement()) {

					final String lastElement = pathInXml.lastElement();
					if ("test2".equals(lastElement)) {

						final StartElement startElement = xmlEvent.asStartElement();
						final String attributeValue =
								XmlStAXReader.getAttribute(startElement, "attribute");
						Logger.printLine("attribute value: " + attributeValue);
					}
				}
			}
		}.readXml();

		FactoryFileDeleter.getInstance().deleteFile(tempXmlFilePath, true);
	}
}
