package org.fpm.di.test7;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

import java.util.Stack;
import java.util.Vector;

public class Test7Configuration implements Configuration {
    @Override
    public void configure(Binder binder) {
        binder.bind(A.class, B.class);
        binder.bind(Vector.class, new Stack<>());
        binder.bind(C.class);
    }
}
