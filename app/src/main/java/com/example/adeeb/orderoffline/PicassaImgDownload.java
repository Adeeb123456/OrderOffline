package com.example.adeeb.orderoffline;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by AdeeB on 12/1/2016.
 */
public class PicassaImgDownload {
    Context context;
    public PicassaImgDownload(Context context) {
        this.context=context;
    }

    public void reqPicassa(String imgUrl, ImageView imgref){

        Picasso.with(context).load(imgUrl).resize(1000,400).into(imgref);
    }
}
