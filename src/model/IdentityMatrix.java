package model;

public class IdentityMatrix {
    private long size;

    IdentityMatrix(int i) {
        this.size = i;
    }

    public byte[] convertAndMultiply(short[] message) {
        byte[][] convertCol = new byte[message.length][1];
        for (int j = 0; j < message.length; j++) {
            convertCol[j][0] = (byte) message[j];
        }

        byte[][] product = new byte[Integer.valueOf(Long.toString(this.size))][1];
        // iterates through each row in identity matrix
        for (long i = 0; i < size; i++) {
            // goes through each column in identity matrix
            for (long j = 0; j < size; j++) {
                if (i == j) {
                    product[Integer.valueOf(Long.toString(j))][0] +=
                            convertCol[Integer.valueOf(Long.toString(j))][0];
                }
            }
        }

        byte[] afterProduct = new byte[message.length];
        for (int j = 0; j < message.length; j++) {
            afterProduct[j] = product[j][0];
        }
        return afterProduct;
    }
}
