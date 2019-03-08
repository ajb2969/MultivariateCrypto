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
  private HBox top, midtop, midbot, bottom;
  private Label file_lbl, passwd_lbl, status;
  private HBox cont_file;
  private TextField file_name;
  private Button file_chooser;

  @Override
  public void update(Observable o, Object arg) {
    this.status.setText(model.getStatus());
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    top = new HBox();
    top.setSpacing(7);
    top.setPadding(new Insets(10, 10, 10, 10));
    // handles file choosing
    file_lbl = new Label("Choose file");
    cont_file = new HBox();
    file_name = new TextField();
    file_name.setText("");

    file_chooser = new Button();
    file_chooser.setOnAction(
        e -> {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Select file to encrypt");
          File file = fileChooser.showOpenDialog(primaryStage);
          if (file != null) {
            file_name.setText(file.getAbsolutePath());
            model.announce(null);
          }
        });
    cont_file.getChildren().addAll(file_name, file_chooser);
    top.getChildren().addAll(file_lbl, cont_file);

    // handles password
    midtop = new HBox();
    midtop.setSpacing(7);
    midtop.setPadding(new Insets(10, 10, 10, 10));
    passwd_lbl = new Label("Password");
    PasswordField password_field = new PasswordField();
    password_field.setText("");
    midtop.getChildren().addAll(passwd_lbl, password_field);

    midbot = new HBox();
    midbot.setSpacing(7);
    midbot.setPadding(new Insets(10, 10, 10, 10));
    Label save_lbl = new Label("Save As");
    TextField save_file = new TextField();
    midbot.getChildren().addAll(save_lbl, save_file);

    // handles saving encrypted file
    bottom = new HBox();
    bottom.setSpacing(7);
    bottom.setPadding(new Insets(10, 10, 10, 10));
    Button encrypt = new Button();
    encrypt.setText("Encrypt");
    encrypt.setOnAction(
        e -> model.encrypt(file_name.getText(), password_field.getText(), save_file.getText()));
    Button decrypt = new Button();
    decrypt.setText("Decrypt");
    decrypt.setOnAction(
        e -> model.decrypt(file_name.getText(), password_field.getText(), save_file.getText()));
    GridPane cryption = new GridPane();
    cryption.addColumn(0, encrypt);
    cryption.addColumn(1, decrypt);
    bottom.getChildren().add(cryption);

    HBox status_box = new HBox();
    status_box.setSpacing(7);
    status_box.setPadding(new Insets(10, 10, 10, 10));
    status = new Label(this.model.getStatus());
    status_box.getChildren().add(status);

    GridPane window_layout = new GridPane();
    window_layout.addRow(0, top);
    window_layout.addRow(1, midtop);
    window_layout.addRow(2, midbot);
    window_layout.addRow(3, bottom);
    window_layout.addRow(4, status_box);

    primaryStage.setTitle("Homework 5");
    primaryStage.setScene(new Scene(window_layout, 400, 250));
    primaryStage.show();
  }

  public void init() throws Exception {
    Logger.getAnonymousLogger().info("inti: Initialize and connect to model!");
    model = new EncryptionModel();
    model.addObserver(this);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
