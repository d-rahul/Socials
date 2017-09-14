package com.socialutils;

import android.support.v7.app.AppCompatActivity;

import com.socialutils.facebook.FacebookLoginManager;
import com.socialutils.googleplus.GooglePlusLoginManager;
import com.socialutils.twitter.TwitterLoginManager;

/**
 * Created by hb on 9/12/15.
 */
public class SocialLogin {


    /**
     *
     * @param activity
     * @param fragmentContainerId
     * @param fbLoginCallBack
     */
    public void onFacebookLogin(AppCompatActivity activity, int fragmentContainerId, FacebookLoginManager.FBLoginCallBack fbLoginCallBack) {
        FacebookLoginManager facebookLoginManager=new FacebookLoginManager();
        facebookLoginManager.setListener(activity,fbLoginCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookLoginManager).addToBackStack("").commit();
    }


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
     * @param activity
     * @param fragmentContainerId
     * @param twitterLoginCallback
     */
    public void onTwitterLogin(AppCompatActivity activity, int fragmentContainerId, TwitterLoginManager.TwitterLoginCallback twitterLoginCallback) {

        TwitterLoginManager fabricLoginManager=new TwitterLoginManager();
        fabricLoginManager.setListener(activity,twitterLoginCallback);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, fabricLoginManager).addToBackStack("").commit();
    }


}
