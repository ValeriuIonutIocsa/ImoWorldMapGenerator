package com.utils.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.utils.annotations.ApiMethod;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class FileLocker {

	private final String lockerName;
	private final Path lockFilePath;

	private RandomAccessFile randomAccessFile;
	private FileChannel fileChannel;
	private FileLock fileLock;

	public FileLocker(
			final String lockerName,
			final Path lockFilePath) {

		this.lockerName = lockerName;
		this.lockFilePath = lockFilePath;
	}

	@ApiMethod
	public boolean lock() {

		boolean success = true;
		try {
			Logger.printProgress("acquiring " + lockerName + " file lock");

			if (IoUtils.fileExists(lockFilePath)) {
				try {
					final String text;
					if (lockerName != null) {
						text = "locked by " + lockerName;
					} else {
						text = "locked";
					}
					IoUtils.writeStringToFile(lockFilePath, text, StandardCharsets.UTF_8);

				} catch (final Exception ignored) {
					success = false;
				}
				if (success) {
					lockExistingFile();
				}

			} else {
				FactoryFolderCreator.getInstance().createParentDirectories(lockFilePath, true);
				FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(lockFilePath, true);
				Files.createFile(lockFilePath);
				lockExistingFile();
			}

		} catch (final Exception exc) {
			Logger.printError("failed to acquire the " + lockerName + " file lock");
			Logger.printException(exc);
		}
		return success;
	}

	private void lockExistingFile() throws IOException {

		final File lockFile = lockFilePath.toFile();
		randomAccessFile = new RandomAccessFile(lockFile, "rw");
		fileChannel = randomAccessFile.getChannel();
		fileLock = fileChannel.lock();
	}

	@ApiMethod
	public void unlock() {

		try {
			Logger.printProgress("releasing the " + lockerName + " file lock");

			if (fileLock != null) {

				fileLock.release();
				if (fileChannel == null) {
					Logger.printError("file channel is null!");

				} else {
					fileChannel.close();
					if (randomAccessFile == null) {
						Logger.printError("random access file is null!");

					} else {
						randomAccessFile.close();
						FactoryFileDeleter.getInstance().deleteFile(lockFilePath, true);
					}
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to release the " + lockerName + " file lock");
			Logger.printException(exc);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
