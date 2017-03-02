package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Queue;

public class SimonVersion3 extends SimonActivity {
    private final String HighScore = "HighScoreV3.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sequenceAnim = new v3ButtonSequenceTask();
    }

    protected class v3ButtonSequenceTask extends ButtonSequenceTask{
        public v3ButtonSequenceTask(){
            super();
        }

        protected Void doInBackground(Void... params){
            Queue<SimonGameEngine.Button> sequence = game.getPattern();
            SimonGameEngine.Button[] sequenceArr = (SimonGameEngine.Button[]) sequence.toArray();
            SimonGameEngine.Button button;
            SimonGameEngine.Button swappedButton = null;


            for(int i = 0; i < sequence.size(); i++){
                button = sequenceArr[i];
			    /*even if thread is interrupted, sequence animation will resume
			    once thread is resumed*/
                    switch(button){
                        case TOP_LEFT: swappedButton = SimonGameEngine.Button.BOTTOM_RIGHT;
                            break;
                        case TOP_RIGHT: swappedButton = SimonGameEngine.Button.BOTTOM_LEFT;
                            break;
                        case BOTTOM_LEFT: swappedButton = SimonGameEngine.Button.TOP_RIGHT;
                            break;
                        case BOTTOM_RIGHT: swappedButton = SimonGameEngine.Button.TOP_LEFT;
                            break;
                    }
                publishProgress(swappedButton);
                try{
                    Thread.sleep(1000); //at least one second of delay between each bling
                    publishProgress(button);
                } catch(InterruptedException e){
                }
            }
            return null;
        }
    }
}