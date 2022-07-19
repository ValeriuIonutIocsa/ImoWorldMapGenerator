package com.utils.io.folder_deleters;

import com.utils.annotations.ApiMethod;

public final class FactoryFolderDeleter {

	private FactoryFolderDeleter() {
	}

	@ApiMethod
	public static FolderDeleter getInstance() {
		return FolderDeleterImpl.INSTANCE;
	}
}
