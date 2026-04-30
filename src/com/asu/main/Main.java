package com.asu.main;

import com.asu.data.FileHandler;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String fileName = "data/users.txt";

        FileHandler.createFile(fileName);

        FileHandler.writeToFile(fileName,
                "U001|Nelly|123456|Nelly.123|pass|Customer");

        FileHandler.writeToFile(fileName,
                "U002|John|789101|john123|pass|Manager");

        System.out.println("READ:");
        List<String> data = FileHandler.readFile(fileName);
        for (String line : data) {
            System.out.println(line);
        }


    }
}
