package com.example.adeeb.orderoffline;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by AdeeB on 10/14/2016.
 */
public class AnimationXml {
    Context context;

    public AnimationXml(Context context) {
        this.context=context;
    }

    public  void animateCollapsingview(ImageView imageView){
        Animation animationzoom;
            animationzoom= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.zoom2);
            animationzoom.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            imageView.startAnimation(animationzoom);
        }
    public  void animateview(RecyclerView.ViewHolder holder){
        Animation animationzoom;
        animationzoom= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.rotate);
        animationzoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        holder.itemView.startAnimation(animationzoom);
    }

    public  void animateview2(View view){
        Animation animationzoom;
        animationzoom= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.ordersended);
        animationzoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animationzoom);
    }


    public  void animateview3(View view){
        Animation animationzoom;
        animationzoom= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.signup);
        animationzoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animationzoom);
    }

    }

