import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application implements Observer {
  private EncryptionModel model;
  private Label status;

  @Override
  public void update(Observable o, Object arg) {
    this.status.setText(model.getStatus());
  }

  @Override
  public void start(Stage primaryStage) {
    TextField fileName;
    HBox top = new HBox();
    top.setSpacing(7);
    top.setPadding(new Insets(10, 10, 10, 10));
    // handles file choosing
    Label fileLbl = new Label("Choose file");
    HBox contFile = new HBox();
    fileName = new TextField();
    fileName.setText("");

    Button fileChooser = new Button();
    fileChooser.setOnAction(
        e -> {
          FileChooser fChooser = new FileChooser();
          fChooser.setTitle("Select file to encrypt");
          File file = fChooser.showOpenDialog(primaryStage);
          if (file != null) {
            fileName.setText(file.getAbsolutePath());
            model.announce(null);
          }
        });
    contFile.getChildren().addAll(fileName, fileChooser);
    top.getChildren().addAll(fileLbl, contFile);

    // handles password
    HBox midtop = new HBox();
    midtop.setSpacing(7);
    midtop.setPadding(new Insets(10, 10, 10, 10));
    Label passwdLbl = new Label("Password");
    PasswordField passwordField = new PasswordField();
    passwordField.setText("");
    midtop.getChildren().addAll(passwdLbl, passwordField);

    HBox midbot = new HBox();
    midbot.setSpacing(7);
    midbot.setPadding(new Insets(10, 10, 10, 10));
    Label saveLbl = new Label("Save As");
    TextField saveFile = new TextField();
    midbot.getChildren().addAll(saveLbl, saveFile);

    // handles saving encrypted file
    HBox bottom = new HBox();
    bottom.setSpacing(7);
    bottom.setPadding(new Insets(10, 10, 10, 10));
    Button encrypt = new Button();
    encrypt.setText("Encrypt");
    encrypt.setOnAction(
        e ->
            model.crypt(fileName.getText(), passwordField.getText(), saveFile.getText(), (byte) 0));
    Button decrypt = new Button();
    decrypt.setText("Decrypt");
    decrypt.setOnAction(
        e ->
            model.crypt(fileName.getText(), passwordField.getText(), saveFile.getText(), (byte) 1));
    Button lFreq = new Button();
    lFreq.setText("Cipher Analysis");
    lFreq.setOnAction(
        e -> {
          ExtendedView ev = new ExtendedView(primaryStage, model, this.status);
          ev.showOptions();
        });
    GridPane cryption = new GridPane();
    cryption.setHgap(10);
    cryption.addColumn(0, encrypt);
    cryption.addColumn(1, decrypt);
    cryption.addColumn(2, lFreq);
    bottom.getChildren().add(cryption);

    HBox statusBox = new HBox();
    statusBox.setSpacing(7);
    statusBox.setPadding(new Insets(10, 10, 10, 10));
    status = new Label(this.model.getStatus());
    statusBox.getChildren().add(status);

    GridPane windowLayout = new GridPane();
    windowLayout.addRow(0, top);
    windowLayout.addRow(1, midtop);
    windowLayout.addRow(2, midbot);
    windowLayout.addRow(3, bottom);
    windowLayout.addRow(4, statusBox);

    primaryStage.setTitle("Homework 5");
    primaryStage.setScene(new Scene(windowLayout, 400, 250));
    primaryStage.show();
  }

  public void init() {
    Logger.getAnonymousLogger().info("inti: Initialize and connect to model!");
    model = new EncryptionModel();
    model.addObserver(this);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
