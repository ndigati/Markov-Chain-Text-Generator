import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by ndigati on 7/4/15.
 */
public class Utils {
    /*public static void main(String[] args) throws IOException {

        Model m = new Model();
        String text = readFile("./Modest Proposal.txt", Charset.defaultCharset());
        m.generateModel(text);
        System.out.println(m.generateText(25));
    }*/

    /**
     * Function to convert a .txt file into a Java String
     * String can then be used in models generateModel() method
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
        StringBuilder newString = new StringBuilder(text);
        newString.setCharAt(0, Character.toUpperCase(text.charAt(0)));
        return newString.toString();
    }
}
