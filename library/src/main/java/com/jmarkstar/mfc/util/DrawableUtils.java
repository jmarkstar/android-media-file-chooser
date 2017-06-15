package com.jmarkstar.mfc.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by jmarkstar on 13/06/2017.
 */
public class DrawableUtils {

    public static Drawable tint(Context context, @DrawableRes int drawableRes, int colorRes){
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, colorRes );
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}
