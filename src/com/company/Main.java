package com.company;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
//	 write your code here
        String fileName = args.length > 0 ? args[0] : "test.txt";
        Assembler.process(fileName);


    }
}
