package com.hackx.unclassified;

import java.io.Serializable;

public abstract class Task implements Serializable {

    public static String superStr = "superStr";

    public void run() {
        System.out.println("Task Run");
    }
}
