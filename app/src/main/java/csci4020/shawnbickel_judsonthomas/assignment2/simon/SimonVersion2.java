package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Queue;

public class SimonVersion2 extends SimonActivity {
    private final String HighScore = "HighScoreV2.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sequenceAnim = new v2ButtonSequenceTask();
    }

    protected class v2ButtonSequenceTask extends ButtonSequenceTask{
        public v2ButtonSequenceTask(){
            super();
        }

        protected Void doInBackground(Void... params){
            Queue<SimonGameEngine.Button> sequence = game.getPattern();
            SimonGameEngine.Button[] sequenceArr = (SimonGameEngine.Button[]) sequence.toArray();
            SimonGameEngine.Button button;
            for(int i = sequence.size() - 1; i >= 0; i--){
                button = sequenceArr[i];
			    /*even if thread is interrupted, sequence animation will resume
			    once thread is resumed*/
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
