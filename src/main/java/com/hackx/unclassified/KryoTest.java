package com.hackx.unclassified;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class KryoTest {

    public static void main(String[] args) throws Exception {
        Kryo kryo = new Kryo();

        Output output = new Output(new FileOutputStream("file.bin"));
        SimpleTask simpleTask = new SimpleTask();
        kryo.writeObject(output, simpleTask);
        output.close();

        Input input = new Input(new FileInputStream("file.bin"));
        SimpleTask simpleTask1 = kryo.readObject(input, SimpleTask.class);
        simpleTask1.run();
        input.close();
    }

}
