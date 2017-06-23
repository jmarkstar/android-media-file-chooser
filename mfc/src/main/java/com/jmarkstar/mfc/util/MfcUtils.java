package com.jmarkstar.mfc.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jmarkstar on 15/06/2017.
 */
public class MfcUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor (AppCompatActivity activity, @ColorRes int colorRs){
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, colorRs));
    }
}
