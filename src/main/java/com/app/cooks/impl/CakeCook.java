package com.app.cooks.impl;

import com.app.cooks.interfaces.FullFunctionalCook;
import org.apache.commons.lang3.mutable.MutableInt;

public class CakeCook implements FullFunctionalCook {

    @Override
    public String getMessage() {
        return "CAKE";
    }

    @Override
    public void cook(MutableInt currentCake) {
        currentCake.increment();
        System.out.println("Cake cook got: " + currentCake + " dishes");
    }
}
