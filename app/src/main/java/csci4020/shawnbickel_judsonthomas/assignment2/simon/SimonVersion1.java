package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class SimonVersion1 extends SimonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon_game_layout);



        // Initialize button listeners
        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStartEvent();
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
}
