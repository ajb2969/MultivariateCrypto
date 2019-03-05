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
            this.status = "";
            announce(null);
            //start encryption here
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
            this.status = "";
            announce(null);
            //start decryption here
        }
    }

    String getStatus() {
        return this.status;
    }

    public void announce(String arg) {
        setChanged();
        notifyObservers(arg);
    }
}
