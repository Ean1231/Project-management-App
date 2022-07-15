package com.example.pimoscanner.location;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.FragmentTransaction;

import com.example.pimoscanner.R;

public class RoutingDialogFragment extends DialogFragment {
    public static final String ARG_END_POINT_DEFAULT = "EndPointDefault";

    public static final String MY_LOCATION = "My Location";

    private static final String SEARCH_FROM = "From";

    private static final String SEARCH_TO = "To";

    private String mEndPointDefault;

    private SearchView mStartText;

    private SearchView mEndText;

    private RoutingDialogListener mRoutingDialogListener;

    // Mandatory empty constructor for fragment manager to recreate fragment
    // after it's destroyed.
    public RoutingDialogFragment() {
    }

    /**
     * Sets listener for click on Get Route button.
     *
     * @param listener
     */
    public void setRoutingDialogListener(RoutingDialogListener listener) {
        mRoutingDialogListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle( DialogFragment.STYLE_NORMAL, 0);

        if (getArguments().containsKey(ARG_END_POINT_DEFAULT)) {
            mEndPointDefault = getArguments().getString(ARG_END_POINT_DEFAULT);
        } else {
            mEndPointDefault = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routing_layout, container, false);
        getDialog().setTitle( R.string.title_routing_dialog);
        // Initialize searchviews
        mStartText = view.findViewById(R.id.startPoint);
        mEndText = view.findViewById(R.id.endPoint);

        mStartText.setIconifiedByDefault(false);
        mEndText.setIconifiedByDefault(false);

        // Set hint for searchviews
        mStartText.setQueryHint(SEARCH_FROM);
        mEndText.setQueryHint(SEARCH_TO);

        // Change default search icons for the search view
        int startIconId = mStartText.getContext().getResources().getIdentifier("android:id/search_mag_icon", null,
                null);
        ImageView start_icon = mStartText.findViewById(startIconId);
        start_icon.setImageResource(R.drawable.pin_circle_red);

        int endIconId = mEndText.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView end_icon = mEndText.findViewById(endIconId);
        end_icon.setImageResource(R.drawable.pin_circle_blue);

        mStartText.setQuery(MY_LOCATION, false);
        mStartText.clearFocus();
        mEndText.requestFocus();
        if (mEndPointDefault != null) {
            mEndText.setQuery(mEndPointDefault, false);
        }
        ImageView swap = view.findViewById(R.id.iv_interchange);

        Button routeButton = view.findViewById(R.id.getRouteButton);
        // Set up onClick listener for the "Get Route" button
        routeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String startPoint = mStartText.getQuery().toString();
                String endPoint = mEndText.getQuery().toString();
                if (mRoutingDialogListener.onGetRoute(startPoint, endPoint)) {
                    dismiss();
                }
            }

        });

        // Interchange the text in the searchviews
        swap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Swap the places
                String temp = mStartText.getQuery().toString();
                mStartText.setQuery(mEndText.getQuery().toString(), false);
                mEndText.setQuery(temp, false);

            }
        });

        // Setup listener when the search button is clicked n the keyboard for
        // the searchviews
        mEndText.setOnQueryTextListener(new SearchView.OnQueryTextListener () {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String startPoint = mStartText.getQuery().toString();
                String endPoint = mEndText.getQuery().toString();
                if (startPoint.length() > 0) {
                    if (mRoutingDialogListener.onGetRoute(startPoint, endPoint)) {
                        dismiss();
                    }
                } else {
                    // "From" text is null
                    mEndText.clearFocus();
                    mStartText.requestFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mStartText.setOnQueryTextListener(new SearchView.OnQueryTextListener () {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String startPoint = mStartText.getQuery().toString();
                String endPoint = mEndText.getQuery().toString();
                if (endPoint.length() > 0) {
                    if (mRoutingDialogListener.onGetRoute(startPoint, endPoint)) {
                        dismiss();
                    }
                } else {
                    // "To" text is null
                    mStartText.clearFocus();
                    mEndText.requestFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    public void show(FragmentTransaction ft, String routingdialog) {
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement, to receive a routing request from this fragment.
     */
    public interface RoutingDialogListener {
        /**
         * Callback for when the Get Route button is pressed.
         *
         * @param startPoint
         *            String entered by user to define start point.
         * @param endPoint
         *            String entered by user to define end point.
         * @return true if routing task executed, false if parameters rejected.
         *         If this method rejects the parameters it must display an
         *         explanatory Toast to the user before returning.
         */
        boolean onGetRoute(String startPoint, String endPoint);
    }
}
