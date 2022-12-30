package org.example;

import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args) {
        System.out.println(Modifier.isAbstract(A.class.getModifiers()));
    }
}

class A {

}