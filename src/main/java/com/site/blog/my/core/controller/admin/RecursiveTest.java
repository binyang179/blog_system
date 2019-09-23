package com.site.blog.my.core.controller.admin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * @Author crab
 * @Date 9/21/19 11:08 AM
 */

public class RecursiveTest {
    // 定义最小区间为10
    private final static int MAX_THRESHOLD = 10;

    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> future = forkJoinPool.submit(new CalculateRecursiveTask(1, 100));
        try {
            Integer result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class CalculateRecursiveTask extends RecursiveTask<Integer> {
        // 起始
        private int start;
        // 结束
        private int end;

        public CalculateRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            // 如果起始和结束范围小于我们定义的区间范围，则直接计算
            if ((end - start) <= MAX_THRESHOLD) {
                return IntStream.rangeClosed(start, end).sum();
            } else {
                // 否则，将范围一分为二，分成两个子任务
                int middle = (start + end) / 2;
                CalculateRecursiveTask leftTask = new CalculateRecursiveTask(start, middle);
                CalculateRecursiveTask rightTask = new CalculateRecursiveTask(middle + 1, end);
                // 执行子任务
                leftTask.fork();
                rightTask.fork();

                // 汇总子任务
                return leftTask.join() + rightTask.join();
            }
        }
    }
}
