package com.jpkhawam.nabu.util;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple counter implementation of {@link IdlingResource} that determines idleness by
 * maintaining an internal counter. When the counter is 0 - it is considered to be idle, when it is
 * non-zero it is not idle. This is very similar to the way a {@link java.util.concurrent.Semaphore}
 * behaves.
 * <p>
 * This class can then be used to wrap up operations that while in progress should block tests from
 * accessing the UI.
 */
public final class SimpleCountingIdlingResource implements IdlingResource {

    private final String resourceName;
    private final AtomicInteger counter = new AtomicInteger(0);
    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    /**
     * Creates a SimpleCountingIdlingResource
     *
     * @param resource the resource name this resource should report to Espresso.
     */
    public SimpleCountingIdlingResource(String resource) {
        this.resourceName = resource;
    }

    @Override
    public String getName() {return resourceName;}

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    /**
     * Increments the count of in-flight transactions to the resource being monitored.
     */
    public void increment() {
        counter.getAndIncrement();
    }

    /**
     * Decrements the count of in-flight transactions to the resource being monitored.
     *
     * If this operation results in the counter falling below 0 - an exception is raised.
     *
     * @throws IllegalStateException if the counter is below 0.
     */
    public void decrement() {
        int counterValue = counter.decrementAndGet();
        if (counterValue == 0) {
            // we've gone from non-zero to zero. That means we're idle now! Tell espresso.
            if (resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
        }

        if (counterValue < 0) {
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
    }
}
