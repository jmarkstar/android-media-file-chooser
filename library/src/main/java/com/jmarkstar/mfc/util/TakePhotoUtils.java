package com.jmarkstar.mfc.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jmarkstar on 06/03/2017.
 */
public class TakePhotoUtils {

    /**
     * @return path
     * */
    public static String createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_"+ timeStamp;
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(imageFileName, ".png", storageDirectory);
            return image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap setReducedImageSize(int targetWidth, int targetHeight, String mImageFileLocation){
        BitmapFactory.Options bmOtpions = new BitmapFactory.Options();
        bmOtpions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOtpions);
        int cameraImageWidth = bmOtpions.outWidth;
        int cameraImageHeight = bmOtpions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetWidth, cameraImageHeight/targetHeight);
        bmOtpions.inSampleSize = scaleFactor;
        bmOtpions.inJustDecodeBounds = false;

        Bitmap resizedBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOtpions);
        return resizedBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, String mImageFileLocation){
        ExifInterface exifInterface = null;
        try{
            exifInterface = new ExifInterface(mImageFileLocation);
        }catch(IOException e){
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90: matrix.setRotate(90);break;
            case ExifInterface.ORIENTATION_ROTATE_180: matrix.setRotate(180);break;
            case ExifInterface.ORIENTATION_ROTATE_270: matrix.setRotate(270);break;
            default:
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void storeBitmap(Bitmap bitmap, String filename){
        FileOutputStream out = null;
        try {
            File file = new File(filename);
            if(file.exists())
                file.delete();

            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 65, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        int measureAside = 0;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            measureAside = bitmap.getHeight();
            output = Bitmap.createBitmap(measureAside, measureAside, Bitmap.Config.ARGB_8888);
        } else {
            measureAside = bitmap.getWidth();
            output = Bitmap.createBitmap(measureAside, measureAside, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        float r = measureAside/2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getBitmapFromFilepath(String filepath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
        return bitmap;
    }
}
