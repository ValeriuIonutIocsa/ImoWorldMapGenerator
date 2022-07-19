package com.utils.io;

import java.nio.file.Path;

import com.utils.annotations.ApiMethod;
import com.utils.log.Logger;

public class CachedFile<
		ObjectT> {

	private Path filePath;
	private long size;
	private long lastModifiedTime;
	private ObjectT dataObject;

	public CachedFile() {

		size = -1;
		lastModifiedTime = -1;
	}

	@ApiMethod
	public void cache(
			final Path filePath,
			final ObjectT dataObject) {

		try {
			this.filePath = filePath;
			size = IoUtils.fileSize(filePath);
			lastModifiedTime = IoUtils.fileLastModifiedTime(filePath);
			this.dataObject = dataObject;

		} catch (final Exception exc) {
			Logger.printError("failed to cache file:" + System.lineSeparator() + filePath);
			Logger.printException(exc);
		}
	}

	@ApiMethod
	public boolean isCached(
			final Path filePath) {

		boolean cached = false;
		try {
			final boolean parseFile;
			if (filePath == null) {
				parseFile = this.filePath == null;
			} else {
				parseFile = filePath.equals(this.filePath);
			}
			if (parseFile) {

				final long size = IoUtils.fileSize(filePath);
				if (this.size == size) {

					final long lastModifiedTime = IoUtils.fileLastModifiedTime(filePath);
					cached = this.lastModifiedTime == lastModifiedTime;
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to check if file is cached:" +
					System.lineSeparator() + filePath);
			Logger.printException(exc);
		}
		return cached;
	}

	@ApiMethod
	public ObjectT getDataObject() {
		return dataObject;
	}
}
