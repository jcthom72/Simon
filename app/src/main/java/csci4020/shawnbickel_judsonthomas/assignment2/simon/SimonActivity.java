package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by judson on 2/26/2017.
 */

//specific simon version activities will inherit from SimonActivity
public class SimonActivity extends AppCompatActivity{
    protected SimonGameEngine game;
    protected ButtonSequenceTask sequenceAnim;
    protected FailureButtonSequenceTask failureAnim;
    protected Handler blingHandler;
    protected Runnable blingRun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize game object
        game = new SimonGameEngine();

        //initialize ButtonSequenceTask for button pattern animation
        sequenceAnim = new ButtonSequenceTask();
        failureAnim = new FailureButtonSequenceTask();

        //initialize bling handler for posting delayed bling message to UI thread
        blingHandler = new Handler();
    }

    protected void gamePressEvent(ImageView buttonImagePressed){
        if(game.isPlaying()){
            blingButton(buttonImagePressed, 200);
            game.player.press(imageViewToButton(buttonImagePressed.getId()));

            if(!game.isPlaying()){
			/*previous press ended the round; determine if round was won or lost*/

                if(game.player.hasWonRound()){
                    //trigger next round event
                    nextRoundEvent();
                }

                else /*player lost*/{
                    //trigger game end event
                    gameEndEvent();
                }
            }
        }
    }

    protected void updateScoreText(){
        ((TextView) findViewById(R.id.HighScore)).setText("" + game.player.getScore());
    }

    protected void nextRoundEvent(){
        game.nextRound();
        updateScoreText();

        //play button sequence
        if(sequenceAnim == null){
            sequenceAnim = new ButtonSequenceTask();
        }
        sequenceAnim.execute();
    }

    protected void gameEndEvent(){
        //save high score somewhere
        updateScoreText();

        game.endGame();
        if(failureAnim == null){
            failureAnim = new FailureButtonSequenceTask();
        }
        failureAnim.execute();
    }

    protected void gameStartEvent(){
        game.startGame();
        updateScoreText();

        if(sequenceAnim == null){
            sequenceAnim = new ButtonSequenceTask();
        }
        sequenceAnim.execute();
    }

    /*function to map ImageViews for buttons to their corresponding button enum value;
    maps buttons from GUI to logical game*/
    //final because it should not be able to be overriden by simon version subclasses
    final protected SimonGameEngine.Button imageViewToButton(int ID){
        switch(ID){
            case R.id.top_left_button: return SimonGameEngine.Button.TOP_LEFT;
            case R.id.top_right_button: return SimonGameEngine.Button.TOP_RIGHT;
            case R.id.bottom_left_button: return SimonGameEngine.Button.BOTTOM_LEFT;
            case R.id.bottom_right_button: return SimonGameEngine.Button.BOTTOM_RIGHT;
            default: return null;/*invalid ID; throw exception, etc.*/
        }
    }

    /*function to map button enum values to the imageview to their corresponding GUI imageview;
    maps buttons from logical game to GUI*/
    final protected int buttonToImageView(SimonGameEngine.Button button){
        switch(button){
            case TOP_LEFT: return R.id.top_left_button;
            case TOP_RIGHT: return R.id.top_right_button;
            case BOTTOM_LEFT: return R.id.bottom_left_button;
            case BOTTOM_RIGHT: return R.id.bottom_right_button;
            default: return 0; /*error has occurred*/
        }
    }

    final protected int getNormalImageId(int ID){
        switch(ID){
            case R.id.top_left_button: return R.drawable.top_left;
            case R.id.top_right_button: return R.drawable.top_right;
            case R.id.bottom_left_button: return R.drawable.bottom_left;
            case R.id.bottom_right_button: return R.drawable.bottom_right;
            default: return 0; /*error*/
        }
    }

    final protected int getBlingImageId(int ID){
        switch(ID){
            case R.id.top_left_button: return R.drawable.top_left_bling;
            case R.id.top_right_button: return R.drawable.top_right_bling;
            case R.id.bottom_left_button: return R.drawable.bottom_left_bling;
            case R.id.bottom_right_button: return R.drawable.bottom_right_bling;
            default: return 0; /*error*/
        }
    }

    //causes the "physical" simon game button to bling (flash and play a sound)
    final protected void blingButton(final ImageView ivButton, int blingLength){
        //play sound

        //flash image
        ivButton.setImageResource(getBlingImageId(ivButton.getId())); //set the image to the "blinged" button
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ivButton.setImageResource(getNormalImageId(ivButton.getId()));
            }
        };

        blingHandler.postDelayed(r, blingLength);
    }

    protected class FailureButtonSequenceTask extends ButtonSequenceTask{
        FailureButtonSequenceTask(){
            super();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SimonGameEngine.Button twirlSequence[] = {SimonGameEngine.Button.TOP_LEFT, SimonGameEngine.Button.TOP_RIGHT,
                    SimonGameEngine.Button.BOTTOM_RIGHT, SimonGameEngine.Button.BOTTOM_LEFT};

            /*note, unlike our normal sequence task, if this thread is interrupted we do not want it
            * to re-enter the loop once it resumes; this is because the failure animation task isn't really necessary,
            * but the pattern sequence animation certainly is*/
            try{
                for (int i = 0; i < 12; i++) {
                    Thread.sleep(200);
                    publishProgress(twirlSequence[i % twirlSequence.length]);
                }
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(SimonGameEngine.Button... progress){
            ImageView buttonToBling = (ImageView) findViewById(buttonToImageView(progress[0]));
            blingButton(buttonToBling, 100);
        }

        @Override
        protected void onPostExecute(Void result){
            failureAnim = null;
            Toast.makeText(SimonActivity.this, "You lose! Hope you had fun though.", Toast.LENGTH_SHORT).show();
            enableButtons();
        }
    }

    protected class ButtonSequenceTask extends AsyncTask<Void, SimonGameEngine.Button, Void> {
        ButtonSequenceTask(){
            super();
        }

        protected void onPreExecute(){
            disableButtons();
        }

        protected Void doInBackground(Void... params){
            for(SimonGameEngine.Button button : game.getPattern()){
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

        protected void onProgressUpdate(SimonGameEngine.Button... progress){
            ImageView buttonToBling = (ImageView) findViewById(buttonToImageView(progress[0]));
            blingButton(buttonToBling, 300);
        }

        protected void onPostExecute(Void result){
            sequenceAnim = null;
            enableButtons();
            //start the round
            game.startRound();
        }
    }

    protected void disableButtons(){
        /*disable 4 simon buttons, start game button, etc.*/
        int buttonsToDisable[] = {R.id.bottom_right_button, R.id.bottom_left_button,
                R.id.top_left_button, R.id.top_right_button, R.id.play_button};

        for(int buttonToDisable : buttonsToDisable){
            findViewById(buttonToDisable).setEnabled(false);
        }
    }

    protected void enableButtons(){
 	    /*re-enable 4 simon buttons, start game button, etc.*/
        int buttonsToEnable[] = {R.id.bottom_right_button, R.id.bottom_left_button,
                R.id.top_left_button, R.id.top_right_button, R.id.play_button};

        for(int buttonToEnable : buttonsToEnable){
            findViewById(buttonToEnable).setEnabled(true);
        }
    }
}
