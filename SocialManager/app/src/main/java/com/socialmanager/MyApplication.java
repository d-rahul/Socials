package com.socialmanager;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {
   private static final String TWITTER_KEY = "xZZVRTiYL9roi10oGHOh8F4IP";
       private static final String TWITTER_SECRET = "9mBunQccYGJwrnbIhSnzPGaxcUAvX6eZ44BIir740NzmCUoVBy";
    @Override
    public void onCreate() {
        super.onCreate();

        /***
         *Facebook SDK Init
         */

     FacebookSdk.sdkInitialize(getApplicationContext());


        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

    }
}
