package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class SimonVersion2 extends AppCompatActivity {
    protected SimonGameEngine game2;
    private PlaySimon playSimonGame;
    private final String HighScoreV2 = "HighScoreV2.txt";
    protected SimonActivity.ButtonSequenceTask sequenceAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon_game_layout);

        // Initialize Game when user pushes the start button
        findViewById(R.id.play_button).setOnClickListener(new InitializeGame());
    }

    class InitializeGame implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            /* if an AsyncTask already exists and the task if finished executing, then make the
                 object null */
            if (playSimonGame != null && playSimonGame.getStatus() == AsyncTask.Status.FINISHED) {
                playSimonGame = null;
            }

            // if AsyncTask does not exist in memory, then the object instantiation creates it
            if (playSimonGame == null) {
                playSimonGame = new PlaySimon();
                playSimonGame.execute();
            }
        }
    }

    protected void updateScoreText(){
        ((TextView) findViewById(R.id.HighScore)).setText("" + game2.player.getScore());
        int s = game2.player.getScore();
        String score = Integer.toString(s);
        saveHighScore(score);
    }

    // saves the player's score to a file
    private void saveHighScore(String score)  {
        try {
            FileOutputStream fos = openFileOutput(HighScoreV2, Context.MODE_PRIVATE);
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
            FileInputStream fis = openFileInput(HighScoreV2);
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

    protected void gameStartEvent(){
        game2.startGame();
        TextView score = (TextView) findViewById(R.id.HighScore);
        // sets the player's score retrieved from file
        String sc = returnHighScore();
        score.setText(sc);

        try{
            int s = Integer.parseInt(sc);
            game2.player.setScore(s);
        }catch (NumberFormatException e){
            updateScoreText();
        }


        updateScoreText();

        if(sequenceAnim == null){
            //sequenceAnim = new SimonActivity.ButtonSequenceTask();
        }
        //sequenceAnim.execute();
    }


    // PlaySimon class includes methods to execute threads
    class PlaySimon extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // doInBackground is the background thread
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // pass View from background thread to main UI method onProgressUpdate()
                publishProgress(voids);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        // updates main UI
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    // onPause destroys the AsyncTask
    @Override
    protected void onPause() {
        super.onPause();
        if (playSimonGame != null) {
            playSimonGame.cancel(true);
            playSimonGame = null;
        }
    }
}
