package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    private RadioGroup group;
    private int VersionChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // determine which radio button is selected
        group = (RadioGroup) findViewById(R.id.RadioGroupVersions);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton1){
                    VersionChoice = 1;
                }

                else if (i == R.id.radioButton2){
                    VersionChoice = 2;
                }

                else if (i == R.id.radioButton3){
                    VersionChoice = 3;
                }
            }
        });

        // listener to start the game
        findViewById(R.id.Simon_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent a  = new Intent (MenuActivity.this, Simon.class);
                startActivity(a);
            }
        });

        // listener to display image information
        findViewById(R.id.About_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = "<html>" +
                        "<h1>Simon Game Image</h1>" +
                        "<h3>Creator: Steve Berry</h3>" +
                        "<p><b>Link: </b> <a href='https://www.flickr.com/photos/unloveable/2405593748'>Source Website</a><p>" +
                        "<b>License: </b> CC BY-NC-SA 2.0" + "  " +
                        "<a href='https://creativecommons.org/licenses/by-nc-sa/2.0/legalcode'>Source Website</a><br>" +
                        "</html>";
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(Html.fromHtml(message));
                builder.setPositiveButton("Ok", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                // must be done after the call to show();
                // allows anchor tags to work
                TextView tv = (TextView) dialog.findViewById(android.R.id.message);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }
}
