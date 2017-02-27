package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

    private Simon simon;
    public Player player;

    private boolean inSession;

    public SimonGameEngine(){
        round = 0;
        inSession = false;
        sequence = new LinkedList<Button>();
        simon = new Simon();
        player = new Player();
    }

    /*if a round is not currently in session, advances the game to the next round;
    otherwise, does nothing*/
    public void nextRound(){
        if(!inSession){
            round++;
            sequence.clear(); //ensure sequence is empty
            simon.generateSequence();
        }
    }

    /*if a round is not currently in session and the game has started, starts the current round;
    otherwise, does nothing.
    Note: this method can be used to restart a round multiple times.*/
    public void startRound(){
        if(!inSession && hasStarted()){
            inSession = true;
        }
    }

    /*if a round is not currently in session, resets the game to the initial, unstarted state (round #0);
    otherwise, does nothing*/
    public void endGame(){
        if(!inSession){
            round = 0;
            sequence.clear();
        }
    }

    /*if the game is in its initial state i.e. round = 0 (due to 1. initial creation or 2. a resetGame call),
    starts the game; otherwise, does nothing.
    Note: this method has the same effect as calling nextRound() when game is in its initial state.*/
    public void startGame(){
        if(!inSession && round == 0){
            nextRound();
        }
    }

    /*returns the current round number.
    Note: if the round has ended the returned value will correspond
    to the round number for the last round played. To determine whether the returned
    round number is for the current round or the last round played, call isPlaying().*/
    public int getRound(){
        return round;
    }

    /*returns true if the game has started; false otherwise*/
    public boolean hasStarted(){
        return round != 0;
    }

    /*returns true if the current round is in session; false otherwise*/
    public boolean isPlaying(){
        return inSession;
    }

    /*returns a copy of the current button pattern (pattern and sequence are synonymous here)*/
    public Queue<Button> getPattern(){
        Queue<Button> pattern = new LinkedList<Button>(sequence);
        return pattern;
    }

    class Simon{
        private Random random;

        public Simon(){
            random = new Random();
        }

        /*generates the random button pattern.
        Note: the number of buttons in the pattern sequence is determined by
        the round number (round 1 = 1 button, round 2 = 2 buttons, etc.)*/
        private void generateSequence(){
            int buttonToAdd;
            for(int i = 0; i < round; i++){
                buttonToAdd = random.nextInt(4);
                sequence.add(BUTTONS[random.nextInt(4)]);
            }
        }
    }

    class Player{
        /*player "presses" button "button".*/
        public void press(Button button){
            if(inSession){
                if(button == sequence.remove()){
                    //correct guess
                    if(sequence.isEmpty()){
                        //end round (player won)
                        inSession = false;
                    }
                }

                else{
                    //incorrect guess
                    //end round (player lost)
                    inSession = false;
                }
            }
        }

        /*returns true if the player has lost the round; false otherwise.
        Note: a return value of false does not imply that the player has won;
        if the game has not started yet, or if the round is still in session,
        this method will return false.*/
        public boolean hasLostRound(){
            if(
                    hasStarted() /*game has started*/ &&
                            !inSession /*not currently in a round*/&&
                            !sequence.isEmpty() /*NOT all buttons in sequence were guessed*/){
                return true;
            }
            return false;
        }

        /*returns true if the player has won the round; false otherwise.
        Note: a return value of false does not imply that the player has lost;
        if the game has not started yet, or if the round is still in session,
        this method will return false.*/
        public boolean hasWonRound(){
            if(
                    hasStarted() /*game has started*/ &&
                            !inSession /*not currently in a round*/&&
                            sequence.isEmpty() /*all buttons in sequence were guessed*/){
                return true;
            }
            return false;
        }

        /*returns the player's current score*/
        public int getScore(){
            return getRound();
        }
    }
}