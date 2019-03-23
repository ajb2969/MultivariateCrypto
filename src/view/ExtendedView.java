package view;

import java.util.logging.Logger;
import model.EncryptionModel;
import model.LetterFrequency;
import model.Percentage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExtendedView {
  private Stage s;
  private EncryptionModel model;
  private static final String LETTERFREQ = "openLetterFreq";
  private static final String CIPHERCHANGE = "cipherChanges";
  private static final String PASSCHANGE = "passwordChanges";

  ExtendedView(Stage s, EncryptionModel model) {
    this.s = s;
    this.model = model;
  }

  private void letterFrequency(TreeSet<LetterFrequency> freq) {
    s.setTitle("Letter Frequencies");
    final CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Letter");
    final NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Frequency");
    final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
    XYChart.Series plaintext = new XYChart.Series();
    plaintext.setName("Plaintext");
    Iterator<LetterFrequency> lfreq = freq.iterator();
    while (lfreq.hasNext()) {
      LetterFrequency l = lfreq.next();
      if (l.getC() >= 65 && l.getC() <= 90) {
        // comparing only lowercase a-z
        plaintext.getData().add(new XYChart.Data(l.getC().toString(), l.getFreq1()));
      }
    }
    lfreq = freq.iterator();
    XYChart.Series ciphertext = new XYChart.Series();
    ciphertext.setName("Ciphertext");
    while (lfreq.hasNext()) {
      LetterFrequency l = lfreq.next();
      if (l.getC() >= 65 && l.getC() <= 90) {
        // comparing only lowercase a-z
        ciphertext.getData().add(new XYChart.Data(l.getC().toString(), l.getFreq2()));
      }
    }
    Scene scene = new Scene(bc, 800, 600);
    bc.getData().addAll(plaintext, ciphertext);
    s.setScene(scene);
  }

  private void multipleResults(List<TreeSet<Percentage>> results) {

    if(results.isEmpty()) {

    }
  }

  public void showOptions() {
    GridPane listing = new GridPane();
    listing.setHgap(20);
    listing.setVgap(20);
    listing.setAlignment(Pos.BASELINE_CENTER);
    RowConstraints rc = new RowConstraints();
    rc.setMinHeight(20);
    listing.getRowConstraints().add(rc);
    Label selectLabel = new Label("Please select an option: ");
    selectLabel.setTextAlignment(TextAlignment.CENTER);
    selectLabel.setPadding(new Insets(10));
    Button letterFrequency = new Button("Letter Frequency");
    letterFrequency.setOnAction(e -> setScene(LETTERFREQ));

    letterFrequency.setMinSize(10, 10);
    Button plainChange = new Button("Ciphertext Changes");
    plainChange.setOnAction(e -> setScene(CIPHERCHANGE));

    Button passChange = new Button("Password Changes");
    passChange.setOnAction(e -> setScene(PASSCHANGE));

    listing.addRow(0, selectLabel);
    listing.add(letterFrequency, 0, 1);
    listing.add(plainChange, 0, 2);
    listing.add(passChange, 0, 3);

    Scene scene = new Scene(listing, 400, 250);
    this.s.setScene(scene);
  }

  private void setScene(String method) {
    TextField fileName;
    HBox top = new HBox();
    top.setSpacing(7);
    top.setPadding(new Insets(10, 10, 10, 10));
    Label fileLbl = new Label("Choose file");
    HBox contFile = new HBox();
    fileName = new TextField();
    fileName.setText("");
    Button fileChooser = new Button();
    fileChooser.setOnAction(
        e -> {
          FileChooser fChooser = new FileChooser();
          fChooser.setTitle("Select file to encrypt");
          File file = fChooser.showOpenDialog(this.s);
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

    HBox low = new HBox();
    Button start = new Button("Start");
    start.setOnAction(
        e -> {
          try {
            if (!passwordField.getText().equals("") && !fileName.getText().equals("")) {
              Method m = model.getClass().getMethod(method, String.class, String.class);
              Object o = m.invoke(model, fileName.getText(), passwordField.getText());
              if (o instanceof TreeSet) {
                letterFrequency((TreeSet<LetterFrequency>) o);
              }

              if (o instanceof List) {
                multipleResults((List<TreeSet<Percentage>>) o);
              }
            }
          } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
            Logger.getAnonymousLogger().severe(e1.getMessage());
          }
        });
    low.getChildren().add(start);

    GridPane windowLayout = new GridPane();
    windowLayout.addRow(0, top);
    windowLayout.addRow(1, midtop);
    windowLayout.addRow(2, low);
    this.s.setScene(new Scene(windowLayout, 400, 250));
  }
}
