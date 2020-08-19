package com.app.cooks.impl;

import com.app.cooks.interfaces.FullFunctionalCook;
import org.apache.commons.lang3.mutable.MutableInt;

public class BorschtCook implements FullFunctionalCook {

    @Override
    public String getMessage() {
        return "BORSCHT";
    }

    @Override
    public void cook(MutableInt currentBorscht) {
        currentBorscht.increment();
        System.out.println("Borscht cook got: " + currentBorscht + " dishes");
    }
}
