package com.kstefancic.potresnirizik.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.localDBdto.LocalImage;
import com.kstefancic.potresnirizik.buildingdata.PicturesFragment;
import com.kstefancic.potresnirizik.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by user on 2.12.2017..
 */

public class PictureGridViewAdapter extends ArrayAdapter {

    private Context context;
    private int resourceId;
    private ArrayList images = new ArrayList();
    private PicturesFragment.PictureChoosen pictureChoosen;

    public PictureGridViewAdapter(Context context, int resourceId, ArrayList data, PicturesFragment.PictureChoosen pictureChoosenListener){
        super(context,resourceId,data);
        this.pictureChoosen = pictureChoosenListener;
        this.context=context;
        this.resourceId=resourceId;
        this.images =data;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.ivPicture = row.findViewById(R.id.pictureItem_ivImage);
            holder.ibClear = row.findViewById(R.id.pictureItem_ibDelete);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        final LocalImage localImage= (LocalImage) images.get(position);
        Log.i("LOCAL IMAGE", localImage.toString());
        holder.ivPicture.setImageBitmap(localImage.getImage());
        holder.ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper.getInstance(context).deleteImageById(localImage.getId());
                images.remove(position);
                notifyDataSetChanged();
                pictureChoosen.onPictureChoosenListener(images);
            }
        });

        /*Uri uriItem = (Uri) imageUris.get(position);
        Picasso.with(context)
                .load(uriItem)
                .fit()
                .centerCrop()
                .into(holder.ivPicture);*/

        return  row;
    }

    static class ViewHolder{
        ImageView ivPicture;
        ImageButton ibClear;
    }
}
