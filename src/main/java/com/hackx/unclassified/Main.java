package com.hackx.unclassified;

public class Main {
    public static void main(String[] args) throws Exception {
//        SimpleTask simpleTask = new SimpleTask();
//        FileSerializer.writeObjectToFile(simpleTask, "task.ser");
//        ClassManipulator.saveClassFile(simpleTask);

        ClassLoader fileClassLoader = new FileClassLoader();
        Task simpleTask = (Task) FileSerializer.readObjectFromFile("task.ser", fileClassLoader);
        simpleTask.run();
        System.out.printf(simpleTask.superStr);
    }
}
