
public class IdentityMatrix{
    private byte ba [][];

    public IdentityMatrix(int i) throws IllegalArgumentException {
        this.ba = new byte [i][i];
    }

    public void populate() {
        for(int i = 0; i < ba.length; i++) {
            this.ba[i][i] = (byte) 1;
        }
    }

    public byte [][] getIdentity() {
        return this.ba;
    }
}
