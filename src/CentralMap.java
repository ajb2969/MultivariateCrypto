public class CentralMap {

  private short[] password;
  private short[] plaintext;
  private static final short GALOISFIELD = 127;
  private Line[] lines;
  private Point[] points;

  CentralMap(byte[] password, short[] plaintext) {
    short[] convPassword = new short[password.length];
    int ind = 0;
    for (byte i : password) {
      convPassword[ind] = i;
      ind++;
    }
    this.password = convPassword;

    this.plaintext = plaintext;
    this.lines = new Line[this.plaintext.length];
    this.points = new Point[this.plaintext.length];
    init();
  }

  private void init() {
    for (int i = 0; i < plaintext.length; i++) {
      this.lines[i] = new Line(this.plaintext[i]);
      this.points[i] = new Point(this.plaintext[i]);
    }
  }

  private short modulus(short num, short divisor) {
    return (short) ((num % divisor) + ((num % divisor) < 0 ? divisor : 0));
  }

  private short [] print(int flag){
    if (flag == 0) {
      short[] returnArray = new short[lines.length];
      for (int counter = 0; counter < lines.length; counter++) {
        returnArray[counter] = lines[counter].getB();
      }
      return returnArray;
    } else {
      short[] returnArray = new short[points.length];
      for (int counter = 0; counter < points.length; counter++) {
        returnArray[counter] = points[counter].getB();
      }
      return returnArray;
    }
  }

  public short[] decrypt() {
    int returnVal = 0;
    int r = 1;
    if (password.length % 2 == 0) {
      r = 0;
    }
    for (int i = password.length - 1; i >= 0; i--) {
      if (r == 0) {
        // starts with lines
        short l0 = (short) (points[0].getB() - password[i]);
        lines[0] = new Line(modulus(l0, GALOISFIELD));
        short l1 = (short) (points[1].getB() + points[0].getB() * lines[0].getB());
        lines[1] = new Line(modulus(l1, GALOISFIELD));
        short l2 = (short) (points[2].getB() + points[0].getB() * lines[1].getB());
        lines[2] = new Line(modulus(l2, GALOISFIELD));
        r = 1;
        returnVal = 0;
      } else {
        short p0 = (short) (lines[0].getB() - password[i]);
        points[0] = new Point(modulus(p0, GALOISFIELD));
        short p1 = (short) (lines[1].getB() - points[0].getB() * lines[0].getB());
        points[1] = new Point(modulus(p1, GALOISFIELD));
        short p2 = (short) (lines[2].getB() - points[0].getB() * lines[1].getB());
        points[2] = new Point(modulus(p2, GALOISFIELD));
        r = 0;
        returnVal = 1;
      }
    }
    return print(returnVal);
  }

  public short[] encrypt() {
    // Always start with lines
    // turnary to handle how java does modulus -8783 % GALOISFIELD == -20 instead of 107
    int returnVal = 0;
    for (int i = 0; i < this.password.length; i++) {
      if (i % 2 == 0) {
        short l0 = (short) (points[0].getB() + password[i]);
        lines[0] = new Line(modulus(l0, GALOISFIELD));
        short l1 = (short) (points[1].getB() + points[0].getB() * lines[0].getB());
        lines[1] = new Line(modulus(l1, GALOISFIELD));
        short l2 = (short) (points[2].getB() + points[0].getB() * lines[1].getB());
        lines[2] = new Line(modulus(l2, GALOISFIELD));

        /**
         *
         * for j = 4 to n
         if(j mod4)=3or(j mod4)=2 l(j)=(p(j)+p(1)∗l(j−2)) mod prime
         else
         l(j)=(p(j)+p(j−2)∗l(1)) mod prime
         end
         */

        returnVal = 0;
      } else {
        short p0 = (short) (lines[0].getB() + password[i]);
        points[0] = new Point(modulus(p0, GALOISFIELD));
        short p1 = (short) (lines[1].getB() - points[0].getB() * lines[0].getB());
        points[1] = new Point(modulus(p1, GALOISFIELD));
        short p2 = (short) (lines[2].getB() - points[0].getB() * lines[1].getB());
        points[2] = new Point(modulus(p2, GALOISFIELD));
        returnVal = 1;
      }
    }
    return print(returnVal);
  }
}

class Line {
  private short b;

  Line(short b) {
    this.b = b;
  }

  public short getB() {
    return b;
  }
}

class Point {
  private short b;

  Point(short b) {
    this.b = b;
  }

  public short getB() {
    return b;
  }
}
