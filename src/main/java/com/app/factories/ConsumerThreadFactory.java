package com.app.factories;

import com.app.DataUtil;
import com.app.consumers.Consumer;
import com.app.cooks.interfaces.FullFunctionalCook;
import org.apache.commons.lang3.mutable.MutableInt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ConsumerThreadFactory {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private ConsumerThreadFactory() {
        throw new AssertionError("No instance for you");
    }

    private static void assertListsHasTheSameSize(List<FullFunctionalCook> orderOfCooks, List<Long> timeouts,
                                                  List<Object> locks) {
        if (orderOfCooks.size() == timeouts.size() && orderOfCooks.size() == locks.size()) {
            return;
        }
        throw new AssertionError("List of cooks and timeouts should have the same size as list of locks");
    }

    public static Thread threadForSingleConsumer(List<FullFunctionalCook> orderOfCooks,
                                                 List<Long> timeouts,
                                                 List<MutableInt> cookedBefore,
                                                 Consumer consumer,
                                                 List<Object> locks,
                                                 int j) {
        assertListsHasTheSameSize(orderOfCooks, timeouts, locks);

        return new Thread(() -> {
            for (int i = 0; i < orderOfCooks.size(); i++) {
                Date date = new Date();

                System.out.println("Consumer " + j + " started to work" + " " + DataUtil.getCurrentLocalDateTimeStamp());
                synchronized (locks.get(i)) {
                    //Если блюд больше 1 -> забрать блюдо
                    while (cookedBefore.get(i).intValue() <= 0) {
                        try {
                            locks.get(i).notify();
                            locks.get(i).wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    consumer.consume("Number of consumer: " + j + " " + orderOfCooks.get(i).getMessage());
                    cookedBefore.get(i).decrement();
                }
                try {
                    Thread.sleep(timeouts.get(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
/*
    public static Thread threadForSingleConsumerZeroStartingDishes(List<FullFunctionalCook> orderOfCooks,
                                                                   List<Long> timeouts,
                                                                   Consumer consumer,
                                                                   List<Object> locks,
                                                                   int j) {
        List<MutableInt> zeroStartingDishes = new ArrayList<>();
        for (int i = 0; i < orderOfCooks.size(); i++) {
            zeroStartingDishes.add(new MutableInt(0));
        }
        return threadForSingleConsumer(orderOfCooks, timeouts, zeroStartingDishes, consumer, locks, j);
    }

    public static List<Thread> listOfThreadsForMultipleSameConsumers(List<FullFunctionalCook> orderOfCooks,
                                                                     List<Long> timeouts,
                                                                     List<MutableInt> cookedBefore,
                                                                     Consumer objectConsumerType,
                                                                     int amountOfConsumers,
                                                                     List<Object> locks) {
        assertListsHasTheSameSize(orderOfCooks, timeouts, locks);

        List<Thread> threads = new ArrayList<>(amountOfConsumers);
        for (int i = 0; i < amountOfConsumers; i++) {
            threads.add(threadForSingleConsumer(orderOfCooks, timeouts, cookedBefore, objectConsumerType, locks));
        }
        return threads;
    }

    public static List<Thread> listOfThreadsForMultipleSameConsumersZeroStartingDishes(final List<FullFunctionalCook> orderOfCooks,
                                                                                       List<Long> timeouts,
                                                                                       Consumer objectConsumerType,
                                                                                       int amountOfConsumers,
                                                                                       List<Object> locks) {
        assertListsHasTheSameSize(orderOfCooks, timeouts, locks);

        List<Thread> threads = new ArrayList<>(amountOfConsumers);
        for (int i = 0; i < amountOfConsumers; i++) {
            threads.add(threadForSingleConsumerZeroStartingDishes(orderOfCooks, timeouts, objectConsumerType, locks));
        }
        return threads;
    }*/
}
