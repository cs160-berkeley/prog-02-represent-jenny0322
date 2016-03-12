package motionlogger.jennychen.com.represent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class ScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    public static final String DATA = "data";


    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private JSONObject person;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber, JSONObject person) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString(DATA, person.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        try {
            person = new JSONObject(getArguments().getString(DATA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        String party;
        String type;
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        ((TextView) rootView.findViewById(R.id.name_watch)).setText(
                person.optString("first_name").toString() + " " + person.optString("last_name").toString());

        if (person.optString("party").toString().equals("D")){
            party = "Democrat";
        }else{
            party = "Republican";
        }if (person.optString("title").toString().equals("Sen")){
            type = "Senator";
        }else{
            type = "Rep";
        }
        ((TextView) rootView.findViewById(R.id.information_watch)).setText(party + " " + type);






        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
