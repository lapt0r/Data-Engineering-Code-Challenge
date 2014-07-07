//Kurt Boberg
//Data Engineering Code Challenge

package blackjack;

public class Player{
    
    private Boolean dealer;
    private Boolean stay;
    private int availableChips;
    private int score;
    private int numAces;
    private int[] hand;
    private int numCards;

    public Player(){

        dealer = false;
        availableChips = 100;
        hand = new int[11];
        numAces = 0;
        numCards = 0;
        stay = false;

    }

    public Player(int chips){

        dealer = false;
        availableChips = chips;
        hand = new int[11];
        numAces = 0;
        numCards = 0;
        stay = false;

    }

    public Player(Boolean d, int chips){

        dealer = d;
        availableChips = chips;
        hand = new int[11];
        numAces = 0;
        numCards = 0;
        stay = false;
    }

    public void printHand(){

        String handString = "";

        for(int i = 0; i < 11; i++){
            if(hand[i] > 0){
                handString = handString + hand[i] + " ";
            }
        }

        System.out.println("Your current hand values are: " + handString);
    }

    public Boolean isDealer(){

        return dealer;
    }

    public void setChips(int chips){

        availableChips = chips;
    }

    public void addChips(int chips){

        availableChips = availableChips + chips;
    }

    public int getChips(){

        return availableChips;
    }

    public void setScore(int s){

        score = s;
    }

    public int getScore(){

        return score;
    }

    public void getAce(){

        numAces = numAces + 1;
    }

    public int getNumAces(){

        return numAces;
    }

    public int[] getHand(){

        return hand;
    }

    public void wipeHand(){

        numAces = 0;
        numCards = 0;
        hand = new int[11];
        stay = false;
    }

    public void wipeScore(){

        score = 0;
    }

    public void giveCard(int value){

        hand[numCards] = value;
        numCards = numCards + 1;
        if(value == 1){
            numAces++;
        }
    }

    public void stayHand(){

        stay = true;
    }

    public Boolean isFinished(){

        return stay;
    }

    public int computeMaximumHand(){

        //compute non-ace hand value

        int value = 0;

        for(int i = 0; i < numCards; i++){

            //skip aces for now, those are handled later

            if(hand[i] == 1){
                //do nothing
            }
            else{
                //debug code
                //System.out.println("current best score is " + value);
                value = value + hand[i];

                //debug code
                //System.out.println("new best score is " + value);
            }
        }
        
        //more debug code
        //System.out.println("Total of non-ace cards in hand is " + value);

        //compute ace values

        for(int j = 0; j < numAces; j++){

            if(value + 11 < 22){
                value = value + 11;
            }
            else{
                value = value + 1;
            }
        }

        return value;
    }
}