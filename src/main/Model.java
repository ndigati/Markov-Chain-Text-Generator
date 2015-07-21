package main;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ndigati on 7/4/15.
 */

public class Model {
    private Multimap<String, String> model;

    public Model() {
        model = ArrayListMultimap.create();
    }

    public void generateModel(String text) {
        if (text == null) {
            return;
        }

        String[] txt = text.split(" ");

        for (int i = 0; i < txt.length - 1; i++) {
            model.put(txt[i], txt[i+1]);
        }
    }

    public String generateText(int numWords) {
        if (model.size() == 0) {
            return "";
        }
        Random random = new Random();
        StringBuilder sentence = new StringBuilder();
        String storeWord = "";

        for (int i = 0; i < numWords - 1; i++) {
            ArrayList<String> values;
            if (storeWord.equals("")) {
                ArrayList<String> keys = new ArrayList<>(model.keySet());
                String firstWord = keys.get(random.nextInt(keys.size()));
                values = new ArrayList<>(model.get(firstWord));
                storeWord = values.get(random.nextInt(values.size()));
                sentence.append(firstWord).append(" ").append(storeWord).append(" ");
            } else {
                values = new ArrayList<>(model.get(storeWord));
                storeWord = values.get(random.nextInt(values.size()));
                sentence.append(storeWord).append(" ");
            }

        }
        return sentence.toString();
    }

    public int modelSize() { return model.size(); }

    public boolean containsKey(String key) { return model.containsKey(key); }

    public static void main(String[] args) {
        Model m = new Model();
        m.generateModel("Hello World.");
    }
}
