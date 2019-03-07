
public class IdentityMatrix{
    private byte ba [][];

    public IdentityMatrix(int i) throws IllegalArgumentException {
        this.ba = new byte [i][i];
        this.populate();
    }

    public void populate() {
        for(int i = 0; i < ba.length; i++) {
            this.ba[i][i] = (byte) 1;
        }
    }

    public byte[] convertAndMultiply(byte [] message) {
        byte [][] convertCol = new byte[message.length][1];
        for(int j = 0; j<message.length; j++) {
            convertCol[j][0] = message[j];
        }

        byte [][] product = new byte [this.ba.length][1];
        //iterates through each row in identity matrix
        for(int i = 0; i < this.ba.length; i++) {
            //goes through each column in identity matrix
            for(int j = 0; j < this.ba[0].length; j++) {
                product[j][0] += this.ba[i][j] * convertCol[j][0];
            }
        }

        byte [] afterProduct = new byte[message.length];
        for(int j = 0; j<message.length; j++) {
            afterProduct[j] = product[j][0];
        }
        return afterProduct;
    }
}
