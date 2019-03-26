package model;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.TreeSet;
import java.util.logging.Logger;

public class EncryptionModel extends Observable {
    private String status;
    private static final String ERRORMSG = "Error: %s, %s is too big. ";

    public EncryptionModel() {
        this.status = "";
    }

    public void crypt(String selectedFile, String password, String saveFile, byte flag) {
        if (saveFile.equals("")) {
            this.status = "A save file name must be entered";
            announce(null);
        } else if (password.equals("")) {
            this.status = "A password must be entered in order to encrypt";
            announce(null);
        } else if (selectedFile.equals("")) {
            this.status = "A file must be selected to encrypt";
            announce(null);
        } else {
            this.status = "The encrypted file, " + saveFile + ", has been created";
            announce(null);

            try {
                byte[] file = Files.readAllBytes(Paths.get(selectedFile));
                short[] fileBytes = new short[file.length];
                for (int i = 0; i < file.length; i++) {
                    fileBytes[i] = file[i];
                }
                selectedFile = selectedFile.substring(0, selectedFile.lastIndexOf('/'));
                IdentityMatrix l1 = new IdentityMatrix(fileBytes.length);
                IdentityMatrix l2 = new IdentityMatrix(fileBytes.length);
                byte[] afterl2 = l2.convertAndMultiply(fileBytes);
                CentralMap cm = new CentralMap(password.getBytes(), afterl2);
                byte[] ciphertext;
                if (flag == 0) {
                    ciphertext = l1.convertAndMultiply(cm.encrypt());
                } else {
                    ciphertext = l1.convertAndMultiply(cm.decrypt());
                }

                writeFile(selectedFile, "/" + saveFile, ciphertext);
            } catch (NoSuchFileException e) {
                this.status = String.format("Error: " + "%s does not exist", e.getFile());
                announce(null);
            } catch (IOException e) {
                Logger.getAnonymousLogger().severe(e.getMessage());
            } catch (OutOfMemoryError e) {
                this.status = String.format(ERRORMSG, e.getCause(), e.getMessage());
                announce(null);
            }
            announce(null);
        }
    }

    public TreeSet<LetterFrequency> openLetterFreq(String selectedFile, String password) {
        try {
            byte[] file = Files.readAllBytes(Paths.get(selectedFile));
            short[] fileBytes = new short[file.length];
            for (int i = 0; i < file.length; i++) {
                fileBytes[i] = file[i];
            }
            IdentityMatrix l1 = new IdentityMatrix(fileBytes.length);
            IdentityMatrix l2 = new IdentityMatrix(fileBytes.length);
            byte[] afterl1 = l1.convertAndMultiply(fileBytes);
            CentralMap cm = new CentralMap(password.getBytes(), afterl1);
            byte[] ciphertext = l2.convertAndMultiply(cm.encrypt());
            Frequency freq = new Frequency(file, ciphertext);
            return new TreeSet<>(freq.getFrequencies());
        } catch (NoSuchFileException e) {
            this.status = String.format("Error: %s does not exist", e.getFile());
            announce(null);
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
        } catch (OutOfMemoryError e) {
            this.status = String.format(ERRORMSG, e.getCause(), e.getMessage());
            announce(null);
        }
        return new TreeSet<>();
    }

    public void cipherChanges(String selectedFile, String password) {
        try {
            byte[] file = Files.readAllBytes(Paths.get(selectedFile));
            short[] fileBytes = new short[file.length];
            for (int i = 0; i < file.length; i++) {
                fileBytes[i] = file[i];
            }

            ArrayList<GridPaneData> data = new ArrayList<>();
            Percentage five = new Percentage(fileBytes, (float) 0.05);
            for(int i =1; i< 6;i++) {
                int passwordLen = 3 * i;
                while(password.length() < passwordLen) {
                    password = password.concat(password);
                }
                password = password.substring(0, passwordLen);
                IdentityMatrix l1 = new IdentityMatrix(five.getText().length);
                IdentityMatrix l2 = new IdentityMatrix(five.getText().length);
                byte[] afterl1 = l1.convertAndMultiply(five.getText());
                CentralMap cm = new CentralMap(password.getBytes(), afterl1);
                byte[] ciphertext = l2.convertAndMultiply(cm.encrypt());
                data.add(new GridPaneData(five.getPercent(), password.length(), five.getDiff(ciphertext)));
            }

            Percentage ten = new Percentage(fileBytes, (float) 0.1);
            for(int i =1; i< 6;i++) {
                int passwordLen = 3 * i;
                while(password.length() < passwordLen) {
                    password = password.concat(password);
                }
                password = password.substring(0, passwordLen);
                IdentityMatrix l1 = new IdentityMatrix(ten.getText().length);
                IdentityMatrix l2 = new IdentityMatrix(ten.getText().length);
                byte[] afterl1 = l1.convertAndMultiply(ten.getText());
                CentralMap cm = new CentralMap(password.getBytes(), afterl1);
                byte[] ciphertext = l2.convertAndMultiply(cm.encrypt());
                data.add(new GridPaneData(ten.getPercent(), password.length(), ten.getDiff(ciphertext)));
            }

            GridPane gp = new GridPane();
            gp.setVgap(20);
            gp.setPadding(new Insets(10,10,10,10));
            Label l1 = new Label("Percent of plaintext changed");
            l1.setPadding(new Insets(10,10,10,10));
            Label l2 = new Label("Length of password");
            l2.setPadding(new Insets(10,10,10,10));
            Label l3 = new Label("Percent of ciphertext changed");
            l3.setPadding(new Insets(10,10,10,10));
            gp.add(l1,0, 0);

            gp.add(l2,1, 0);
            gp.add(l3,2, 0);

            int row = 1;
            for(GridPaneData gpd: data) {
                gp.add(new Label(String.valueOf(gpd.getPercent())),0, row);
                gp.add(new Label(String.valueOf(gpd.getPassLength())),1, row);
                gp.add(new Label(String.valueOf(gpd.getDiff())),2, row);
                row +=1;
            }

            Scene s = new Scene(gp);
            Stage stage = new Stage();
            stage.setTitle("Ciphertext changes");
            stage.setScene(s);
            stage.show();

        } catch (NoSuchFileException e) {
            this.status = String.format("Error: %s does not exist", e.getFile());
            announce(null);
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
        } catch (OutOfMemoryError e) {
            this.status = String.format(ERRORMSG, e.getCause(), e.getMessage());
            announce(null);
        }

    }

