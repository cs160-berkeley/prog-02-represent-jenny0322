package motionlogger.jennychen.com.represent;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class detail extends AppCompatActivity {

    protected String randomized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String name  = intent.getStringExtra("extra_name");
        randomized = intent.getStringExtra("randomized");


        TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(name);
        ImageView avatarView = (ImageView) findViewById(R.id.avatar);
        TextView partyView = (TextView) findViewById(R.id.party);
        TextView typeview = (TextView) findViewById(R.id.type);
        if (name.equals("Janet Bewley")) {
            avatarView.setBackgroundResource(R.drawable.janet_bewley);
            partyView.setText("Democrat");
            partyView.setTextColor(getResources().getColor(R.color.blue));
            typeview.setText("Senator");

        }else if (name.equals("Robert Cowles")) {

            avatarView.setBackgroundResource(R.drawable.rob_cowles);
            partyView.setText("Republican");
            partyView.setTextColor(getResources().getColor(R.color.red));
            typeview.setText("Senator");
        }else{
            avatarView.setBackgroundResource(R.drawable.scott_allen);
            partyView.setText("Republican");
            partyView.setTextColor(getResources().getColor(R.color.red));
            typeview.setText("Representative");
        }
    }
    public void showVote(View view) {
        Intent voteIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        voteIntent.putExtra("TYPE", "vote");
        System.out.println("detail received randomized is" + randomized);
        voteIntent.putExtra("RANDOMIZED", randomized);
        startService(voteIntent);
    }
}
