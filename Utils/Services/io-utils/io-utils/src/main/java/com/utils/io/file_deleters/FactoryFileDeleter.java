package com.utils.io.file_deleters;

import com.utils.annotations.ApiMethod;

public final class FactoryFileDeleter {

	private FactoryFileDeleter() {
	}

	@ApiMethod
	public static FileDeleter getInstance() {
		return FileDeleterImpl.INSTANCE;
	}
}
