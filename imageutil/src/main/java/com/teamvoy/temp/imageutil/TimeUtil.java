package com.teamvoy.temp.imageutil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dmytro on 6/16/15.
 */
public class TimeUtil {

    public static String getAbsolutePath(Activity activity, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = activity.getContentResolver().query(uri,
                filePathColumn, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        } else
            return null;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Bitmap decodeFile(Context context, Uri uri) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o);

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = calculateInSampleSize(o, 1024, 800);
            o.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static ArrayList<String> cropImages(Context context, List<String> pathes) {
        ArrayList<String> croppedPathes = new ArrayList<>();
        try {
            for (int i = 0; i < pathes.size(); ++i) {
                // Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri
                        .fromFile(new File(pathes.get(i)))), null, o);

                // Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = 2;
                o.inJustDecodeBounds = false;
                croppedPathes.add(saveBitmapToExternal(BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.fromFile(new File(pathes.get(i)))), null, o2), i));
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return croppedPathes;
    }


    /**
     *
     * @param bitmap bitmap to save
     * @param count number to put after image name, if number is -1 use date
     * @return the absolute path to file
     */
    public static String saveBitmapToExternal(Bitmap bitmap,int count) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String imageFileName = "JPEG_" + String.valueOf(count==-1?timeStamp:count)+ "_";
        File file = new File(dir, imageFileName);

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}