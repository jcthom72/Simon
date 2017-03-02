package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.os.Bundle;
import android.widget.TextView;


public class SimonVersion1 extends SimonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sequenceAnim = new ButtonSequenceTask();

        ((TextView) findViewById(R.id.SimonTevtView)).setText("Simon (v1)");
    }
}
