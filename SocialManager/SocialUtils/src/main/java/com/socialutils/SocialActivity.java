package com.socialutils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.socialutils.facebook.FacebookLoginManager;
import com.socialutils.facebook.FacebookShareManager;
import com.socialutils.manager.FacebookManager;
import com.socialutils.model.Social;

/**
 * Created by hb on 9/2/16.
 */
public class SocialActivity extends AppCompatActivity implements FacebookLoginManager.FBLoginCallBack,Constants, FacebookShareManager.FBShareCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if(getIntent()!=null){
            if(getIntent().getAction().equals(SOCIAL_ACTION_FB_LOGIN)){
                FacebookManager facebookManager = new FacebookManager();
                facebookManager.onFacebookLogin(this, R.id.frame_content, this);
            }
           else if(getIntent().getAction().equals(SOCIAL_ACTION_FB_DIALOG_SHARE)){
                String title=getIntent().getExtras().getString(FB_SHARE_TITLE);
                String url=getIntent().getExtras().getString(FB_SHARE_URL);
                String desc=getIntent().getExtras().getString(FB_SHARE_DESCRIPTION);
                FacebookManager facebookManager = new FacebookManager();
                facebookManager.onFacebookShare(title,url,desc,this, R.id.frame_content, this);
            }



        }





    }

    @Override
    public void onFBLoginSuccess(AccessToken accessToken, Social social) {

        Intent i=new Intent();
        i.putExtra("Data", social);
        setResult(RESULT_OK,i);
        finish();//finishing activity
    }

    @Override
    public void onFBLoginFailed() {
        Intent i=new Intent();
        setResult(RESULT_CANCELED,i);
        finish();//finishing activity
    }


    @Override
    public void onFBShareSuccess() {
        Intent i=new Intent();
        setResult(RESULT_OK,i);
        finish();//finishing activity
    }

    @Override
    public void onFBShareFailed() {
        Intent i=new Intent();
        setResult(RESULT_CANCELED,i);
        finish();//finishing activity
    }
}
