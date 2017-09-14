package com.socialutils.manager;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.socialutils.googleplus.GooglePlusLoginManager;
import com.socialutils.googleplus.GooglePlusShareManager;

/**
 * Created by Priyesh Bhargava on 9/12/15.
 */
public class GooglePlusManager {

    /**
     *
     * @param activity
     * @param fragmentContainerId
     * @param gpLoginCallBack
     */
    public void onGooglePlusLogin(AppCompatActivity activity, int fragmentContainerId, GooglePlusLoginManager.GPLoginCallBack gpLoginCallBack) {
        GooglePlusLoginManager googlePlusManager=new GooglePlusLoginManager();
        googlePlusManager.setLoginListener(activity, gpLoginCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, googlePlusManager).addToBackStack("").commit();
    }


    /**
     *
     * @param mContext
     * @param shareText
     * @param imgUrl
     */
    public void onGooglePlusShare(Context mContext,String shareText,String imgUrl) {
        GooglePlusShareManager googlePlusShareManager=new GooglePlusShareManager(mContext);
        googlePlusShareManager.onGooglePlusShare(shareText, imgUrl);


    }


    /**
     *
     * @param mContext
     * @param shareText
     * @param imgUri
     */
    public void onGooglePlusShare(Context mContext,String shareText,Uri imgUri) {
        GooglePlusShareManager googlePlusShareManager=new GooglePlusShareManager(mContext);
        googlePlusShareManager.onGooglePlusShare(shareText,imgUri);


    }

}
