package com.utils.io.folder_creators;

import com.utils.annotations.ApiMethod;

public final class FactoryFolderCreator {

	private FactoryFolderCreator() {
	}

	@ApiMethod
	public static FolderCreator getInstance() {
		return FolderCreatorImpl.INSTANCE;
	}
}
