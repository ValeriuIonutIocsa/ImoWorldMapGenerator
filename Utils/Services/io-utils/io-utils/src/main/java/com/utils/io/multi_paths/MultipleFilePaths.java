package com.utils.io.multi_paths;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.utils.io.IoUtils;
import com.utils.string.StrUtils;

public class MultipleFilePaths {

	private final List<Path> pathList;

	public MultipleFilePaths() {

		pathList = new ArrayList<>();
	}

	public void addPath(
			final Path path) {
		pathList.add(path);
	}

	public Path computeFirstPathThatExists() {

		Path firstPathThatExists = null;
		for (final Path path : pathList) {

			if (IoUtils.fileExists(path)) {
				firstPathThatExists = path;
				break;
			}
		}
		return firstPathThatExists;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
