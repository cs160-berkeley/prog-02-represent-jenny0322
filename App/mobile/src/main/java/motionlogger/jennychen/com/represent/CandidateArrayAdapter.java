package motionlogger.jennychen.com.represent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jenny0322 on 3/1/16.
 */
public class CandidateArrayAdapter extends BaseAdapter {
    protected String[] names;
    protected int[] photos;
    protected String[] party;
    protected String[] type;
    protected Context context;
    protected String[] email;
    protected String[] website;
    protected String[] twitter;
    protected String[] twitterDetail;
    protected static String extra_name;

    public CandidateArrayAdapter(Context context, String[] names, int[] photos, String[] party, String[] type, String[] email, String[] website, String[] twitter, String[] twitterDetail) {
        this.context = context;
        this.names = names;
        this.photos = photos;
        this.party = party;
        this.type = type;
        this.email = email;
        this.website = website;
        this.twitter = twitter;
        this.twitterDetail = twitterDetail;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context

                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.zipresults_main, parent, false);

        TextView nameView = (TextView) v.findViewById(R.id.name_dynamic);

        TextView partyView = (TextView) v.findViewById(R.id.party_dynamic);

        TextView typeView = (TextView) v. findViewById(R.id.type_dynamic);

        TextView siteView = (TextView) v. findViewById(R.id.site_dynamic);

        TextView emailView = (TextView) v. findViewById(R.id.email_dynamic);

        TextView twitterView = (TextView) v. findViewById(R.id.twitter_dynamic);

        TextView twitterDetailView = (TextView) v. findViewById(R.id.twitterdetail_dynamic);



        ImageView pictureView = (ImageView) v.findViewById(R.id.avatar_dynamic);

        nameView.setText(names[position]);

        if (party[position].equals("Republican")) {
            partyView.setTextColor(ContextCompat.getColor(context, R.color.red));

        }

        partyView.setText(party[position]);


        typeView.setText(type[position]);

        siteView.setText(website[position]);

        emailView.setText(email[position]);

        twitterView.setText(twitter[position]);

        twitterDetailView.setText(twitterDetail[position]);

        pictureView.setBackgroundResource(photos[position]);

        v.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //Send Toast or Launch Activity here
                Intent detail = new Intent(context, detail.class);

                detail.putExtra("extra_name", names[position]);

                detail.putExtra("randomized", "false");
                context.startActivity(detail);



            }

        });

        return v;
    }


}
