package com.jmarkstar.mfc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by jmarkstar on 13/06/2017.
 */
public class MfcDialog extends DialogFragment {

    private static final String DIALOG_FRAGMENT_TAG = "MfcDialog";
    private static final String SETUP_TAG = "setup";

    private TextView mTvTitle;
    private TextView mTvGalleryOption;
    private TextView mTvCameraOption;

    private MfcSetup mSetup;

    private static MfcDialog newInstance(MfcSetup setup){
        MfcDialog dialog = new MfcDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SETUP_TAG, setup);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static MfcDialog build(MfcSetup mfcSetup){
        return newInstance(mfcSetup);
    }

    /*Default settings*/
    public static MfcDialog build(Context context){
        return newInstance(new MfcSetup.Builder(context).build());
    }

    public MfcDialog show(AppCompatActivity activity){
        return show(activity.getSupportFragmentManager());
    }

    public MfcDialog show(Fragment fragment){
        return show(fragment.getChildFragmentManager());
    }

    private MfcDialog show(FragmentManager fragmentManager){
        super.show(fragmentManager, DIALOG_FRAGMENT_TAG);
        return this;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_dialog, null, false);

        onInitViews(view);
        onSetup();

        return view;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow()!=null){
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    private void onInitViews(View root){
        mTvTitle = (TextView)root.findViewById(R.id.mfc_dialog_title);
        mTvGalleryOption = (TextView)root.findViewById(R.id.mfc_gallery_option);
        mTvCameraOption = (TextView)root.findViewById(R.id.mfc_camera_option);
    }

    private void onSetup(){
        if(getArguments()!=null){
            mSetup = getArguments().getParcelable(SETUP_TAG);
        }

        int accentColor = ContextCompat.getColor(getContext(), mSetup.getAccentColor());

        Drawable galleryIcon = ContextCompat.getDrawable(getContext(), mSetup.getDialogGalleryIcon());
        galleryIcon = DrawableCompat.wrap(galleryIcon);
        DrawableCompat.setTint(galleryIcon, accentColor );
        DrawableCompat.setTintMode(galleryIcon, PorterDuff.Mode.SRC_IN);

        Drawable cameraIcon = ContextCompat.getDrawable(getContext(), mSetup.getDialogCameraIcon());
        cameraIcon = DrawableCompat.wrap(cameraIcon);
        DrawableCompat.setTint(cameraIcon, accentColor );
        DrawableCompat.setTintMode(cameraIcon, PorterDuff.Mode.SRC_IN);

        mTvTitle.setText(mSetup.getDialogTitle());
        mTvGalleryOption.setCompoundDrawablesWithIntrinsicBounds(galleryIcon, null, null, null);
        mTvCameraOption.setCompoundDrawablesWithIntrinsicBounds(cameraIcon, null, null, null);
    }
}
