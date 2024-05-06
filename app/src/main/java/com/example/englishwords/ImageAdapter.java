package com.example.englishwords;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int[] mImages;
    private int mMargin;

    public ImageAdapter(Context context, int[] images, int margin) {
        mContext = context;
        mImages = images;
        mMargin = margin;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public Object getItem(int position) {
        return mImages[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350)); // Adjust size as needed
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setMinimumHeight(350);
            imageView.setMinimumWidth(350);
            imageView.setPadding(mMargin, mMargin, mMargin, mMargin);
        } else {
            imageView = (ImageView) convertView;
        }


        // Set margins programmatically

        imageView.setImageResource(mImages[position]);
        return imageView;
    }
}
