//Kurt Boberg
//Data Engineering Code Challenge

package blackjack;
import java.io.*;
import java.util.*;

public class Blackjack
{

    Player dealer;
    Player humanPlayer;
    BufferedReader reader;
    int pool = 0;
    int lastBet = 0;

    public Blackjack(){

        dealer = new Player(true,1000000);
        humanPlayer = new Player();
    }

    private int getNextCard(){

        int nextCard;
        Random r = new Random();
        nextCard = r.nextInt(12) + 1;
        if(nextCard > 10) {
            nextCard = 10;
        }
        return nextCard;
    }

    private int playHand(Player player){

        reader = new BufferedReader(new InputStreamReader(System.in));
        int nextCard;
        int playerScore = 0;
        String input = "";
        int currentScore = 0;
        int bet = 0;
        int chipsLeft = 0;
        Boolean isStaying = false;

        if(player.isDealer()){
            System.out.println("Dealer's turn.");
        }
        else{
            //debug code
            //System.out.println("Dealer's hand: " + Arrays.toString(dealer.getHand()));
            System.out.println("Player's turn.");
        }

        while(!player.isFinished()){

            nextCard = getNextCard();
            System.out.println("Next card value is " + nextCard);
            player.giveCard(nextCard);
            playerScore = player.computeMaximumHand();
            if(playerScore > 21){
                return playerScore;
            }
            else{
                if(player.isDealer()){
                    if(playerScore > 17 && playerScore < 22){
                        player.stayHand();
                        break;
                    }
                    //dealer always matches previous bet  (to be improved)
                    pool = pool + lastBet;
                }
                else{
                    player.printHand();
                    while(true){
                        System.out.println("Stay? y/n: ");
                        try{
                            input = reader.readLine();
                        }
                        catch(IOException e){
                            System.out.println("Unhandled IO Exception.");
                        }
                        if(input.equals("y")){
                            player.stayHand();
                            isStaying = true;
                            break;
                        }
                        else if(input.equals("n")){
                            break;
                        }
                        else{
                            System.out.println("Invalid command.");
                        }
                    }
                    while(!isStaying){
                        System.out.println("How much would you like to bet?\nYou have " + player.getChips() + " chips available and must bet at least 1 chip.");
                        try{
                            input = reader.readLine();
                            bet = Integer.parseInt(input);
                            lastBet = bet;
                        }
                        catch(IOException e){
                            System.out.println("Unhanded IO Exception.");
                        }
                        if(bet < 1){
                            System.out.println("Must bet at least 1 chip.");
                        }
                        else if(bet > player.getChips()){
                            System.out.println("Bet exceeds current chip pool.  Betting all remaining chips.");
                            pool = pool + player.getChips();
                            player.setChips(0);
                            break;
                        }
                        else{
                            chipsLeft = player.getChips() - bet;
                            System.out.println("Bet of " + input + " accepted.  You have " + chipsLeft + " chips remaining.");
                            pool = pool + bet;
                            player.setChips(chipsLeft);
                            break;
                        }
                    }
                }
            }
        }

        return playerScore;
    }

    public void newGame(){

        reader = new BufferedReader(new InputStreamReader(System.in));
        Boolean keepPlaying = true;
        Boolean continueHand = true;
        Boolean inputLock = true;
        int nextCard;
        int dealerScore;
        int playerScore;
        String input = "";

        while(keepPlaying){

            dealerScore = 0;
            playerScore = 0;
            pool = 0;

            if(humanPlayer.getChips() < 1){
                System.out.println("Player's chips have been exhausted.");
                keepPlaying = false;
                break;
            }
            else if(dealer.getChips() < 1){
                System.out.println("Dealer's chips have been exhausted.  Player wins.");
                keepPlaying = false;
                break;
            }
            else{

                //Player plays first
                playerScore = playHand(humanPlayer);
                System.out.println("Player's hand totals " + playerScore);
                dealerScore = playHand(dealer);
                System.out.println("Dealer's hand totals " + dealerScore);
                

                if(dealerScore > 21){
                    if(playerScore < 22){
                        System.out.println("Dealer busts.  Player wins pool of " + pool + " with " + playerScore + "!");
                        humanPlayer.addChips(pool);
                    }
                    else{
                        System.out.println("Double bust. Draw. Pool split between players.");
                        dealer.addChips(pool/2);
                        humanPlayer.addChips(pool/2);
                    }
                }
                else{
                    if(playerScore > 21){
                        System.out.println("Player busts.  Dealer wins pool of " + pool + " with " + dealerScore + ".");
                        dealer.addChips(pool);
                    }
                    else if(playerScore > dealerScore){
                        System.out.println("Player wins pool of " + pool + " with a score of " + playerScore + " to dealer's " + dealerScore + ".");
                        humanPlayer.addChips(pool);
                    }
                    else{
                        System.out.println("Dealer wins pool of " + pool + " with score of " + dealerScore + " to player's " + playerScore + ".");
                        dealer.addChips(pool);
                    }
                }
                

                //Buggy, cutting for timely deployment

                while(inputLock){

                    System.out.println("You have " + humanPlayer.getChips() + " chips remaining. Keep playing? y/n: ");
                    try{
                        input = reader.readLine();
                    }
                    catch(IOException e){
                        System.out.println("Unhandled IO Exception.");
                    }

                    if(input.equals("y")){

                        humanPlayer.wipeHand();
                        humanPlayer.wipeScore();
                        dealer.wipeHand();
                        dealer.wipeScore();
                        inputLock = false;
                        System.out.println("Beginning new round...");
                    }
                    else if(input.equals("n")){
                        keepPlaying = false;
                        break;
                    }
                    else{
                        System.out.println("Invalid command.");
                    }
                }

                inputLock = true;
                

                //keepPlaying = false;
            }
        }

        System.out.println("Game terminated.");
    }
}


