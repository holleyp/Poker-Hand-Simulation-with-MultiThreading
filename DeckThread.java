public class DeckThread extends Thread{
    static int[] results = new int[10];

    public void run(Deck deck, int n) {
        for (int i = 0; i < n; i++) {
            //each deck is made individually so all each thread needs to do is shuffle it every time instead of make the deck everytime
            deck.shuffle();
            int result = deck.check();
            results[result] = results[result] + 1;
        }
    }

}
