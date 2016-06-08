package com.example.sunnny.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sunnny on 21/05/16.
 */
public class ImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    ArrayList<String> urls;

    public ImageAdapter(Context mContext, ArrayList<String> urls) {
        super(mContext,urls.size());
        this.mContext = mContext;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ImageView imageView;
        if(view==null)
        {
            imageView=new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150,250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2,2,2,2);
        }
        else
        {
            imageView=(ImageView)view;
        }


        Picasso.with(mContext)
                .load(getItem(i))
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(imageView);

        return imageView;
    }

}
