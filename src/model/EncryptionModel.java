package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Observable;
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
            IdentityMatrix l1 = new IdentityMatrix(fileBytes.length);
            IdentityMatrix l2 = new IdentityMatrix(fileBytes.length);
            byte[] afterl1 = l1.convertAndMultiply(fileBytes);
            CentralMap cm = new CentralMap(password.getBytes(), afterl1);
            byte[] ciphertext = l2.convertAndMultiply(cm.encrypt());

            


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
            IdentityMatrix l1 = new IdentityMatrix(fileBytes.length);
            IdentityMatrix l2 = new IdentityMatrix(fileBytes.length);
            byte[] afterl1 = l1.convertAndMultiply(fileBytes);
            CentralMap cm = new CentralMap(password.getBytes(), afterl1);
            byte[] ciphertext = l2.convertAndMultiply(cm.encrypt());


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
