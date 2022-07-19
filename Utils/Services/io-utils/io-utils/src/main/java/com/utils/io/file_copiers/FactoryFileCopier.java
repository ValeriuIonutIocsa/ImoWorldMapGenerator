package com.utils.io.file_copiers;

import com.utils.annotations.ApiMethod;

public final class FactoryFileCopier {

	private FactoryFileCopier() {
	}

	@ApiMethod
	public static FileCopier getInstance() {
		return FileCopierImpl.INSTANCE;
	}
}
