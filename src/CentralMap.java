public class CentralMap {

    private byte [] password;
    private byte [] plaintext;
    final int GALOIS_FIELD = 127;
    Line [] lines;
    Point [] points;
    public CentralMap(byte [] password, byte [] plaintext) {
        this.password = password;
        this.plaintext = plaintext;
        this.lines = new Line[this.password.length];
        this.points = new Point[this.password.length];

    }

    private void init() {
        for(int i = 0; i < plaintext.length; i++) {
            this.lines[i] = new Line(this.plaintext[i]);
            this.points[i] = new Point(this.plaintext[i]);
        }
    }

    public byte [] encrypt() {
        //Always start with lines
        for(int i = 0; i< password.length; i++) {
            if(i % 2 == 0) {
                this.lines[0] = new Line(
                        (byte)((this.points[0].getB() +
                                this.password[i]) %
                                GALOIS_FIELD)
                );
                this.lines[1] = new Line(
                        (byte) ((this.points[1].getB() +
                                 this.points[0].getB() *
                                 this.lines[0].getB()) %
                                 this.GALOIS_FIELD)
                );
                this.lines[2] = new Line((byte)((this.points[2].getB() +
                                            this.points[0].getB() *
                                            this.lines[1].getB()) %
                                            this.GALOIS_FIELD)
                );
                for(int j = 3; j < this.password.length; j++) {
                    if(j % 4 == 3 || j % 4 == 2)
                        this.lines[j] = new Line((byte)((this.points[j].getB() +
                                                    this.points[0].getB() *
                                                    this.lines[j - 2].getB()) %
                                                    GALOIS_FIELD)
                        );
                    else
                        this.lines[j] = new Line((byte)((this.points[j].getB() +
                                                        this.points[j-2].getB() *
                                                        this.lines[0].getB()) %
                                                        GALOIS_FIELD));
                }
            } else {
                this.points[0] = new Point(
                        (byte)((this.lines[0].getB() +
                                this.password[i]) %
                                GALOIS_FIELD)
                );
                this.points[1] = new Point(
                        (byte) ((this.lines[1].getB() -
                                this.points[0].getB() *
                                this.lines[0].getB()) %
                                this.GALOIS_FIELD)
                );
                this.points[2] = new Point((byte)((this.points[2].getB() -
                                                   this.points[0].getB() *
                                                   this.lines[1].getB()) %
                                                   this.GALOIS_FIELD)
                );
                for(int j = 3; j < this.password.length; j++) {
                    if(j % 4 == 3 || j % 4 == 2)
                        this.points[j] = new Point((byte)(
                                (this.lines[j].getB() -
                                 this.points[0].getB() *
                                 this.lines[j - 2].getB()) %
                                 GALOIS_FIELD)
                        );
                    else
                        this.points[j] = new Point((byte)(
                                (this.lines[j].getB() -
                                 this.points[j-2].getB() *
                                 this.lines[0].getB()) %
                                 GALOIS_FIELD)
                        );
                }
            }
        }
        if(this.password.length % 2 == 0) {
            byte [] ciphertext = new byte[this.points.length];
            int i = 0;
            for (Point p:
                 this.points) {
                ciphertext[i] = p.getB();
                i++;
            }
            return ciphertext;
        } else {
            byte [] ciphertext = new byte[this.lines.length];
            int i = 0;
            for (Line p:
                    this.lines) {
                ciphertext[i] = p.getB();
                i++;
            }
            return ciphertext;
        }
    }
}

class Line {
    private byte b;
    public Line(byte b) {
        this.b = b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public byte getB() {
        return b;
    }
}

class Point {
    private byte b;
    public Point(byte b) {
        this.b = b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public byte getB() {
        return b;
    }
}
