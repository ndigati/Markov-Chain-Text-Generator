package main;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by ndigati on 7/4/15.
 */
public class Utils {
    /*public static void main(String[] args) throws IOException {
        runPythonScript("test");
    }*/

    /**
     * Function to convert a .txt file into a Java String
     * String can then be used in models generateModel() method
     *
     * @param path Path to .txt file
     * @param encoding Encoding you want to use on the String
     * @return String representation of the .txt file
     * @throws IOException
     */
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding).replaceAll("\\r\\n|\\r|\\n", " ");
    }

    public static String capitalizeFirstLetter(String text) {
        if (text == null || text.length() == 0) {
            return "";
        }

        StringBuilder newString = new StringBuilder(text);
        newString.setCharAt(0, Character.toUpperCase(text.charAt(0)));
        return newString.toString();
    }

    public static void runPythonScript(String scriptName, String subreddit) {
        try {
            scriptName = scriptName.concat(".py");
            if (subreddit == null || subreddit.equals("")) {
                Process p = Runtime.getRuntime().exec(new String[]{"python3", scriptName});
            } else {
                Process p = Runtime.getRuntime().exec(new String[]{"python3", scriptName, subreddit});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
