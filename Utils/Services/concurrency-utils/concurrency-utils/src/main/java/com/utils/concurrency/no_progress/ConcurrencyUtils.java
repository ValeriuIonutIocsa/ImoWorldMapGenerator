package com.utils.concurrency.no_progress;

import java.util.List;

public interface ConcurrencyUtils {

	void executeMultiThreadedTask(
			List<Runnable> runnableList);
}
