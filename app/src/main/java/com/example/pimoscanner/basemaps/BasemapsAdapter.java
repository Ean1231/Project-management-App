package com.example.pimoscanner.basemaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pimoscanner.R;

import java.util.ArrayList;
import java.util.List;

public class BasemapsAdapter extends BaseAdapter {

    BasemapsAdapterClickListener mOnClickListener;
    // need context to use it to construct view
    Context mContext;
    // hold onto a copy of all basemap items
    List<BasemapItem> mItems;

    public BasemapsAdapter(Context c, ArrayList<BasemapItem> portalItems, BasemapsAdapterClickListener listener) {
        mContext = c;
        mItems = portalItems;
        mOnClickListener = listener;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Inflate view unless we have an old one to reuse
        View newView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newView = inflater.inflate( R.layout.basemap_image, null);
        }

        // Create view for the thumbnail
        ImageView image = newView.findViewById(R.id.basemap_grid_item_thumbnail_imageview);
        image.setImageBitmap(mItems.get(position).itemThumbnail);

        // Register listener for clicks on the thumbnail
        image.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(final View view) {
                mOnClickListener.onBasemapItemClicked(position);
            }
        });

        // Set the title and return the view we've created
        TextView text = newView.findViewById(R.id.basemap_grid_item_title_textview);
        text.setText(mItems.get(position).item.getTitle());
        return newView;
    }

    /**
     * A callback interface thatDirections indicates when a basemap in the list has been
     * clicked.
     */
    public interface BasemapsAdapterClickListener {
        /**
         * Callback for when a basemap list item is clicked.
         *
         * @param listPosition
         *            Position within the list of an item that has been clicked.
         */
        void onBasemapItemClicked(int listPosition);
    }
}
