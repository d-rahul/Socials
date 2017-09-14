package com.socialutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.socialutils.facebook.FacebookShareManager;
import com.socialutils.googleplus.GooglePlusShareManager;
import com.socialutils.twitter.TwitterShareManager;

/**
 * Created by Priyesh Bhargava on 9/12/15.
 */
public class SocialShare {

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
        facebookShareManager.setDialogShareListener(activity,title,contentUrl, description, shareCallBack);
        activity.getSupportFragmentManager().beginTransaction().add(fragmentContainerId, facebookShareManager).addToBackStack("").commit();
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

    /**
     *  @param mContext
     * @param shareText
     * @param imgUrl
     */
    public void onTwitterShare(Context mContext,String shareText, String imgUrl) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.twitter.android");

        int textCount=117;

        if(intent==null)
            textCount=140;


        String mShareTextData=shareText;
        String mShareText="";
        if(mShareTextData.length()>textCount){
            mShareText= mShareTextData.substring(0,(textCount-4));
            mShareText+="...";
        }
        else if(mShareTextData.length()==textCount){
            mShareText= mShareTextData.substring(0,(textCount-1));

        }
        else{
            mShareText=shareText;
        }



        TwitterShareManager twitterFabricShareManager = new TwitterShareManager();
        twitterFabricShareManager.setListener(mContext,mShareText, imgUrl);


    }


    /**
     *
     * @param mContext
     * @param shareText
     * @param imgUri

     */
    public void onTwitterShare(Context mContext,String shareText, Uri imgUri) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.twitter.android");

        int textCount=117;

        if(intent==null)
            textCount=140;


        String mShareTextData=shareText;
        String mShareText="";
        if(mShareTextData.length()>textCount){
            mShareText= mShareTextData.substring(0,(textCount-4));
            mShareText+="...";
        }
        else if(mShareTextData.length()==textCount){
            mShareText= mShareTextData.substring(0,(textCount-1));

        }
        else{
            mShareText=shareText;
        }



        TwitterShareManager twitterFabricShareManager = new TwitterShareManager();
        twitterFabricShareManager.setListener(mContext,mShareText, imgUri);


    }

}
