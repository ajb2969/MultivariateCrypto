import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Observable;

class EncryptionModel extends Observable {
    private String status;
    public EncryptionModel() {
        this.status = "";
    }


    void encrypt(String selectedFile, String password, String saveFile) {
        if(saveFile.equals("")) {
            this.status = "A save file name must be entered";
            announce(null);
        } else if(password.equals("")) {
            this.status = "A password must be entered in order to encrypt";
            announce(null);
        } else if(selectedFile.equals("")) {
            this.status = "A file must be selected to encrypt";
            announce(null);
        } else {
            this.status = "The encrypted file, " +saveFile+", has been created";
            announce(null);

            try {
                byte [] file = Files.readAllBytes(Paths.get(selectedFile));
                selectedFile = selectedFile.substring(0, selectedFile
                        .lastIndexOf("/"));
                IdentityMatrix l1 = new IdentityMatrix(file.length);
                byte [] afterl1 = l1.convertAndMultiply(file);
                CentralMap cm = new CentralMap(password.getBytes(), afterl1);
                IdentityMatrix l2 = new IdentityMatrix(file.length);
                byte [] ciphertext = l2.convertAndMultiply(cm.encrypt());
                writeFile(selectedFile, "/" + saveFile, ciphertext);
            } catch (NoSuchFileException e) {
                this.status = String.format("Error: " +
                        "%s does not exist", e.getFile() );
                announce(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    void decrypt(String selectedFile, String password, String saveFile) {
        if(saveFile.equals("")) {
            this.status = "A save file name must be entered";
            announce(null);
        } else if(password.equals("")) {
            this.status = "A password must be entered in order to decrypt";
            announce(null);
        } else if(selectedFile.equals("")) {
            this.status = "A file must be selected to decrypted";
            announce(null);
        } else {
            this.status = "The decrypted file, " +saveFile+", has been created";
            announce(null);

            try {
                byte [] file = Files.readAllBytes(Paths.get(selectedFile));
                selectedFile = selectedFile.substring(0, selectedFile
                        .lastIndexOf("/"));
                IdentityMatrix l2 = new IdentityMatrix(file.length);
                byte [] afterl2 = l2.convertAndMultiply(file);
                CentralMap cm = new CentralMap(password.getBytes(), afterl2);
                IdentityMatrix l1 = new IdentityMatrix(file.length);
                byte [] plaintext = l1.convertAndMultiply(cm.decrypt());
                writeFile(selectedFile, "/" + saveFile, plaintext);
            } catch (NoSuchFileException e) {
                this.status = String.format("Error: " +
                        "%s does not exist", e.getFile() );
                announce(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String getStatus() {
        return this.status;
    }

    private void writeFile(
            String directoryPath,
            String filePath,
            byte [] str_output)
            throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter output = new PrintWriter(directoryPath + filePath, "UTF-8");
        for (byte b:
             str_output) {
            output.write((char) b);
        }
        output.flush();

    }

    public void announce(String arg) {
        setChanged();
        notifyObservers(arg);
    }
}
