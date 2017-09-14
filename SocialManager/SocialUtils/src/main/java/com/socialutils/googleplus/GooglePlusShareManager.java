package com.socialutils.googleplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.plus.PlusShare;
import com.socialutils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Priyesh Bhargava on 8/12/15.
 */
public class GooglePlusShareManager implements Constants {

    private Context mContext;
    String shareText;

    public GooglePlusShareManager(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Google Plus share
     */
    public void onGooglePlusShare(String shareText, String imagePath) {
        this.shareText = shareText;

            DownloadImageTask downloadImageTask = new DownloadImageTask();
            downloadImageTask.execute(imagePath);


    }


    public void onGooglePlusShare(String shareText, Uri imagePath) {


        PlusShare.Builder share = new PlusShare.Builder(mContext);
        share.setText(shareText);
        share.addStream(imagePath);
        share.setType("image/*");
        mContext.startActivity(share.getIntent());

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {


        public DownloadImageTask() {

        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            setImageUri(result);
            onGooglePlusShare(shareText, getImageUri());
        }
    }


    public void setImageUri(Bitmap bitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/"+DOWNLOADED_IMAGE_FOLDER);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = DOWNLOADED_IMAGE_NAME;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public Uri getImageUri() {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/"+DOWNLOADED_IMAGE_FOLDER);
        myDir.mkdirs();
        String fname =DOWNLOADED_IMAGE_NAME;
        File file = new File(myDir, fname);
        return Uri.fromFile(file);


    }
}
