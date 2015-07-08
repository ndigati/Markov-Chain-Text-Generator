import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ndigati on 7/4/15.
 */
public class Model {

    private Map<Key, ArrayList<String>> model = new HashMap<>();

    /**
     * Generate the Markov Chain model based on the given text
     * @param text String used to generate the model
     */
    public void generateModel(String text) {
        String[] txt = text.split(" ");
        for (int i = 0; i < txt.length; i++) {
            boolean inBounds = (i+2 >= 0) && (i+2 < txt.length);
            String firstWord, secondWord, thirdWord;

            if (inBounds) {
                firstWord = txt[i];
                secondWord = txt[i+1];
                thirdWord = txt[i+2];
            } else {
                return;
            }

            Key k = new Key(firstWord, secondWord);

            if(!model.containsKey(k)) {
                ArrayList<String> value = new ArrayList<>();
                value.add(thirdWord);
                model.put(k, value);
            } else {
                model.get(k).add(thirdWord);
            }
        }
    }

    /**
     * Testing method to print out the generated model
     * Used for debugging the model
     * @return String representation of the model
     */
    public String printModel() {
        String result = "";
        for (Map.Entry<Key, ArrayList<String>> entry : model.entrySet()) {
            Key key = entry.getKey();
            ArrayList<String> value = entry.getValue();

            result = result.concat("{ " + key.key1 + " , " + key.key2 +  ": {");
            for (String s : value) {
                result = result.concat("\'" + s + "\': " + ", ");
            }
            result = result.concat("}\n");
        }
        return result.concat("\n");
    }

    /**
     * Generate a text sentence based on the model
     * @param numWords number of words in the generated sentence
     * @return String generated sentence
     */
    public String generateText(int numWords) {
        String text = "";
        String secondWord = "";
        String lastWord = "";
        Random random = new Random();

        for (int i = 0; i < numWords - 1; i++) {
            Key randomKey;
            String key1, key2;
            ArrayList<String> values;
            // First triplet of words is chosen completely randomly
            if (secondWord.equals("")) {
                ArrayList<Key> keys = new ArrayList<>(model.keySet());
                randomKey = keys.get(random.nextInt(keys.size()));
                key1 = randomKey.getKey1();
                key2 = randomKey.getKey2();
                values = model.get(randomKey);
                lastWord = values.get(random.nextInt(values.size()));
                text = text.concat(key1 + " " + key2 + " " + lastWord + " ");
                secondWord = key2;
            }
            // Remaining pairs of words are chosen with the previous second choice as the key
            else {
                values = model.get(new Key(secondWord, lastWord));
                if (values == null) {
                    // The new key was not in the set of available keys
                    // Algorithm probably chose the last word to be in the new Key
                    // If this happens just choose randomly from all keys again
                    ArrayList<Key> keys = new ArrayList<>(model.keySet());
                    values = model.get(keys.get(random.nextInt(keys.size())));
                }

                // Update secondWord first to keep track of the new second word in the next iteration
                secondWord = lastWord;
                lastWord = values.get(random.nextInt(values.size()));
                text = text.concat(lastWord + " ");
            }
        }

        return text;
    }

    class Key {
        private String key1;
        private String key2;

        public Key(String key1, String key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public String getKey1() { return this.key1; }

        public String getKey2() { return this.key2; }

        @Override
        public int hashCode() {
            int result = key1.hashCode();
            result = 31 * result + key2.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) { return false; }
            if (o == this) { return true; }
            if (!(o instanceof Key)) { return false; }
            Key k = (Key)o;
            return k.key1.equals(this.key1) && k.key2.equals(this.key2);
        }
    }
}
