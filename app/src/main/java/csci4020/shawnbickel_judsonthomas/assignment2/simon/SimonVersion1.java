package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashSet;
import java.util.Set;


public class SimonVersion1 extends AppCompatActivity {

    private PlaySimon playSimonGame;
    private SoundPool soundPool;
    private Set<Integer> sounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon_version_1);

        sounds = new HashSet<Integer>();

        // Initialize Game when user pushes the start button
        findViewById(R.id.play_button).setOnClickListener(new InitializeGame());
        findViewById(R.id.pause_button).setOnClickListener(new PauseLevel());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes.Builder audioAttr = new AudioAttributes.Builder();
        audioAttr.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setAudioAttributes(audioAttr.build());
        builder.setMaxStreams(4);

        soundPool = builder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0){
                    sounds.add(sampleId);
                }else{

                }
            }
        });

        // loads sound into memory
        final int beep = soundPool.load(this, R.raw.electronic_beep, 1);
        /*  At this point the sound should be connected to the game buttons
        *           through soundPool.play(beep, 1.0f, 1.0f, 0,0 1.0f);*/

        final int orbit = soundPool.load(this, R.raw.orbit, 1);
        /* Connect sound to game play */

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
        if (soundPool != null){
            soundPool.release();
            soundPool = null;

            sounds.clear();
        }
        if (playSimonGame != null) {
            playSimonGame.cancel(true);
            playSimonGame = null;
        }
    }
}
