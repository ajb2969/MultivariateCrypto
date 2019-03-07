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
                int gf = 127;
                byte [] file = Files.readAllBytes(Paths.get(selectedFile));
                selectedFile = selectedFile.substring(0, selectedFile
                        .lastIndexOf("/"));
                IdentityMatrix l1 = new IdentityMatrix(file.length);
                byte [] afterl1 = l1.convertAndMultiply(file);
                
                //build graph to transform y -> z

                IdentityMatrix l2 = new IdentityMatrix(file.length);
                byte [] ciphertext = l2.convertAndMultiply(afterl1);
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
            //start decryption here
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
