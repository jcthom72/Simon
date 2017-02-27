package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by judson on 2/26/2017.
 */

//specific simon version activities will inherit from SimonActivity
public class SimonActivity extends AppCompatActivity{
    protected SimonGameEngine game;
    protected ButtonSequenceTask sequenceAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize game object
        game = new SimonGameEngine();

        //initialize ButtonSequenceTask for button pattern animation
        sequenceAnim = new ButtonSequenceTask(this);
    }

    protected void gamePressEvent(ImageView buttonImagePressed){
        blingButton(buttonImagePressed);

        if(game.isPlaying()){
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

    protected void nextRoundEvent(){
        game.nextRound();
        sequenceAnim.execute();
    }

    protected void gameEndEvent(){
        //save high score somewhere
        //score = game.player.getScore() etc. etc.

        game.endGame();
    }

    protected void gameStartEvent(){
        game.startGame();
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
    final protected void blingButton(ImageView ivButton){
        //play sound

        //flash image
        try{
            ivButton.setImageResource(getBlingImageId(ivButton.getId())); //set the image to the "blinged" button
            Thread.sleep(1000); //make UI thread sleep: button stays flashed for at least 100ms
        } catch (InterruptedException e){
        }
        //ivButton.setImageResource(getNormalImageId(ivButton.getId()));
    }

    protected class ButtonSequenceTask extends AsyncTask<Void, SimonGameEngine.Button, Void> {
        SimonActivity activity;

        ButtonSequenceTask(SimonActivity activity){
            super();
            this.activity = activity;
        }

        protected void onPreExecute(){
            activity.sequenceStartEvent();
        }

        protected Void doInBackground(Void... params){
            for(SimonGameEngine.Button button : activity.game.getPattern()){
			/*even if thread is interrupted, sequence animation will resume
			once thread is resumed*/
                try{
                    publishProgress(button);
                    Thread.sleep(1000); //at least one second of delay between each bling
                } catch(InterruptedException e){
                }
            }
            return null;
        }

        protected void onProgressUpdate(SimonGameEngine.Button... progress){
            ImageView buttonToBling = (ImageView) activity.findViewById(activity.buttonToImageView(progress[0]));
            activity.blingButton(buttonToBling);
        }

        protected void onPostExecute(Void result){
            activity.sequenceEndEvent();
        }
    }

    /*event executed when button sequence animation starts*/
    final protected void sequenceStartEvent(){
	/*disable 4 simon buttons, start game button, etc.*/

    }

    /*event executed when button sequence animation ends*/
    final protected void sequenceEndEvent(){
	/*re-enable 4 simon buttons, start game button, etc.*/

        //start the round
        game.startRound();
    }
}
