package com;

import com.app.consumers.impl.DishesConsumer;
import com.app.cooks.impl.BorschtCook;
import com.app.cooks.impl.CakeCook;
import com.app.cooks.impl.TeaCook;
import com.app.cooks.interfaces.FullFunctionalCook;
import com.app.factories.ConsumerThreadFactory;
import com.app.factories.CookThreadFactory;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppRunner {

    private static MutableInt borschtCookBefore = new MutableInt(0);
    private static MutableInt cakeCookBefore = new MutableInt(0);
    private static MutableInt teaCookBefore = new MutableInt(0);

    private static final Object borschtLock = new Object();
    private static final Object cakeLock = new Object();
    private static final Object teaLock = new Object();
    private static final List<Object> locks = List.of(borschtLock, cakeLock, teaLock);

    private static final int amountOfConsumers = 30;

    public static void main(String[] args) {
 FullFunctionalCook borschtCook = new BorschtCook();
        FullFunctionalCook cakeCook = new CakeCook();
        FullFunctionalCook teaCook = new TeaCook();

        Thread borschtCookThread = CookThreadFactory.makeThread(borschtCook, 500L, borschtCookBefore, borschtLock);
        Thread cakeCookThread = CookThreadFactory.makeThread(cakeCook, 500L, cakeCookBefore, cakeLock);
        Thread teaCookThread = CookThreadFactory.makeThread(teaCook, 500L, teaCookBefore, teaLock);

        List<Thread> consumersThreads = new ArrayList<>();
        for (int i = 0; i < amountOfConsumers; i++) {
            consumersThreads.add(
                    ConsumerThreadFactory
                            .threadForSingleConsumer(
                                    List.of(borschtCook, cakeCook, teaCook),
                                    List.of(300L, 300L, 300L),
                                    List.of(borschtCookBefore, cakeCookBefore, teaCookBefore),
                                    new DishesConsumer(),
                                    locks,
                                    i
                            )
            );
        }

        borschtCookThread.start();
        cakeCookThread.start();
        teaCookThread.start();

        consumersThreads.forEach(Thread::start);
    }
}