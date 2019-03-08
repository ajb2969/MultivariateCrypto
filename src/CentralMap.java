public class CentralMap {

  private short[] password;
  private short[] plaintext;
  static final int GALOISFIELD = 127;
  Line[] lines;
  Point[] points;

  public CentralMap(byte[] password, short[] plaintext) {
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

  public short[] decrypt() {
    for(int i = password.length -1; i>=0; i--) {
      
    }
    return new short[password[0]];
  }

  public short[] encrypt() {
    // Always start with lines
    //turnary to handle how java does modulus -8783 % GALOISFIELD == -20 instead of 107
    int returnVal = 0;
    for (int i = 0; i < this.password.length; i++) {
      if (i % 2 == 0) {
        short l0 = (short) (points[0].getB() + password[i]);
        lines[0] =
            new Line((short) ((l0 % GALOISFIELD) + ((l0 % GALOISFIELD) < 0 ? GALOISFIELD : 0)));
        short l1 = (short) (points[1].getB() + points[0].getB() * lines[0].getB());
        lines[1] =
            new Line((short) ((l1 % GALOISFIELD) + ((l1 % GALOISFIELD) < 0 ? GALOISFIELD : 0)));
        short l2 = (short) (points[2].getB() + points[0].getB() * lines[1].getB());
        lines[2] =
            new Line((short) ((l2 % GALOISFIELD) + ((l2 % GALOISFIELD) < 0 ? GALOISFIELD : 0)));
        returnVal = 0;
      } else {
        short p0 = (short) (lines[0].getB() + password[i]);
        points[0] =
            new Point((short) ((p0 % GALOISFIELD) + ((p0 % GALOISFIELD) < 0 ? GALOISFIELD : 0)));
        short p1 = (short) (lines[1].getB() - points[0].getB() * lines[0].getB());
        points[1] =
            new Point((short) ((p1 % GALOISFIELD) + ((p1 % GALOISFIELD) < 0 ? GALOISFIELD : 0)));
        short p2 = (short) (lines[2].getB() - points[0].getB() * lines[1].getB());
        points[2] =
            new Point((short) ((p2 % GALOISFIELD) + ((p2 % GALOISFIELD) < 0 ? GALOISFIELD : 0)));
        returnVal = 1;
      }
    }
    if (returnVal == 0) {
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
}

class Line {
  private short b;

  public Line(short b) {
    this.b = b;
  }

  public void setB(short b) {
    this.b = b;
  }

  public short getB() {
    return b;
  }
}

class Point {
  private short b;

  public Point(short b) {
    this.b = b;
  }

  public short getB() {
    return b;
  }
}
