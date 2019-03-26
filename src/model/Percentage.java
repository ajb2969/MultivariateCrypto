package model;

import java.util.Random;

class Percentage {
    private short [] text;
    private float percent;
    Percentage(short[] text, float percentage) {
        this.text = text;
        this.percent = percentage;
        diff();
    }

    private void diff() {
        double numChange = Math.floor(this.text.length * this.percent);
        int counter = 0;
        Random r = new Random();
        r.setSeed(31);
        int index = 0;
        while(counter != numChange) {
            if(r.nextBoolean()) {
                this.text[index] = (short)r.nextInt(127);
                counter+=1;
            }

            if (index == this.text.length) {
                index = 0;
            } else {
                index++;
            }
        }
    }

    public short[] getText() {
        return text;
    }

    public float getPercent() {
        return percent;
    }

    public float getDiff(byte[] ciphertext) {
        int counter = 0;
        for(int i = 0; i < ciphertext.length; i++) {
            if(ciphertext[i] == this.text[i]) {
                counter++;
            }
        }

        return 100 - (100 * ((float)counter / ciphertext.length));
    }
}
