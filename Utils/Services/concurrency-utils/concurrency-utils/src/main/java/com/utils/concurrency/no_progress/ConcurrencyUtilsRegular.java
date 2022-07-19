package com.utils.concurrency.no_progress;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.utils.concurrency.no_progress.data.CustomCallable;
import com.utils.concurrency.no_progress.data.CustomCallableRegular;
import com.utils.log.Logger;

public class ConcurrencyUtilsRegular extends AbstractConcurrencyUtils {

	public ConcurrencyUtilsRegular(
			final int threadCount) {
		super(threadCount);
	}

	@Override
	void printInitMessages() {
		Logger.printLine("(number of threads: " + threadCount + ")");
	}

	@Override
	void submitCallable(
			final Runnable runnable,
			final ExecutorService executorService,
			final List<Future<?>> futureList) {

		final CustomCallable customCallable = new CustomCallableRegular(runnable);
		final Future<Void> future = executorService.submit(customCallable);
		futureList.add(future);
	}

	@Override
	void futureGet(
			final Future<?> future) {

		try {
			future.get();
		} catch (final Exception ignored) {
		}
	}
}
