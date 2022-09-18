package com.utils.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.utils.log.Logger;

public final class IoTestUtils {

	private IoTestUtils() {
	}

	public static void checkFolderContentsEqual(
			final String folderPathString,
			final String otherFolderPathString,
			final List<FileCompareData> fileCompareDataList) throws Exception {

		Logger.printNewLine();
		Logger.printProgress("comparing folder contents:");
		Logger.printLine(folderPathString);
		Logger.printLine(otherFolderPathString);

		final Path folderPath = Paths.get(folderPathString);
		final List<Path> filePathList;
		final boolean folderExists = IoUtils.directoryExists(folderPath);
		if (folderExists) {
			filePathList = IoUtils.listFilesRecursively(folderPath);
		} else {
			filePathList = new ArrayList<>();
		}

		final Set<String> matchedOtherFilePathStringSet = new HashSet<>();
		for (final Path filePath : filePathList) {

			if (!IoUtils.directoryExists(filePath)) {

				final Path relativePath = folderPath.relativize(filePath);
				final String relativePathString = relativePath.toString();
				final Path otherFilePath = Paths.get(otherFolderPathString, relativePathString);
				final String otherFilePathString = otherFilePath.toString();
				matchedOtherFilePathStringSet.add(otherFilePathString);

				final boolean contentEquals =
						FileUtils.contentEquals(filePath.toFile(), otherFilePath.toFile());
				if (!contentEquals) {
					Logger.printWarning("The following files are different:" +
							System.lineSeparator() + filePath +
							System.lineSeparator() + otherFilePath);
				}

				final String filePathString = filePath.toString();

				final FileCompareData fileCompareData = new FileCompareData(
						filePathString, otherFilePathString, contentEquals);
				fileCompareDataList.add(fileCompareData);
			}
		}

		final Path otherFolderPath = Paths.get(otherFolderPathString);
		final List<Path> otherFilePathList;
		final boolean otherFolderExists = IoUtils.directoryExists(otherFolderPath);
		if (otherFolderExists) {
			otherFilePathList = IoUtils.listFilesRecursively(otherFolderPath);
		} else {
			otherFilePathList = new ArrayList<>();
		}

		for (final Path otherFilePath : otherFilePathList) {

			if (!IoUtils.directoryExists(otherFilePath)) {

				final String otherFilePathString = otherFilePath.toString();
				if (!matchedOtherFilePathStringSet.contains(otherFilePathString)) {

					final Path relativePath = otherFolderPath.relativize(otherFilePath);
					final String relativePathString = relativePath.toString();
					final Path filePath = Paths.get(folderPathString, relativePathString);
					final String filePathString = filePath.toString();

					final boolean contentEquals =
							FileUtils.contentEquals(filePath.toFile(), otherFilePath.toFile());
					if (!contentEquals) {
						Logger.printWarning("The following files are different:" +
								System.lineSeparator() + filePath +
								System.lineSeparator() + otherFilePath);
					}

					final FileCompareData fileCompareData = new FileCompareData(
							filePathString, otherFilePathString, contentEquals);
					fileCompareDataList.add(fileCompareData);
				}
			}
		}
	}
}
