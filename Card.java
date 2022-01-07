public class Card {
    public String suit;
    public int rank;

    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }
    public String getSuit() {
        return this.suit;
    }
    public int getRank() {
        return this.rank;
    }
    public String show(){
        return this.rank + " of " + this.suit;
    }
}
