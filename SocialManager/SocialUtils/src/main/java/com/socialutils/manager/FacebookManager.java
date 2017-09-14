package com.socialutils.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.socialutils.facebook.FacebookLoginManager;
import com.socialutils.facebook.FacebookShareManager;

/**
 * Created by Priyesh Bhargava on 9/12/15.
 */
public class FacebookManager {

    /**
     *
     * @param activity
     * @param fragmentContainerId
     * @param fbLoginCallBack
     */
    public void onFacebookLogin(AppCompatActivity activity, int fragmentContainerId, FacebookLoginManager.FBLoginCallBack fbLoginCallBack) {
        FacebookLoginManager facebookLoginManager=new FacebookLoginManager();
        facebookLoginManager.setListener(activity, fbLoginCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookLoginManager).addToBackStack("").commit();
    }


    /**
     * onFacebookShare for sharing title ,description and contentUrl(Redirect url)
     */

    /**
     *
     * @param title
     * @param contentUrl
     * @param description
     * @param activity
     * @param fragmentContainerId
     * @param shareCallBack
     */
    public void onFacebookShare(String title,String contentUrl,String description,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack) {

        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setDialogShareListener(activity, title, contentUrl, description, shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();
    }


    /**
     *
     * @param imgUrl
     * @param activity
     * @param fragmentContainerId
     * @param shareCallBack
     */
    public void shareImageOnFacebook(String imgUrl,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack) {

        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setDialogShareListener(activity,"", imgUrl,"", shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();
    }

    /**
     *
     * @param shareBitmap
     * @param activity
     * @param fragmentContainerId
     * @param shareCallBack
     */
    public void shareImageOnFacebook(Bitmap shareBitmap,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack) {

        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setDialogShareListener(activity,shareBitmap, shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();

    }


    public void shareImageOnFacebook(int shareResId,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack) {


        Bitmap mBitmap = BitmapFactory.decodeResource(activity.getResources(), shareResId);

        if(mBitmap==null){
            Toast.makeText(activity,"Image corrupted!",Toast.LENGTH_SHORT).show();
            return;

        }
        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setDialogShareListener(activity,mBitmap, shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();

    }


    /**
     *
     * @param imgUri
     * @param activity
     * @param fragmentContainerId
     * @param shareCallBack
     */

    public void shareImageOnFacebook(Uri imgUri,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack) {
        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setDialogShareListener(activity, imgUri, shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();


    }


    /**
     *
     * @param shareText
     * @param activity
     * @param fragmentContainerId
     * @param shareCallBack
     */

    public void shareTextOnFacebook(String shareText,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack){
        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setTextShareListener(activity, shareText, shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();
    }

    /**
     *
     * @param shareText
     * @param activity
     * @param fragmentContainerId
     * @param shareCallBack
     */

    public void shareLinkOnFacebook(String shareText,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack){
        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setDialogShareListener(activity,"",shareText,"", shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();
    }

    /**
     *
     * @param vdoUri
     * @param activity
     * @param fragmentContainerId
     * @param shareCallBack
     */
    public void shareVDOOnFacebook(Uri vdoUri,AppCompatActivity activity, int fragmentContainerId,FacebookShareManager.FBShareCallBack shareCallBack){
        FacebookShareManager facebookShareManager=new FacebookShareManager();
        facebookShareManager.setVDODialogShareListener(activity, vdoUri,shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();
    }



}
