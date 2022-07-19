package com.utils.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import com.utils.log.Logger;

public final class IoTestUtils {

	private IoTestUtils() {
	}

	public static void checkFolderContentsEqual(
			final String folderPathString,
			final String otherFolderPathString) throws Exception {

		int contentNotEqualCount = 0;
		Logger.printNewLine();
		Logger.printProgress("comparing folder contents:");
		Logger.printLine(folderPathString);
		Logger.printLine(otherFolderPathString);

		final Path folderPath = Paths.get(folderPathString);
		final boolean folderExists = IoUtils.directoryExists(folderPath);
		Assertions.assertTrue(folderExists);

		final List<Path> filePathList = IoUtils.listFilesRecursively(folderPath);
		for (final Path filePath : filePathList) {

			if (!IoUtils.directoryExists(filePath)) {

				final Path relativePath = folderPath.relativize(filePath);
				final String relativePathString = relativePath.toString();
				final Path otherFilePath = Paths.get(otherFolderPathString, relativePathString);
				final boolean contentEquals =
						FileUtils.contentEquals(filePath.toFile(), otherFilePath.toFile());
				if (!contentEquals) {

					contentNotEqualCount++;
					Logger.printWarning("The following files are different:" +
							System.lineSeparator() + filePath +
							System.lineSeparator() + otherFilePath);
				}
			}
		}
		Assertions.assertEquals(0, contentNotEqualCount);
	}
}
