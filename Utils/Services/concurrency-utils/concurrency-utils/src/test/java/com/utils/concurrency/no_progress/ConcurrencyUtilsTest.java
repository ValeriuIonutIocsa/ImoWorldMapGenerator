package com.utils.concurrency.no_progress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.string.StrUtils;

class ConcurrencyUtilsTest {

	private static final Random RANDOM = new Random();

	@Test
	void testExecuteMultiThreadedTaskRegular() {

		final ConcurrencyUtils concurrencyUtils = new ConcurrencyUtilsRegular(16);
		testExecuteMultiThreadedTaskCommon(concurrencyUtils);
	}

	@Test
	void testExecuteMultiThreadedTaskTimed() {

		final ConcurrencyUtils concurrencyUtils = new ConcurrencyUtilsTimed(16, 500);
		testExecuteMultiThreadedTaskCommon(concurrencyUtils);
	}

	private static void testExecuteMultiThreadedTaskCommon(
			final ConcurrencyUtils concurrencyUtils) {

		final List<Runnable> runnableList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {

			final int index = i;
			runnableList.add(() -> {
				final long startTime = System.currentTimeMillis();
				try {
					final int runTime = (int) (RANDOM.nextDouble() * 1000);
					Logger.printLine("run time for task " + index + ": " + StrUtils.timeMsToString(runTime));

					Thread.sleep(runTime);

					final long executionTime = System.currentTimeMillis() - startTime;
					Logger.printLine("task " + index + " done in " + StrUtils.timeMsToString(executionTime));

				} catch (final InterruptedException e) {
					final long executionTime = System.currentTimeMillis() - startTime;
					Logger.printLine("task " + index + " aborted after " + StrUtils.timeMsToString(executionTime));
				}
			});
		}

		Logger.printProgress("running concurrency utils test");
		concurrencyUtils.executeMultiThreadedTask(runnableList);
	}
}
