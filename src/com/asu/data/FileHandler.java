package com.asu.data;

    import java.io.*;
    import java.util.*;

public class FileHandler {

    public static void createFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + fileName);
            } else { 
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("Error creating file.");
            e.printStackTrace();
        }
    }

    public static List<String> readFile(String fileName) {
        List<String> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                data.add(line);
            }

        } catch (IOException e) {
            System.out.println("Error reading file.");
            e.printStackTrace();
        }

        return data;
    }

    public static void writeToFile(String fileName, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
            e.printStackTrace();
        }
    }

    public static void overwriteFile(String fileName, List<String> content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : content) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error overwriting file.");
            e.printStackTrace();
        }
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.delete()) {
            System.out.println("File deleted.");
        } else {
            System.out.println("Failed to delete file.");
        }
    }
}
