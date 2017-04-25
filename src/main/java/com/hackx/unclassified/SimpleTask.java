package com.hackx.unclassified;

public class SimpleTask extends Task {

    public static String staticStr = "staticStr";
    private String name = "Hello Kryo";

    @Override
    public void run() {
        System.out.println("SimpleTask Run");
        System.out.println(name);
    }

}
