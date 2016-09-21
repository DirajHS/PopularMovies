/**
 * Created by Diraj H S on 9/15/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies;

import android.app.Activity;
import android.content.Context;

public class AnimationHandling {

    public enum ANIM_TYPE {
        START,      //start of an activity
        CLOSE,      //back press of an activity
    }

    public static void animateScreen(Context context, ANIM_TYPE animType) {
        switch (animType) {
            case START:
                ((Activity) context).overridePendingTransition(R.anim.splash_slide_in_anim_set, R.anim.splash_slide_out_anim_set);
                break;
            case CLOSE:
                ((Activity) context).overridePendingTransition( R.anim.splash_slide_in_anim_reverse_set,
                        R.anim.splash_slide_out_anim_reverse_set);
                break;
        }
    }
}

