package com.utils.io.zip;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ZipFileCreatorTest {

	@Test
	void testWork() {

		final String srcFolderPathString = "D:\\IVI_MISC\\Misc\\nf\\tmp\\abcd";
		final String zipArchiveFilePathString = "D:\\IVI_MISC\\Misc\\nf\\tmp\\abcd.zip";
		final boolean useTempFile = true;
		final boolean deleteExisting = true;
		final int threadCount = 12;
		final boolean updateFileTimes = true;
		final boolean verbose = true;

		final Path srcFolderPath = Paths.get(srcFolderPathString);
		final Path zipArchiveFilePath = Paths.get(zipArchiveFilePathString);
		final ZipFileCreator zipFileCreator = new ZipFileCreator(srcFolderPath, zipArchiveFilePath,
				useTempFile, deleteExisting, threadCount, updateFileTimes, verbose);
		zipFileCreator.work();
		final boolean success = zipFileCreator.isSuccess();
		Assertions.assertTrue(success);
	}
}
