package com.app.consumers.impl;

import com.app.consumers.Consumer;

public class DishesConsumer implements Consumer {

    @Override
    public void consume(String message) {
        System.out.println("Consumer consume " + message + " dish");
    }
}
