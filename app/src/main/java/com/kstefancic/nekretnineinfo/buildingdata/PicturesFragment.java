package com.kstefancic.nekretnineinfo.buildingdata;


import android.support.v4.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.views.PictureGridViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by user on 15.11.2017..
 */

public class PicturesFragment extends Fragment{

    private Button btnFinish, btnBrowse;
    private GridView gvPictures;
    private PictureGridViewAdapter pictureGridViewAdapter;
    private PictureChoosen pictureChoosenListener;
    private ArrayList<Uri> fileUris = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pictures,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.gvPictures = layout.findViewById(R.id.frPictures_gridView);
        this.btnBrowse = layout.findViewById(R.id.frPictures_btnBrowse);
        this.btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picturesDirectoryPath = pictureDirectory.getPath();

                Uri data = Uri.parse(picturesDirectoryPath);

                intent.setDataAndType(data,"image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 10);
            }
        });
        this.btnFinish =layout.findViewById(R.id.frPictures_btnFinish);
        this.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              pictureChoosenListener.onPictureChoosenListener(fileUris);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();

            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                Log.d("URI", uri.toString());
                fileUris.add(uri);
            }

            pictureGridViewAdapter = new PictureGridViewAdapter(getActivity(),R.layout.picture_grid_item, fileUris);
            gvPictures.setAdapter(pictureGridViewAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PictureChoosen)
        {
            this.pictureChoosenListener = (PictureChoosen) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.pictureChoosenListener =null;
    }

    public interface PictureChoosen {
        void onPictureChoosenListener( ArrayList<Uri> imageUris);
    }

}
