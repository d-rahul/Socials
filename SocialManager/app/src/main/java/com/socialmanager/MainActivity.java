package com.socialmanager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.socialutils.model.Social;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, new HomeFragment()).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        /*if (requestCode ==SocialUtils.REQUEST_CODE_GOOGLE_PLUS_LOGIN) {
            GooglePlusLoginManager fragment = (GooglePlusLoginManager) getSupportFragmentManager()
                    .findFragmentById(R.id.frame_content);
            if (fragment instanceof GooglePlusLoginManager) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }

        }
        else if(requestCode== SocialUtils.REQUEST_CODE_TWITTER_LOGIN){
            TwitterLoginManager fragment = (TwitterLoginManager) getSupportFragmentManager()
                    .findFragmentById(R.id.frame_content);
            if (fragment instanceof TwitterLoginManager) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

        else if(requestCode== SocialUtils.REQUEST_CODE_FACEBOOK_SHARE){
            FacebookShareManager fragment = (FacebookShareManager) getSupportFragmentManager()
                    .findFragmentById(R.id.frame_content);
            if (fragment instanceof FacebookShareManager) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
*/



        if(requestCode==Utils.REQUEST_CODE_FB_LOGIN){
            Social mSocial= (Social) data.getSerializableExtra("Data");
        }
        else if(requestCode==Utils.REQUEST_CODE_FB_SHARE_DIALOG){

        }


        else  {
            super.onActivityResult(requestCode, resultCode, data);

        }


    }
}