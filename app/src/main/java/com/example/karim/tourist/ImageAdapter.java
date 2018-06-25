package com.example.karim.tourist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;


public class ImageAdapter extends ArrayAdapter<Bitmap> {

    public ImageAdapter(Context context, @NonNull ArrayList<Bitmap> objects) {
        super(context, R.layout.images, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        View v = myCustomInflater.inflate(R.layout.images, parent, false);
        ImageView imageView=v.findViewById(R.id.image);
        imageView.setImageBitmap(getItem(position));
        // if(getItem(position)==null) {
        //   Picasso.with(getContext()).load(getItem(position)).into(imageView);
        //}
        ImageButton b=v.findViewById(R.id.gridbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormActivity.images.remove(  getItem(position));
                notifyDataSetChanged();
            }
        });
        return v;
    }

}
