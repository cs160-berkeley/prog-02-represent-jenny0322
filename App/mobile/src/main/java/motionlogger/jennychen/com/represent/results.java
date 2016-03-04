package motionlogger.jennychen.com.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class results extends AppCompatActivity {

    protected String input_randomized;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();
        input_randomized = intent.getStringExtra("randomized");
        System.out.println("result randomized is" + input_randomized);
    }

    public void viewDetail1(View view) {
        Intent detail = new Intent(this, detail.class);
        TextView input_name_view = (TextView) findViewById(R.id.name1);
        String input_name = input_name_view.getText().toString();
        detail.putExtra("extra_name", input_name);
        System.out.println(input_name);
        detail.putExtra("randomized", input_randomized);
        startActivity(detail);

    }
    public void viewDetail2(View view) {
        Intent detail = new Intent(this, detail.class);
        TextView input_name_view = (TextView) findViewById(R.id.name2);
        String input_name = input_name_view.getText().toString();
        detail.putExtra("extra_name", input_name);
        detail.putExtra("randomized", input_randomized);
        startActivity(detail);

    }
    public void viewDetail3(View view) {
        Intent detail = new Intent(this, detail.class);
        TextView input_name_view = (TextView) findViewById(R.id.name3);
        String input_name = input_name_view.getText().toString();
        detail.putExtra("extra_name", input_name);
        detail.putExtra("randomized", input_randomized);
        startActivity(detail);

    }

}
