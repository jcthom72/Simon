package csci4020.shawnbickel_judsonthomas.assignment2.simon;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.Simon_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent a  = new Intent (MenuActivity.this, Simon.class);
                startActivity(a);
            }
        });

        findViewById(R.id.About_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = "<html>" +
                        "<h1>Simon Game Image</h1>" +
                        "<h3>Creator: Steve Berry</h3>" +
                        "<p><b>Link: </b> <a href='https://www.flickr.com/photos/unloveable/2405593748'>Source Website</a><p>" +
                        "<b>License: </b> CC BY-NC-SA 2.0" +
                        "<p><b>License: </b> <a href='https://creativecommons.org/licenses/by-nc-sa/2.0/legalcode'>Source Website</a><p><br>" +
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
