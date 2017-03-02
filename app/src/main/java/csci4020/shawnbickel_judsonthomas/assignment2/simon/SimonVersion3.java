package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SimonVersion3 extends SimonActivity {
    private final String HighScore = "HighScoreV3.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sequenceAnim = new v3ButtonSequenceTask();

        ((TextView) findViewById(R.id.SimonTevtView)).setText("Simon (Opposite)");
    }

    @Override
    protected void playSequenceAnimation(){
        if (sequenceAnim == null) {
            sequenceAnim = new v3ButtonSequenceTask();
        }
        sequenceAnim.execute();
    }

    protected class v3ButtonSequenceTask extends ButtonSequenceTask{
        public v3ButtonSequenceTask(){
            super();
        }

        @Override
        protected Void doInBackground(Void... params){
            Queue<SimonGameEngine.Button> sequence = game.getPattern();
            Iterator<SimonGameEngine.Button> sequenceItr = ((LinkedList<SimonGameEngine.Button>) sequence).iterator();
            SimonGameEngine.Button button = null;
            SimonGameEngine.Button swappedButton = null;
            boolean retry = false;

            while(sequenceItr.hasNext()){
                if(!retry) {
                    /*if we do not need to retry to bling the previous button (i.e. if the
                    * previous button was not interrupted before it blinged) then get the next button;
                    * otherwise, we will try to bling the same button again*/
                    button = sequenceItr.next();
                }
			    /*even if thread is interrupted, sequence animation will resume
			    once thread is resumed*/

                /*output the diagonal/reflected buttons*/
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
                try{
                    Thread.sleep(1000); //at least one second of delay between each bling
                    publishProgress(swappedButton);
                    retry = false;
                }
                catch(InterruptedException e){
                    retry = true;
                }
            }
            return null;
        }
    }
}