package com.example.adeeb.orderoffline;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by AdeeB on 1/30/2016.
 */
public class AnimationUtils {

public static void Animate(RecyclerView.ViewHolder holder,boolean goesdown){
    AnimatorSet animatorSet=new AnimatorSet();
    ObjectAnimator animatex= ObjectAnimator.ofFloat(holder.itemView, "translationX", goesdown == true ? 200 : -200, 0);
   animatex.setDuration(300);
  //ObjectAnimator animatey= ObjectAnimator.ofFloat(holder.itemView, "translationX",-50,50,-30,30,-20,20,-5,-5, 0);
//  animatey.setDuration(300);
  //  animatorSet.playTogether(animatey,animatex);

    animatorSet.playTogether(animatex);

    animatorSet.start();
}

    public static void AnimateCustomAdaptrObjs(RecyclerView.ViewHolder holder){
        AnimatorSet animatorSet=new AnimatorSet();
        //ObjectAnimator animatex= ObjectAnimator.ofFloat(holder.itemView, "translationY", goesdown == true ? 2000 : -2000, 0);

        ObjectAnimator animatey= ObjectAnimator.ofFloat(holder.itemView, "translationX", -500, 500, 0);
        animatey.setDuration(500);
        animatorSet.playTogether(animatey);
        animatorSet.start();
    }






    public static void Animatebtn(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        //ObjectAnimator animatex= ObjectAnimator.ofFloat(holder.itemView, "translationY", goesdown == true ? 2000 : -2000, 0);

        ObjectAnimator animatey= ObjectAnimator.ofFloat(view, "translationX", -500, 500, 0);
        animatey.setDuration(600);
        animatorSet.playTogether(animatey);
        animatorSet.start();
    }
    public static void Animatescroll(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        //ObjectAnimator animatex= ObjectAnimator.ofFloat(holder.itemView, "translationY", goesdown == true ? 2000 : -2000, 0);

        ObjectAnimator animatey= ObjectAnimator.ofFloat(view, "translationX", -50, 50, 0);
        animatey.setDuration(800);
        animatorSet.playTogether(animatey);
        animatorSet.start();
    }

    public static void Animatebtn3(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        //ObjectAnimator animatex= ObjectAnimator.ofFloat(holder.itemView, "translationY", goesdown == true ? 2000 : -2000, 0);

        ObjectAnimator animatey= ObjectAnimator.ofFloat(view, "translationX", -300, 300, 0);
        animatey.setDuration(200);
        animatorSet.playTogether(animatey);
        animatorSet.start();
    }

    public static void Animatebtn2(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        //ObjectAnimator animatex= ObjectAnimator.ofFloat(holder.itemView, "translationY", goesdown == true ? 2000 : -2000, 0);

        ObjectAnimator animatez= ObjectAnimator.ofFloat(view, "translationZ", -2000, 0);

        ObjectAnimator animatex= ObjectAnimator.ofFloat(view, "translationX", -50, 50, -30, 30, -20, 20, -5, -5, 0);
        ObjectAnimator animatey= ObjectAnimator.ofFloat(view, "translationY", -50, 50, -30, 30, -20, 20, -5, -5, 0);
        animatey.setDuration(100);
        animatorSet.playTogether(animatey,animatex,animatez);
        animatorSet.start();
    }


}
