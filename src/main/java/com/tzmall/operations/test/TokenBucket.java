package com.tzmall.operations.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucket {
    private final int capacity; // 令牌桶容量
    private final long refillInterval; // 填充令牌的时间间隔
    private final AtomicInteger tokens; // 当前令牌数量
    private final Lock lock;
    private final Condition tokensAvailable;
    private final ScheduledExecutorService executorService;

    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillInterval = TimeUnit.SECONDS.toNanos(1) / refillRate;
        this.tokens = new AtomicInteger(capacity); // 初始化时，令牌桶是满的
        this.lock = new ReentrantLock();
        this.tokensAvailable = lock.newCondition();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        startRefillThread();
    }

    /**
     * 消费token
     */
    public boolean tryConsume() {
        lock.lock();
        try {
            while (tokens.get() == 0) {
                tokensAvailable.await();
            }
            return tokens.decrementAndGet() > 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * 填充token
     */
    private void refillTokens() {
        int currentTokens = tokens.get();
        int newTokens = Math.min(currentTokens + 1, capacity);
        tokens.compareAndSet(currentTokens, newTokens);
        if (newTokens > 0) {
            lock.lock();
            try {
                tokensAvailable.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 启动填充线程
     */
    private void startRefillThread() {
        executorService.scheduleAtFixedRate(this::refillTokens, refillInterval, refillInterval, TimeUnit.NANOSECONDS);
    }

    public static void main(String[] args) {
        int capacity = 10; // 设置令牌桶容量
        int refillRate = 2; // 设置填充速率

        TokenBucket tokenBucket = new TokenBucket(capacity, refillRate);

        // 模拟多个线程同时尝试获取令牌
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 1000)); // 随机等待一段时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (tokenBucket.tryConsume()) {
                    System.out.println(Thread.currentThread().getName() + ": 获取到令牌");
                } else {
                    System.out.println(Thread.currentThread().getName() + ": 未获取到令牌");
                }
            }).start();
        }
    }
}