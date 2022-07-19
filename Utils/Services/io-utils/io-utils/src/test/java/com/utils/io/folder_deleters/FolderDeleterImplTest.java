package com.utils.io.folder_deleters;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.io.IoUtils;

class FolderDeleterImplTest {

	@Test
	void testDeleteFolder() {

		final Path folderPath = Paths.get("");
		Assertions.assertTrue(IoUtils.directoryExists(folderPath));
		FolderDeleterImpl.INSTANCE.deleteFolder(folderPath, true);
		Assertions.assertFalse(IoUtils.directoryExists(folderPath));
	}
}
