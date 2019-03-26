package model;

public class CentralMap {

    private short[] password;
    private byte[] plaintext;
    private static final short GALOISFIELD = 127;
    private Line[] lines;
    private Point[] points;

    CentralMap(byte[] password, byte[] plaintext) {
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

    private short modulus(int num, short divisor) {
        // turnary to handle how java does modulus -8783 % GALOISFIELD == -20 instead of 107
        return (short) ((num % divisor) + ((num % divisor) < 0 ? divisor : 0));
    }

    private short[] print(int flag) {
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
                int l0 = (points[0].getB() - password[i]);
                lines[0] = new Line(modulus(l0, GALOISFIELD));
                int l1 = (points[1].getB() + points[0].getB() * lines[0].getB());
                lines[1] = new Line(modulus(l1, GALOISFIELD));
                int l2 = (points[2].getB() + points[0].getB() * lines[1].getB());
                lines[2] = new Line(modulus(l2, GALOISFIELD));
                cryptIfForLoop();
                r = 1;
                returnVal = 0;
            } else {
                int p0 = (lines[0].getB() - password[i]);
                points[0] = new Point(modulus(p0, GALOISFIELD));
                int p1 = (lines[1].getB() - points[0].getB() * lines[0].getB());
                points[1] = new Point(modulus(p1, GALOISFIELD));
                int p2 = (lines[2].getB() - points[0].getB() * lines[1].getB());
                points[2] = new Point(modulus(p2, GALOISFIELD));
                cryptElseForLoop();
                r = 0;
                returnVal = 1;
            }
        }
        return print(returnVal);
    }

    public short[] encrypt() {
        // Always start with lines

        int returnVal = 0;
        for (int i = 0; i < this.password.length; i++) {
            if (i % 2 == 0) {
                int l0 = (points[0].getB() + password[i]);
                lines[0] = new Line(modulus(l0, GALOISFIELD));
                int l1 = (points[1].getB() + points[0].getB() * lines[0].getB());
                lines[1] = new Line(modulus(l1, GALOISFIELD));
                int l2 = (points[2].getB() + points[0].getB() * lines[1].getB());
                lines[2] = new Line(modulus(l2, GALOISFIELD));
                cryptIfForLoop();
                returnVal = 0;
            } else {
                int p0 = (lines[0].getB() + password[i]);
                points[0] = new Point(modulus(p0, GALOISFIELD));
                int p1 = (lines[1].getB() - points[0].getB() * lines[0].getB());
                points[1] = new Point(modulus(p1, GALOISFIELD));
                int p2 = (lines[2].getB() - points[0].getB() * lines[1].getB());
                points[2] = new Point(modulus(p2, GALOISFIELD));
                cryptElseForLoop();
                returnVal = 1;
            }
        }
        return print(returnVal);
    }

    private void cryptIfForLoop() {
        for (int j = 3; j < this.plaintext.length; j++) {
            if ((modulus((j + 1), (short) 4) == 3) || (modulus((j + 1), (short) 4) == 2)) {
                int lj = (points[j].getB() + points[0].getB() * lines[j - 1].getB());
                lines[j] = new Line(modulus(lj, GALOISFIELD));
            } else {
                int lj = (points[j].getB() + points[j - 1].getB() * lines[0].getB());
                lines[j] = new Line(modulus(lj, GALOISFIELD));
            }
        }
    }

    private void cryptElseForLoop() {
        for (int j = 3; j < this.plaintext.length; j++) {
            if ((modulus((j + 1), (short) 4) == 3) || (modulus((j + 1), (short) 4) == 2)) {
                int pj = (lines[j].getB() - points[0].getB() * lines[j - 1].getB());
                points[j] = new Point(modulus(pj, GALOISFIELD));
            } else {
                int pj = (lines[j].getB() - points[j - 1].getB() * lines[0].getB());
                points[j] = new Point(modulus(pj, GALOISFIELD));
            }
        }
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
