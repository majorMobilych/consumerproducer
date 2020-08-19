package com.app.cooks.impl;

import com.app.DataUtil;
import com.app.cooks.interfaces.FullFunctionalCook;
import org.apache.commons.lang3.mutable.MutableInt;

public class TeaCook implements FullFunctionalCook {

    @Override
    public String getMessage() {
        return "TEA";
    }

    @Override
    public void cook(MutableInt currentTea) {
        currentTea.increment();
        System.out.println("Tea cook got: " + currentTea + " dishes" + " " + DataUtil.getCurrentLocalDateTimeStamp());
    }
}
