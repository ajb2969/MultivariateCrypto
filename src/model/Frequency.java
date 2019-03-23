package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class Frequency {
  private HashMap<Character, Integer> dict1;
  private HashMap<Character, Integer> dict2;

  Frequency(byte[] t1, byte[] t2) {
    this.dict1 = new HashMap<>();
    this.dict2 = new HashMap<>();
    compareFrequency(t1, t2);
  }

  private void compareFrequency(byte[] t1, byte[] t2) {
    for (byte b : t1) {
      put(dict1, (char) b);
    }

    for (byte b : t2) {
      put(dict2, (char) b);
    }
  }

  private void put(HashMap<Character, Integer> dict, Character key) {
    if (dict.containsKey(key)) {
      dict.put(key, dict.get(key) + 1);
    } else {
      dict.put(key, 1);
    }
  }

  public List<LetterFrequency> getFrequencies() {
    ArrayList<LetterFrequency> frequencyArrayList = new ArrayList<>();
    Iterator<Character> iter1 = this.dict1.keySet().iterator();
    Iterator<Character> iter2 = this.dict2.keySet().iterator();
    while (iter1.hasNext() || iter2.hasNext()) {
      if (iter1.hasNext()) {
        Character c1 = iter1.next();
        frequencyArrayList.add(
            new LetterFrequency(c1, this.dict1.get(c1), this.dict2.getOrDefault(c1, 0)));
      }

      if (iter2.hasNext()) {
        Character c2 = iter2.next();
        frequencyArrayList.add(
            new LetterFrequency(c2, this.dict1.getOrDefault(c2, 0), this.dict2.get(c2)));
      }
    }
    Collections.sort(frequencyArrayList);
    return frequencyArrayList;
  }
}
