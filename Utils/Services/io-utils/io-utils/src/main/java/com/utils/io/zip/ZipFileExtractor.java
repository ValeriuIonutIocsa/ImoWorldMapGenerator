package com.utils.io.zip;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.utils.concurrency.no_progress.ConcurrencyUtilsSimpleRegular;
import com.utils.io.IoUtils;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.folder_deleters.FactoryFolderDeleter;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class ZipFileExtractor {

	private final Path zipArchiveFilePath;
	private final Path dstFolderPath;
	private final boolean useTempFile;
	private final boolean deleteExisting;
	private final int threadCount;
	private final boolean updateFileTimes;
	private final boolean verbose;

	private boolean success;

	public ZipFileExtractor(
			final Path zipArchiveFilePath,
			final Path dstFolderPath,
			final boolean useTempFile,
			final boolean deleteExisting,
			final int threadCount,
			final boolean updateFileTimes,
			final boolean verbose) {

		this.zipArchiveFilePath = zipArchiveFilePath;
		this.dstFolderPath = dstFolderPath;
		this.useTempFile = useTempFile;
		this.deleteExisting = deleteExisting;
		this.threadCount = threadCount;
		this.updateFileTimes = updateFileTimes;
		this.verbose = verbose;
	}

	public void work() {

		success = false;

		Logger.printProgress("extracting ZIP archive:");
		Logger.printLine(zipArchiveFilePath);

		if (!IoUtils.fileExists(zipArchiveFilePath)) {
			Logger.printWarning("the ZIP archive does not exist:" +
					System.lineSeparator() + zipArchiveFilePath);

		} else {
			if (deleteExisting) {
				FactoryFolderDeleter.getInstance().cleanFolder(dstFolderPath, true);
			}

			FactoryFolderCreator.getInstance().createDirectories(dstFolderPath, true);

			try (FileSystem zipFileSystem = ZipUtils.openZipFileSystem(zipArchiveFilePath, useTempFile)) {

				final List<Runnable> runnableList = new ArrayList<>();
				final Path root = zipFileSystem.getPath("/");
				Files.walkFileTree(root, new SimpleFileVisitor<>() {

					@Override
					public FileVisitResult visitFile(
							final Path filePath,
							final BasicFileAttributes attrs) throws IOException {

						runnableList.add(() -> {

							try {
								if (verbose) {
									Logger.printLine("extracting file: " + filePath);
								}

								final Path dstFilePath = Paths.get(dstFolderPath.toString(), filePath.toString());
								final Path dstFileParentFolderPath = dstFilePath.getParent();
								if (Files.notExists(dstFileParentFolderPath)) {
									synchronized (this) {
										Files.createDirectories(dstFileParentFolderPath);
									}
								}
								FactoryFileCopier.getInstance().copyFile(filePath, dstFilePath, true, true);
								if (updateFileTimes) {
									Files.setLastModifiedTime(dstFilePath, FileTime.from(Instant.now()));
								}

							} catch (final Exception exc) {
								Logger.printError("failed to extract file:" + System.lineSeparator() + filePath);
								Logger.printException(exc);
							}
						});

						return super.visitFile(filePath, attrs);
					}
				});

				if (!runnableList.isEmpty()) {
					new ConcurrencyUtilsSimpleRegular(threadCount).executeMultiThreadedTask(runnableList);
				}
				Logger.printStatus("Finished extracting ZIP archive.");
				success = true;

			} catch (final Exception exc) {
				Logger.printError("failed to extract ZIP archive:" +
						System.lineSeparator() + zipArchiveFilePath);
				Logger.printException(exc);
			}
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public boolean isSuccess() {
		return success;
	}
}
