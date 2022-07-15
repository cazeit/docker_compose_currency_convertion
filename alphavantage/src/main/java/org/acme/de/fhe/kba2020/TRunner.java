/*
 * TRunner
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020;

import java.util.Random;

/**
 * This Class is used to give us the opportunity to block API-Calls to simulate a bad connection.
 * We use int values which represents the time in ms. By default is the sleeptime set to 1000 ms --> 1 s.
 * The Class accepting only a min. sleep time from 1 ms and a max. sleep time from 5000 ms if you want to change this
 * you have to alter the mMAX_SLEEP_TIME and mMIN_SLEEP_TIME with your value, be carefully with that.
 * <p>
 * If we run TRunner(EDebugParameters.Random) the idle time will randomly set between mMAX_SLEEP_TIME and mMIN_SLEEP_TIME.
 * If we run TRunner(EDebugParameters.FixedTimeOut) the idle time will use the default value of 1000ms set in mDefaultSleepTime.
 * If we run TRunner(EDebugParameters.NoResponse) the idle time will use the mMAX_SLEEP_TIME value multiplied by 100 to simulate no response.
 *
 * @author Sebastian Golchert
 * @version 1.0
 */
public class TRunner {
    /**
     * The maximum idle sleep time
     */
    private final int mMAX_SLEEP_TIME = 10000;
    /**
     * The minimum idle sleep time
     */
    private final int mMIN_SLEEP_TIME = 1;
    private int mDefaultSleepTime = 5000;

    private Boolean isSleeping;
    private static TRunner sharedInstance;

    /**
     * Empty Ctor
     */
    public TRunner() {
        this.isSleeping = false;
    }

    /**
     * Ctor with one parameter to set a new default sleep time.
     * The new default slepptime is only set if the new time is between mMIN_SLEEP_TIME and mMAX_SLEEP_TIME.
     *
     * @param sleepTime the new default sleep time
     */
    public TRunner(int sleepTime) {
        setDefaultSleepTime(sleepTime);
    }

    /**
     * This method does the sleep functionality for us by calling Thread.sleep(milliseconds) function on actual Thread.
     * If a exception occurs thy will catched, we also get informed in console when the TRunner sleeps and whe he woke up.
     *
     * @param milliseconds the time which the sleepFor function set the actual Thread to sleep.
     */
    private void sleepFor(int milliseconds) {
        try {
            System.out.println("Sleeping for: " + milliseconds + " ms.");
            isSleeping = true;
            Thread.sleep(milliseconds);
            isSleeping = false;
            System.out.println("Wake Up.");
        } catch (InterruptedException e) {
        }
    }

    /**
     * This Method is used to generate a random sleep time between mMIN_SLEEP_TIME and mMAX_SLEEP_TIME
     *
     * @return
     */
    int getRandTime() {
        Random rand = new Random();
        int randTime;
        randTime = rand.nextInt(((getMaxSleepTime() - getMinSleepTime()) + 1) + getMinSleepTime());
        return randTime;
    }

    /**
     * This method is used to simulate the different behaviours on the TRunner, by using the EDebugParameters Enum to
     * decide how long the TRunner has to sleep.
     *
     * @param debugParam The EDebugParameters Enum to specify the behaviour
     */
    public void run(EDebugParameters debugParam) {
        if (debugParam == EDebugParameters.fixed) {
            sleepFor(getDefaultSleepTime());
        } else if (debugParam == EDebugParameters.random) {
            sleepFor(getRandTime());
        } else if (debugParam == EDebugParameters.noresponse) {
            sleepFor(getMaxSleepTime() * 10);
        } else {
            sleepFor(getRandTime());
        }
    }

    // Only some setters and getters
    public int getMaxSleepTime() {
        return this.mMAX_SLEEP_TIME;
    }

    public int getMinSleepTime() {
        return this.mMIN_SLEEP_TIME;
    }

    public void setDefaultSleepTime(int newSleepTime) {
        if (newSleepTime < this.mMAX_SLEEP_TIME && newSleepTime > mMIN_SLEEP_TIME) {
            this.mDefaultSleepTime = newSleepTime;
        }
    }

    public int getDefaultSleepTime() {
        return this.mDefaultSleepTime;
    }

    public Boolean getSleeping() {
        return isSleeping;
    }

    public static org.acme.de.fhe.kba2020.TRunner getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new TRunner();
        }
        return sharedInstance;
    }
}
