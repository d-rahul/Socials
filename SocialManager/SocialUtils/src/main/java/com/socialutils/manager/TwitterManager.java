package com.socialutils.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.socialutils.twitter.TwitterLoginManager;
import com.socialutils.twitter.TwitterShareManager;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;

/**
 * Created by Priyesh Bhargava on 9/12/15.
 */
public class TwitterManager {

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
        twitterFabricShareManager.setListener(mContext, mShareText, imgUrl);


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
        twitterFabricShareManager.setListener(mContext, mShareText, imgUri);


    }


    public void loadTweets(){


        final long tweetId = 510908133917487104L;
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {

                System.out.println("Data:"+result.toString());
              /*  final TweetView tweetView = new TweetView(EmbeddedTweetsActivity.this, result.data,
                        R.style.tw__TweetDarkWithActionsStyle)
                tweetView.setOnActionCallback(actionCallback)
                myLayout.addView(tweetView);*/
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                // Toast.makeText(...).show();
            }
        });

    }

}
