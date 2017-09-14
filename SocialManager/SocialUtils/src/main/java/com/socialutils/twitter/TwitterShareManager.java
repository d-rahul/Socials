package com.socialutils.twitter;
/**
 * Created by Priyesh Bhargava on 8/12/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.socialutils.Constants;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;


public class TwitterShareManager implements Constants {

    Context mContext;
    String shareText;
    Uri shareImageUri;
    String shareImageUrl;
    private boolean isTwitterOpen = false;


    /**
     * Listener for downloading image from url
     */
    public void setListener(Context mContext, String shareText, String shareImageUrl) {
        this.mContext = mContext;
        this.shareText = shareText;
        this.shareImageUrl = shareImageUrl;
        this.shareImageUri = null;
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        downloadImageTask.execute(shareImageUrl);
    }


    /**
     * Listener for SDcard image
     */
    public void setListener(Context mContext, String shareText, Uri shareImageUri) {
        this.mContext = mContext;
        this.shareText = shareText;
        this.shareImageUrl = null;
        this.shareImageUri = shareImageUri;
        showTwitterDialog(shareText, shareImageUri);
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
            showTwitterDialog(shareText, getImageUri());

        }
    }


    public void setImageUri(Bitmap bitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + DOWNLOADED_IMAGE_FOLDER);
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
        File myDir = new File(root + "/" + DOWNLOADED_IMAGE_FOLDER);
        myDir.mkdirs();
        String fname = DOWNLOADED_IMAGE_NAME;
        File file = new File(myDir, fname);
        return Uri.fromFile(file);


    }


    public void showTwitterDialog(String shareText, Uri uri) {
        TweetComposer.Builder builder = new TweetComposer.Builder(mContext);
        if (shareText != null)
            builder.text(shareText);

        if (uri != null)
            builder.image(uri);


        builder.show();

        isTwitterOpen = true;
    }


}
