package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by sbickel20 on 2/24/17.
 */

public class SimonGameEngine {
    /* holds a randomly generated sequence of pressed buttons */
    private Queue<simonButtons> RandomSequences;

    /* holds the user's sequence choide */
    private Queue<simonButtons> playerSequence;

    /* enum holds the possible button choices */
    public enum simonButtons{UPPER_RIGHT, UPPER_LEFT, LOWER_RIGHT, LOWER_LEFT}
    private int level; // indicates the level
    private int validationCode;  /* indicates whether or not the player pressed the
                                            correct sequence */

    SimonGameEngine(){
        RandomSequences = new LinkedList<>();
        playerSequence = new LinkedList<>();
        level = 0;
        validationCode = 0;
    }

    // playGame method starts when the app is first used or resumed
    public void playGame(){
        Random random = new Random();
        level++;
        for (int x = 0; x < level; x++){
            int seqButton = random.nextInt(4) + 1;

            switch (seqButton){
                case 1:
                    RandomSequences.add(simonButtons.UPPER_RIGHT);
                    break;
                case 2:
                    RandomSequences.add(simonButtons.UPPER_LEFT);
                    break;
                case 3:
                    RandomSequences.add(simonButtons.LOWER_LEFT);
                    break;
                case 4:
                    RandomSequences.add(simonButtons.LOWER_RIGHT);
                    break;
            }
        }
    }

    // addSequence method is used to add a user choice to a queue
    public void addSequence(simonButtons userButtonChoice){
        playerSequence.add(userButtonChoice);
    }

    // verify method is used to determine if a player has pressed the correct sequence
    public int verify(){
        for (int a = 0; a < level; a++){
            // compares each corresponding element of each queue to determine if equal
            if (playerSequence.remove() == RandomSequences.remove()){
                continue; // if they are, continue to next iteration of the loop
            }else{
                validationCode = 1; /* if they are not equal, send an error code and break out
                                            of the loop */
                break;
            }
        }
        playerSequence.clear();
        RandomSequences.clear();
        return validationCode;
    }

    // Player class represents a player's score
    class Player{
        private int score;

        Player(){
            score = 0;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

}
