import processing.core.PImage;

import java.util.ArrayList;

public class Suit {
    private final static ArrayList<Suit> suits = new ArrayList<>();
    static {
        new Suit("spades", "assets/spades.png", 's');
        new Suit("hearts", "assets/hearts.png", 'h');
        new Suit("clubs", "assets/clubs.png", 'c');
        new Suit("diamonds", "assets/diamonds.png", 'd');
        new Suit("dots", "assets/dots.png", '.');
        new Suit("crosses", "assets/crosses.png", 'x');
    }
    private final int index;
    PImage image;
    String name;
    String imagePath;
    char key;
    Suit(String n, String i, char k) {
        name = n;
        imagePath = i;
        key = k;
        index = suits.size();
        suits.add(this);
    }
    static Suit getByName(String n) {
        for(Suit s : suits) {
            if(n.equals(s.name)) {
                return s;
            }
        }
        return null;
    }
    static Suit getByKey(char k) {
        for(Suit s : suits) {
            if(k == s.key) {
                return s;
            }
        }
        return null;
    }
    static Suit getNext(Suit s) {
        if(s == null) {
            return suits.size() > 0 ? suits.get(0) : null;
        }
        if(s.index < suits.size() - 1) {
            return suits.get(s.index + 1);
        }
        return null;
    }
    static Suit getPrevious(Suit s) {
        if(s == null) {
            return suits.size() > 0 ? suits.get(suits.size() - 1) : null;
        }
        if(s.index > 0) {
            return suits.get(s.index - 1);
        }
        return null;
    }
    void load(OhHellScoreboardV2 sc) {
        image = sc.loadImage(imagePath);
    }
    static void loadAll(OhHellScoreboardV2 sc) {
        for(Suit s : suits) {
            s.load(sc);
        }
    }
}
