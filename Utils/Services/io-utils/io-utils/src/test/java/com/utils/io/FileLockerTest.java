package com.utils.io;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class FileLockerTest {

	private static final Path LOCK_FILE_PATH =
			Paths.get(PathUtils.ROOT_PATH, "tmp", "FileLockerTest", "lock_file.txt");

	@Test
	void testLock() throws Exception {

		final FileLocker fileLocker = new FileLocker("JUnit test", LOCK_FILE_PATH);
		final boolean success = fileLocker.lock();
		Logger.printLine(success);

		Thread.sleep(10_000);
	}

	@Test
	void testLockSequence() {

		final FileLocker fileLocker = new FileLocker("JUnit test", LOCK_FILE_PATH);

		final boolean firstTimeSuccess = fileLocker.lock();
		Assertions.assertTrue(firstTimeSuccess);

		final boolean secondTimeSuccess = fileLocker.lock();
		Assertions.assertFalse(secondTimeSuccess);

		fileLocker.unlock();

		final boolean thirdTimeSuccess = fileLocker.lock();
		Assertions.assertTrue(thirdTimeSuccess);

		fileLocker.unlock();
	}
}
