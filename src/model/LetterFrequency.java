package model;

public class LetterFrequency implements Comparable<LetterFrequency> {
    private Character c;
    private int freq1;
    private int freq2;

    LetterFrequency(Character c, int freq1, int freq2) {
        this.c = c;
        this.freq1 = freq1;
        this.freq2 = freq2;
    }

    public Character getC() {
        return c;
    }

    public int getFreq1() {
        return freq1;
    }

    public int getFreq2() {
        return freq2;
    }

    @Override
    public int compareTo(LetterFrequency o) {
        if (o.getC() > this.getC()) {
            return o.getC() < this.getC() ? 1 : -1;
        } else {
            return o.getC() < this.getC() ? 1 : 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