    public void passwordChanges(String selectedFile, String password) {
        byte[] file = new byte[0];
        try {
            file = Files.readAllBytes(Paths.get(selectedFile));
            short[] fileBytes = new short[file.length];
            for (int i = 0; i < file.length; i++) {
                fileBytes[i] = file[i];
            }



            //generate password for each iteratation of 1-3 change passwords
            ArrayList<GridPaneData> passChanges = new ArrayList<>();
            for(int i = 1; i < 6; i++) {
                Random r = new Random();
                r.setSeed(31 + i);
                int passwordLen = 3 * i;
                while(password.length() < passwordLen) {
                    password = password.concat(password);
                }
                password = password.substring(0, passwordLen);


                //create some length password
                IdentityMatrix l1 = new IdentityMatrix(fileBytes.length);
                IdentityMatrix l2 = new IdentityMatrix(fileBytes.length);
                byte[] afterl1 = l1.convertAndMultiply(fileBytes);
                CentralMap cm = new CentralMap(password.getBytes(), afterl1);
                byte[] oriCiphertext = l2.convertAndMultiply(cm.encrypt());
                short [] transfer = new short[oriCiphertext.length];
                for(int j = 0; j < oriCiphertext.length; j++) {
                    transfer[j] = oriCiphertext[j];
                }
                //determine ciphertext with original password
                Percentage p = new Percentage(transfer, (float)0);
                StringBuilder changes = new StringBuilder(password);
                for(int j = 1; j < 4; j++) {
                    int counter = 0;
                    int index = 0;
                    while(counter != j) {
                        if(r.nextBoolean()) {
                            changes.setCharAt(index, (char) r.nextInt(127));
                            counter+=1;
                        }

                        if(index == password.length()) {
                            index = 0;
                        } else {
                            index+=1;
                        }
                    }

                    byte[] al1 = l1.convertAndMultiply(fileBytes);
                    CentralMap revCm = new CentralMap(changes.toString().getBytes(), al1);
                    byte[] newCiphertext = l2.convertAndMultiply(revCm.encrypt());
                    passChanges.add(new GridPaneData(j, changes.length(), p.getDiff(newCiphertext)));
                    changes = new StringBuilder(password);
                }
            }

            GridPane gp = new GridPane();
            gp.setVgap(20);
            gp.setPadding(new Insets(10,10,10,10));
            Label l1 = new Label("Length of Password changed");
            l1.setPadding(new Insets(10,10,10,10));
            Label l2 = new Label("Number of characters changed");
            l2.setPadding(new Insets(10,10,10,10));
            Label l3 = new Label("Percent of ciphertext changed");
            l3.setPadding(new Insets(10,10,10,10));
            gp.add(l1,0, 0);
            gp.add(l2,1, 0);
            gp.add(l3,2, 0);

            int row = 1;
            for(GridPaneData gpd: passChanges) {
                gp.add(new Label(String.valueOf(gpd.getPassLength())),0, row);
                gp.add(new Label(String.valueOf(gpd.getPercent())),1, row);
                gp.add(new Label(String.valueOf(gpd.getDiff())),2, row);
                row +=1;
            }

            Scene s = new Scene(gp);
            Stage stage = new Stage();
            stage.setTitle("Password changes");
            stage.setScene(s);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return this.status;
    }

    private void writeFile(String directoryPath, String filePath, byte[] strOutput) {

        try (PrintWriter output = new PrintWriter(directoryPath + filePath, "UTF-8")) {
            for (short b : strOutput) {
                output.write((char) b);
            }
            output.flush();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }

    public void announce(String arg) {
        setChanged();
        notifyObservers(arg);
    }
}

class GridPaneData {
    private float percent;
    private int passLength;
    private float diff;
    public GridPaneData(float percent, int length, float diff) {
        this.percent = percent;
        this.passLength = length;
        this.diff = diff;
    }


    public float getDiff() {
        return diff;
    }

    public int getPassLength() {
        return passLength;
    }

    public float getPercent() {
        return percent;
    }
}
