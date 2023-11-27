import processing.core.PImage;

public class Suit {
    private static Suit[] suits = {
            new Suit("spades", "assets/spades.png"),
            new Suit("hearts", "assets/hearts.png"),
            new Suit("clubs", "assets/clubs.png"),
            new Suit("diamonds", "assets/diamonds.png"),
            new Suit("dots", "assets/dots.png"),
            new Suit("crosses", "assets/crosses.png"),
    };
    PImage image;
    String name;
    String imagePath;
    Suit(String n, String i) {
        name = n;
        imagePath = i;
    }
    static Suit getByName(String n) {
        for(Suit s : suits) {
            if(n.equals(s.name)) {
                return s;
            }
        }
        return null;
    }
    static Suit getNext(Suit s) {
        if(s == null && suits.length > 0) {
            return suits[0];
        }
        for(int i = 0; i < suits.length; i++) {
            if(suits[i].equals(s) && i + 1 < suits.length) {
                return suits[i + 1];
            }
        }
        return null;
    }
    static Suit getPrevious(Suit s) {
        if(s == null && suits.length > 0) {
            return suits[suits.length - 1];
        }
        for(int i = 0; i < suits.length; i++) {
            if(suits[i].equals(s) && i - 1 > 0) {
                return suits[i - 1];
            }
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
    static void setList(Suit[] s) {
        suits = s;
    }
}
