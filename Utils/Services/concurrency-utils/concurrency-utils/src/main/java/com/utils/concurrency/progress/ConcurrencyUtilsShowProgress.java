package com.utils.concurrency.progress;

import java.util.List;

public interface ConcurrencyUtilsShowProgress {

	void executeMultiThreadedTask(
			List<Runnable> runnableList);
}
