package com.app.factories;

import com.app.cooks.interfaces.FullFunctionalCook;
import org.apache.commons.lang3.mutable.MutableInt;

public final class CookThreadFactory {

    private CookThreadFactory() {
        throw new AssertionError("No instance for you");
    }

    public static Thread makeThread(FullFunctionalCook cook, long timeout, MutableInt cookedBefore, final Object lock) {
        return new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    //Если блюд больше 10 -> ждем;
                    if (cookedBefore.intValue() >= 10) {
                        try {
                            lock.wait();
                            //TODO: надо ли тут lock.notify()?
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        cook.cook(cookedBefore);
                        lock.notify();
                    }
                }
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Thread makeThreadZeroStartingDishes(FullFunctionalCook cook, long timeout, final Object lock) {
        return makeThread(cook, timeout, new MutableInt(0), lock);
    }
}
