import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Arrays.*;

public class Deck extends Thread {
    //deck has one field, all the cards in the deck.
    Card[] cards = new Card[52];

    public Deck() {
        //builds a standard deck of cards, face cards are just numbers, so I can keep everything as an int
        String[] suits = new String[]{"Spades", "Clubs", "Diamonds", "Hearts"};
        int n = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 15; j++) {
                this.cards[n] = new Card(suits[i], j);
                n++;
            }
        }
    }

    //this swaps two cards in the deck, to shuffle the cards I needed ot swap cards easily.
    public void swap(int i, int r) {
        Card temp = new Card(this.cards[i].getSuit(), this.cards[i].getRank());
        this.cards[i] = this.cards[r];
        this.cards[r] = temp;
    }

    //shuffles the deck by swapping cards a lot
    public void shuffle() {
        for (int i = 51; i > 1; i--) {
            int r = ThreadLocalRandom.current().nextInt(0, i + 1);
            swap(i, r);
        }
    }

    //this is the main part of this, it checks the first five cards for what poker hand it makes
    public synchronized int check() {
        int[] ranks = new int[5];
        String[] suits = new String[5];
        int[] wheelStraight = {2,3,4,5,14}; // needed for later when validating straights.

        //I have two lists one of the ranks of the cards and the other is their suit.
        //this is so I can work with them separately for flushes and straights.
        for (int i = 0; i < 5; i++) {
            ranks[i] = this.cards[i].getRank();
            suits[i] = this.cards[i].getSuit();
        }

        //checking for a straight of any kind by seeing if the card in front is the next number.
        sort(ranks);
        int straightCheck = 0;
        for (int i = 0; i < 4; i++) {
            if (ranks[i] == (ranks[i + 1] - 1)) {
                straightCheck++;
            }
        }

        //checks for a flush and counts value occurrences, so I only need one for loop the goes through values once.
        boolean flushCheck = false;
        //keeps count of the kinds of suits for a flush
        //[S, H, C, D] is the order of the suits in the array
        int[] suitCount = new int[4];
        List<Integer> numCount = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            switch (suits[i]) {
                case ("Spades") -> suitCount[0] = suitCount[0] + 1;
                case ("Hearts") -> suitCount[1] = suitCount[1] + 1;
                case ("Clubs") -> suitCount[2] = suitCount[2] + 1;
                case ("Diamonds") -> suitCount[3] = suitCount[3] + 1;
            }

            int count = 0;
            for (int j = 0; j < 5; j++) {
                if (ranks[i] == ranks[j]) {
                    count++;
                }
            }
            //the sum of this list will tell me what kind of combination of cards I have if.
            numCount.add(count);
        }

        //checking if the flush works
        for (int i = 0; i < 4; i++) {
            if (suitCount[i] > 4) {
                flushCheck = true;
                break;
            }
        }

        //since ace has a rank of both 1 and 14 there are two ways to make it. I gave ace the rank of 14 in this.
        if (straightCheck == 4 || Arrays.equals(ranks, wheelStraight)) {
            if (ranks[0] == 10 && flushCheck) {
                //royal flush
                return 0;
            } else if(flushCheck) {
                //straight flush
                return 1;
            } else {
                //straight
                return 5;
            }
        }

        //flush check
        if (flushCheck) {
            //flush
            return 4;
        }

        //checking for combinations of pairs, sets, quads, and full houses using numCount.
        return switch (numCount.stream().mapToInt(Integer::intValue).sum()) {
            case (17) -> 2; // quads
            case (13) -> 3; // full house
            case (11) -> 6; // set
            case (9) -> 7; // two pair
            case (7) -> 8; // one pair
            default -> 9; // high card
        };
    }

    //for testing purposes, so I could run this once and be able to see the hand
    public void showFive() {
        for (int i = 0; i < 5; i++) {
            System.out.println(this.cards[i].show());
        }
    }

    //main program makes ten threads that all go at the same time.
    //each thread checks itself and then add the results to a total.
    public static void main(String[] arg) throws InterruptedException {

        long start = System.nanoTime();
        //number of trials here
        int n = 100000;
        int perThread = n / 10;

        //making all the decks, each thread gets its own deck.
        Deck one = new Deck();
        DeckThread threadOne = new DeckThread();
        Deck two = new Deck();
        DeckThread threadTwo = new DeckThread();
        Deck three = new Deck();
        DeckThread threadThree = new DeckThread();
        Deck four = new Deck();
        DeckThread threadFour = new DeckThread();
        Deck five = new Deck();
        DeckThread threadFive = new DeckThread();
        Deck six = new Deck();
        DeckThread threadSix = new DeckThread();
        Deck seven = new Deck();
        DeckThread threadSeven = new DeckThread();
        Deck eight = new Deck();
        DeckThread threadEight = new DeckThread();
        Deck nine = new Deck();
        DeckThread threadNine = new DeckThread();
        Deck ten = new Deck();
        DeckThread threadTen = new DeckThread();

        //making all the threads.
        threadOne.run(one, perThread);
        threadTwo.run(two, perThread);
        threadThree.run(three, perThread);
        threadFour.run(four, perThread);
        threadFive.run(five, perThread);
        threadSix.run(six, perThread);
        threadSeven.run(seven, perThread);
        threadEight.run(eight, perThread);
        threadNine.run(nine, perThread);
        threadTen.run(ten, perThread);

        //starting all the threads.
        threadOne.start();
        threadTwo.start();
        threadThree.start();
        threadFour.start();
        threadFive.start();
        threadSix.start();
        threadSeven.start();
        threadEight.start();
        threadNine.start();
        threadTen.start();

        //joining all the threads.
        threadOne.join();
        threadTwo.join();
        threadThree.join();
        threadFour.join();
        threadFive.join();
        threadSix.join();
        threadSeven.join();
        threadEight.join();
        threadNine.join();
        threadTen.join();

        //string of possible hands so that I can print them
        String[] hands = new String[] {"Royal Flush", "Straight Flush", "Quads", "Full House", "Flush", "Straight", "Set", "Two Pair", "Pair", "High Card"};

        //prints the totals for each kind of hand and keeps a running total with formatting
        int sum = 0;
        System.out.println(String.format("%-25s", "Hand Type") + String.format("%-15s", "Count") + String.format("%-15s", "Probability"));
        for (int i = 0; i < 10; i++) {
            String prob = (String.valueOf((float) DeckThread.results[i] / (float) n));
            System.out.println(String.format("%-25s","Count of: " + hands[i]) + String.format("%-15s",DeckThread.results[i]) +
                    String.format("%-10s", prob));
            sum += DeckThread.results[i];
        }

        //prints the sum
        System.out.println("Total hands: " + sum);
        long end = System.nanoTime();
        long duration = end - start;
        System.out.println(duration / 1000000);
    }
}


