package com.kstefancic.nekretnineinfo.views;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 2.12.2017..
 */

public class PictureGridViewAdapter extends ArrayAdapter {

    private Context context;
    private int resourceId;
    private ArrayList imageUris = new ArrayList();

    public PictureGridViewAdapter(Context context, int resourceId, ArrayList data){
        super(context,resourceId,data);
        this.context=context;
        this.resourceId=resourceId;
        this.imageUris =data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.ivPicture = row.findViewById(R.id.picture_item_ivImage);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Uri uriItem = (Uri) imageUris.get(position);
        Picasso.with(context)
                .load(uriItem)
                .fit()
                .centerCrop()
                .into(holder.ivPicture);

        return  row;
    }

    static class ViewHolder{
        ImageView ivPicture;
    }
}
