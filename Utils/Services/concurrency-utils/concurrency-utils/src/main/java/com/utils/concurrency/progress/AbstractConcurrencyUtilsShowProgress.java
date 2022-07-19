package com.utils.concurrency.progress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.utils.log.progress.ProgressIndicators;
import com.utils.string.StrUtils;

abstract class AbstractConcurrencyUtilsShowProgress implements ConcurrencyUtilsShowProgress {

	final int threadCount;
	private final int showProgressInterval;

	AbstractConcurrencyUtilsShowProgress(
			final int threadCount,
			final int showProgressInterval) {

		this.threadCount = threadCount;
		this.showProgressInterval = showProgressInterval;
	}

	@Override
	public void executeMultiThreadedTask(
			final List<Runnable> runnableList) {

		if (!runnableList.isEmpty()) {

			printInitMessages();

			final ExecutorService executorService;
			if (threadCount <= 0) {
				executorService = Executors.newCachedThreadPool();
			} else {
				executorService = Executors.newFixedThreadPool(threadCount);
			}
			ProgressIndicators.getInstance().update(0);

			final List<Future<?>> futureList = new ArrayList<>();
			final AtomicInteger completedRunnablesCount = new AtomicInteger(0);
			final int runnableCount = runnableList.size();
			for (final Runnable runnable : runnableList) {
				submitCallable(runnable, executorService, futureList,
						completedRunnablesCount, runnableCount, showProgressInterval);
			}

			for (final Future<?> future : futureList) {
				futureGet(future);
			}

			executorService.shutdown();
			ProgressIndicators.getInstance().update(0);
		}
	}

	abstract void printInitMessages();

	abstract void submitCallable(
			Runnable runnable,
			ExecutorService executorService,
			List<Future<?>> futureList,
			AtomicInteger completedRunnablesCount,
			int runnableCount,
			int showProgressInterval);

	abstract void futureGet(
			Future<?> future);

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
