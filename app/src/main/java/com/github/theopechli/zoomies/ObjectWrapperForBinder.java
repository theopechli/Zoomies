package com.github.theopechli.zoomies;

import android.os.Binder;

public class ObjectWrapperForBinder extends Binder {

    private final Object object;

    public ObjectWrapperForBinder(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
