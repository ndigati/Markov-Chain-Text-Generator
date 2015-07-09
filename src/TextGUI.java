/**
 * Created by ndigati on 7/8/15.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.Charset;

public class TextGUI extends Application {

    Model model = new Model();

    public static void main(String[] args) {
        launch(args);
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

        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text;
                try {
                    // TODO: Use python script for grabbing reddit comments to generate the model
                    // TODO: If the user doesn't want to specify their own model text
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
            }
        });

        redditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                userTextField.setText("Gathering reddit comments!");

                Stage stage = new Stage();
                stage.setTitle("Choose Subreddit");

                GridPane grid = new GridPane();
                grid.setAlignment(Pos.TOP_CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));

                Text sceneTitle = new Text("Choose a subreddit to get comments from:");
                sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid.add(sceneTitle, 0, 0, 2, 1);

                TextField subredditName = new TextField();
                grid.add(subredditName, 0, 1);

                Button btn = new Button("Enter");
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.CENTER_RIGHT);
                hbBtn.getChildren().add(btn);
                grid.add(hbBtn, 1, 1);

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String subreddit;
                        try {
                            subreddit = subredditName.getText();
                        } catch (NullPointerException e) {
                            subreddit = null;
                        }

                        stage.close();

                        //TODO: Add support for running python script
                        //TODO: And generating the model for those gathered comments.
                        System.out.println(subreddit);
                    }
                });

                Scene scene = new Scene(grid, 500, 200);
                stage.setScene(scene);

                stage.show();
            }
        });

        Button btn = new Button("Generate Text");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(300);
        grid.add(actionTarget, 0, 10, 2, 1);


        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                actionTarget.setFill(Color.FIREBRICK);
                try {
                    actionTarget.setText(Utils.capitalizeFirstLetter(model.generateText(30)));
                } catch (IllegalArgumentException e) {
                    actionTarget.setText("Model has not been generated. Please select a valid file or use Reddit comments");
                }
            }
        });

        Scene scene = new Scene(grid, 400, 375);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
