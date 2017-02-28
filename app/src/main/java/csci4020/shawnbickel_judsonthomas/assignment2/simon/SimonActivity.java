package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by judson on 2/26/2017.
 */

//specific simon version activities will inherit from SimonActivity
public class SimonActivity extends AppCompatActivity{
    private TextView score;
    protected SimonGameEngine game;
    protected ButtonSequenceTask sequenceAnim;
    protected FailureButtonSequenceTask failureAnim;
    protected Handler blingHandler;
    protected Runnable blingRun;
    private final String HighScoreV1 = "HighScoreV1.txt";
    private SoundPool soundPool;
    private Set<Integer> sounds; // a set to hold sounds and indicate that sound can be played

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView score = (TextView) findViewById(R.id.HighScore);
        sounds = new HashSet<Integer>();

        //initialize game object
        game = new SimonGameEngine();

        //initialize ButtonSequenceTask for button pattern animation
        sequenceAnim = new ButtonSequenceTask();
        failureAnim = new FailureButtonSequenceTask();

        //initialize bling handler for posting delayed bling message to UI thread
        blingHandler = new Handler();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes.Builder audioAttr = new AudioAttributes.Builder();
        audioAttr.setUsage(AudioAttributes.USAGE_GAME); // sets audio usage as game

        SoundPool.Builder builder = new SoundPool.Builder(); // creates sound
        builder.setAudioAttributes(audioAttr.build());
        builder.setMaxStreams(1); // prevents more than 1 stream from playing

        soundPool = builder.build(); // builder assigned to SoundPool object

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0){
                    sounds.add(sampleId); // adds audio to the set
                }else{
                    Log.i("SOUNDS", "ERROR can't load sound ");
                }
            }
        });
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

    protected void updateRoundText(){
        ((TextView) findViewById(R.id.RoundText)).setText("" + game.player.getRound());
    }

    protected void updateScoreText(){
        game.player.setScore();
        ((TextView) findViewById(R.id.HighScore)).setText("" + game.player.getScore());
        int s = game.player.getScore();
        String score = Integer.toString(s);
        saveHighScore(score);
    }



    // saves the player's score to a file
    private void saveHighScore(String score)  {
        try {
            FileOutputStream fos = openFileOutput(HighScoreV1, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter (bw);
            pw.print(score);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Can't write to file", Toast.LENGTH_LONG).show();
        }
    }

    // returns a player's score from a file
    public String returnHighScore(){
        String score = "";

        try {
            FileInputStream fis = openFileInput(HighScoreV1);
            Scanner s = new Scanner(fis);
            while(s.hasNext()){
                score = s.next();
            }
            s.close();
        } catch (FileNotFoundException e) {
            Log.i("ReadData", "no input file found");
        }
        return score;
    }

    protected void nextRoundEvent(){
        game.player.setScore();
        String sc = returnHighScore();
        score.setText(sc);
        game.nextRound();
        updateRoundText();
        updateScoreText();

        //play button sequence
        if(sequenceAnim == null){
            sequenceAnim = new ButtonSequenceTask();
        }
        sequenceAnim.execute();
    }

    protected void gameEndEvent(){
        //save high score somewhere
        updateRoundText();
        updateScoreText();

        game.endGame();
        if(failureAnim == null){
            failureAnim = new FailureButtonSequenceTask();
        }
        failureAnim.execute();
    }

    protected void gameStartEvent(){
        game.startGame();
        TextView score = (TextView) findViewById(R.id.HighScore);
        // sets the player's score retrieved from file
        String sc = returnHighScore();
        score.setText(sc);

        try{
            int s = Integer.parseInt(sc);
            game.player.setScore();
        }catch (NumberFormatException e){
            updateScoreText();;
        }


        updateRoundText();
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
        // loads sound into memory
        final int beep = soundPool.load(this, R.raw.electronic_beep, 1);
        final int orbit = soundPool.load(this, R.raw.orbit, 1);

        //play sound
        if (blingLength == 300 || blingLength == 200){
            if (sounds.contains(beep)){
                soundPool.play(beep, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }else if (blingLength == 100) {
            if (sounds.contains(orbit)) {
                soundPool.play(orbit, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }

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
            Toast.makeText(SimonActivity.this, "You lose! Hope you had fun thoughh.", Toast.LENGTH_SHORT).show();
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
            Queue<SimonGameEngine.Button> sequence = game.getPattern();
            SimonGameEngine.Button button;
            for(int i = sequence.size() - 1; i >= 0; i--){
                button = (SimonGameEngine.Button) sequence.toArray()[i];
			    /*even if thread is interrupted, sequence animation will resume
			    once thread is resumed*/
                try{
                    Thread.sleep(1000); //at least one second of delay between each bling
                    //SimonGameEngine.Button swappedButton = null;
                    /*switch(button){
                        case TOP_LEFT: swappedButton = SimonGameEngine.Button.BOTTOM_RIGHT;
                            break;
                        case TOP_RIGHT: swappedButton = SimonGameEngine.Button.BOTTOM_LEFT;
                            break;
                        case BOTTOM_LEFT: swappedButton = SimonGameEngine.Button.TOP_RIGHT;
                            break;
                        case BOTTOM_RIGHT: swappedButton = SimonGameEngine.Button.TOP_LEFT;
                            break;
                    }*/
                    //publishProgress(swappedButton);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (soundPool != null){
            soundPool.release();
            soundPool = null;

            sounds.clear();
        }
    }
}
