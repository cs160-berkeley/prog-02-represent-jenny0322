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


public class ScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";


    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        if (mPageNumber == 0) {

            ((ImageButton) rootView.findViewById(R.id.photo_watch)).setBackgroundResource(R.drawable.janet_bewley);
            ((TextView) rootView.findViewById(R.id.name_watch)).setText(
                    "Janet Bewley");
            ((TextView) rootView.findViewById(R.id.information_watch)).setText("Democrat Senator");

        }else if (mPageNumber == 1) {
            ((ImageButton) rootView.findViewById(R.id.photo_watch)).setBackgroundResource(R.drawable.rob_cowles);
            ((TextView) rootView.findViewById(R.id.name_watch)).setText(
                    "Robert Cowles");
            ((TextView) rootView.findViewById(R.id.information_watch)).setText("Republican Senator");
        }else{
            ((ImageButton) rootView.findViewById(R.id.photo_watch)).setBackgroundResource(R.drawable.scott_allen);
            ((TextView) rootView.findViewById(R.id.name_watch)).setText(
                    "Scott Allen");
            ((TextView) rootView.findViewById(R.id.information_watch)).setText("Republican Rep.");

        }

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
