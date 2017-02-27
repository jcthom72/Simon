package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;


public class SimonVersion1 extends SimonActivity {
    private SoundPool soundPool;
    private Set<Integer> sounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon_version_1);

        sounds = new HashSet<Integer>();

        // Initialize button listeners
        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStartEvent();
            }
        });

        findViewById(R.id.pause_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*pause stuff here*/
            }
        });

        //initialize simon buttons
        int[] simonButtonIDs;
        simonButtonIDs = new int[]{R.id.top_left_button, R.id.top_right_button,
                R.id.bottom_left_button, R.id.bottom_right_button};

        for(int simonButtonID : simonButtonIDs){
            findViewById(simonButtonID).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gamePressEvent((ImageView) view);
                }
            });
        }
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
