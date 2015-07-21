import main.Model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by ndigati on 7/20/15.
 */
public class ModelTest {

    @Test
    public void TestGenerateModel() {
        // Empty string and null should give a 0 size model
        Model model = new Model();
        model.generateModel("");
        assertEquals(0, model.modelSize());

        model = new Model();
        model.generateModel(null);
        assertEquals(0, model.modelSize());

        // Strings should give a model size of the number of words - 1
        model = new Model();
        model.generateModel("Hello World.");
        assertEquals(1, model.modelSize());

        model = new Model();
        model.generateModel("This is a test sentence. With more words to test again.");
        assertEquals(10, model.modelSize());

        model = new Model();
        String test = "Another sentence this time in a variable to use the length property of strings";
        model.generateModel(test);
        String[] splitTest = test.split(" ");
        assertEquals(splitTest.length - 1, model.modelSize());

        // Model should contain keys for all words in the sentence except the last word.
        assertEquals(true, model.containsKey(splitTest[5]));
        assertEquals(false, model.containsKey(splitTest[splitTest.length - 1]));
    }
}
