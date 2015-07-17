import java.util.*;

/**
 * Created by ndigati on 7/4/15.
 */
public class Model {

    private Map<Object, ArrayList<String>> model = new HashMap<>();

    /**
     * Generate the Markov Chain model based on the given text
     * @param text String used to generate the model
     */
    public void generateModel(String text) {
        // Regex to find beginning of sentences
        // [\.\!\?]\s+[A-Z]
        // TODO: Need a way of detecting beginning of sentences to add those words to '{{' list
        String[] txt = text.split(" ");
        for (int i = 0; i < txt.length; i++) {
            boolean inBounds = (i+1 >= 0) && (i+1 < txt.length);
            String key, value;

            if (inBounds) {
                key = txt[i];
                value = txt[i+1];
            } else {
                return;
            }

            if (!model.containsKey(key)) {
                ArrayList<String> values = new ArrayList<>();
                values.add(value);
                model.put(key, values);
            } else {
                model.get(key).add(value);
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
        for (Map.Entry<Object, ArrayList<String>> entry : model.entrySet()) {
            String key = (String) entry.getKey();
            ArrayList<String> values = entry.getValue();

            result = result.concat("{ " + key + ": {");
            for (String s : values) {
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
    // New Algorithm to generate text
    // 1. Beginning of sentences are marked with ' {{ '
    // 2. Choose a random value that occurs after the ' {{ ' (this will be the start of sentence, capitalize if needed)
    // 3. If it encounters a word that ends in a period, choose a new word from the list of sentence starters (' {{ ')
    // 4. Continue until the max number of words it reached
    public String generateText(int numWords) {
        boolean beginningOfSentence = true;
        StringBuilder text = new StringBuilder();
        String firstWord = "";
        String lastWord = "";
        Random random = new Random();
        ArrayList<String> values;

        for (int i = 0; i <= numWords; i++) {
            if (beginningOfSentence) {
                values = model.get("{{");
                firstWord = values.get(random.nextInt(values.size()));

                values = model.get(firstWord);
                lastWord = values.get(random.nextInt(values.size()));

                text.append(firstWord).append(" ").append(lastWord).append(" ");

                firstWord = lastWord;

                beginningOfSentence = false;
            } else {
                values = model.get(firstWord);
                lastWord = values.get(random.nextInt(values.size()));

                text.append(lastWord).append(" ");
                if (lastWord.endsWith(".")) {
                    beginningOfSentence = true;
                }

                firstWord = lastWord;
            }
        }
        return text.toString();
    }

    public boolean emptyModel() {
        return model.isEmpty();
    }
}
