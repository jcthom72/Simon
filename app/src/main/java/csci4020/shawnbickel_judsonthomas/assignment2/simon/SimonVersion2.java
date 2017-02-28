package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SimonVersion2 extends AppCompatActivity {

    private PlaySimon playSimonGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon_game_layout);

        // Initialize Game when user pushes the start button
        findViewById(R.id.play_button).setOnClickListener(new InitializeGame());
        findViewById(R.id.pause_button).setOnClickListener(new PauseLevel());
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

    class PauseLevel implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            // implement logic to save the state of the level
        }
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
