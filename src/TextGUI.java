/**
 * Created by ndigati on 7/8/15.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TextGUI extends Application {

    Model model = new Model();
    static boolean debug = false;

    public static void main(String[] args) {
        ArrayList<String> s = new ArrayList<>();
        s.add("hello");
        s.add("world");
        String t = String.join(" ", s);
        System.out.println(t);

        launch(args);

        if (!debug) {
            try {
                File file = new File(System.getProperty("user.dir") + "/comments.txt");

                if (file.delete()) {
                    System.out.println("File deleted");
                } else {
                    System.out.println("File not deleted!!");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Generation");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label filePath = new Label("Text File Path: ");
        grid.add(filePath, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        FileChooser fileChooser = new FileChooser();

        Button openButton = new Button("Open a text file");
        Button redditButton = new Button("Generate from Reddit Comments");
        HBox hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.CENTER_RIGHT);
        hbBtn1.getChildren().add(openButton);
        hbBtn1.getChildren().add(redditButton);
        grid.add(hbBtn1, 0, 2, 2, 1);

        // Open File button action
        openButton.setOnAction(event -> {
            String text;
            try {
                String path;
                try {
                    path = fileChooser.showOpenDialog(primaryStage).getPath();
                    text = Utils.readFile(path, Charset.defaultCharset());
                    model.generateModel(text);
                    userTextField.setText(path);
                } catch (NullPointerException e) {
                    path = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Get Reddit comments button action
        redditButton.setOnAction(event -> {
            userTextField.setText("Gathering reddit comments!");

            Stage stage = new Stage();
            stage.setTitle("Choose Subreddit");

            GridPane grid1 = new GridPane();
            grid1.setAlignment(Pos.TOP_CENTER);
            grid1.setHgap(10);
            grid1.setVgap(10);
            grid1.setPadding(new Insets(25, 25, 25, 25));

            Text sceneTitle1 = new Text("Choose a subreddit to get comments from:");
            sceneTitle1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid1.add(sceneTitle1, 0, 0, 2, 1);

            TextField subredditName = new TextField();
            grid1.add(subredditName, 0, 1);

            Button btn = new Button("Enter");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.CENTER_RIGHT);
            hbBtn.getChildren().add(btn);
            grid1.add(hbBtn, 1, 1);

            btn.setOnAction(event1 -> {
                String subreddit;
                try {
                    subreddit = subredditName.getText();
                } catch (NullPointerException e) {
                    subreddit = null;
                }

                stage.close();

                Utils.runPythonScript("redditComments", subreddit);
                File commentFile = new File(System.getProperty("user.dir") + "/comments.txt");
                while (!commentFile.exists() && !commentFile.isDirectory()) {}
                String path = commentFile.getAbsolutePath();
                System.out.println("File has been created at: " + path);

                try {
                    //TODO: Updated the way comments are separated in the text file, so update to reflect change.
                    String text = Utils.readFile(path, Charset.defaultCharset());
                    text = text.replace(" , ", " ");
                    model.generateModel(text);
                    userTextField.setText("Finished gathering comments!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            subredditName.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    btn.fire();
                }
            });

            Scene scene = new Scene(grid1, 500, 200);
            stage.setScene(scene);

            stage.setOnCloseRequest(WindowEvent -> {
                userTextField.setText("Cancelled gathering Reddit comments!");
                stage.close();
            });

            stage.show();
        });

        Button btn = new Button("Generate Text");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(300);
        grid.add(actionTarget, 0, 10, 2, 1);


        // Generate Markov Chain text button action
        btn.setOnAction(event -> {
            actionTarget.setFill(Color.FIREBRICK);
            try {
                actionTarget.setText(Utils.capitalizeFirstLetter(model.generateText(20)));
            } catch (IllegalArgumentException e) {
                actionTarget.setText("Model has not been generated. Please select a valid file or use Reddit comments");
            }
        });

        Scene scene = new Scene(grid, 400, 375);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
