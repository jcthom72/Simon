package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SimonVersion2 extends SimonActivity {
    private final String HighScore = "HighScoreV2.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sequenceAnim = new v2ButtonSequenceTask();

        ((TextView) findViewById(R.id.SimonTevtView)).setText("Simon (Reverse)");
    }

    @Override
    protected void playSequenceAnimation(){
        if (sequenceAnim == null) {
            sequenceAnim = new v2ButtonSequenceTask();
        }
        sequenceAnim.execute();
    }

    protected class v2ButtonSequenceTask extends ButtonSequenceTask{
        public v2ButtonSequenceTask(){
            super();
        }

        @Override
        protected Void doInBackground(Void... params){
            Queue<SimonGameEngine.Button> sequence = game.getPattern();
            Iterator<SimonGameEngine.Button> sequenceItr = ((LinkedList<SimonGameEngine.Button>) sequence).descendingIterator();
            SimonGameEngine.Button button = null;
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

                /*sequenceItr is a descending itr, so we go play animation sequence in reverse*/
                try{
                    Thread.sleep(1000); //at least one second of delay between each bling
                    publishProgress(button);
                    retry = false;
                } catch(InterruptedException e){
                    retry = true;
                }
            }
            return null;
        }
    }
}
