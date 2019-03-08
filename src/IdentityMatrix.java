public class IdentityMatrix {
  private short[][] ba;

  public IdentityMatrix(int i) {
    this.ba = new short[i][i];
    this.populate();
  }

  public void populate() {
    for (int i = 0; i < ba.length; i++) {
      this.ba[i][i] = (short) 1;
    }
  }

  public short[] convertAndMultiply(short[] message) {
    short[][] convertCol = new short[message.length][1];
    for (int j = 0; j < message.length; j++) {
      convertCol[j][0] = message[j];
    }

    short[][] product = new short[this.ba.length][1];
    // iterates through each row in identity matrix
    for (int i = 0; i < this.ba.length; i++) {
      // goes through each column in identity matrix
      for (int j = 0; j < this.ba[0].length; j++) {
        product[j][0] += this.ba[i][j] * convertCol[j][0];
      }
    }

    short[] afterProduct = new short[message.length];
    for (int j = 0; j < message.length; j++) {
      afterProduct[j] = product[j][0];
    }
    return afterProduct;
  }
}
