package com.utils.io.ro_flag_clearers;

import com.utils.annotations.ApiMethod;

public final class FactoryReadOnlyFlagClearer {

	private FactoryReadOnlyFlagClearer() {
	}

	@ApiMethod
	public static ReadOnlyFlagClearer getInstance() {
		return ReadOnlyFlagClearerImpl.INSTANCE;
	}
}
