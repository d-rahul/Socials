package com.socialutils.twitter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.SerializedName;
import com.socialutils.BaseFragment;
import com.socialutils.Constants;
import com.socialutils.R;
import com.socialutils.model.Social;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;


public class TwitterLoginManager extends BaseFragment implements Constants {
    public static final int RC_SIGN_IN = 140;
    TwitterLoginButton loginButton;
    TwitterLoginCallback twitterLoginCallback;
    TwitterEmailCallback twitterEmailCallback;
    AppCompatActivity activity;
    ProgressDialog progressDialog;
    View view;
    Social social;

    public interface TwitterLoginCallback {
        public void onTwitterLoginSuccess(Social social);

        public void onTwitterLoginFailed();
    }

    public interface TwitterEmailCallback {
        public void onTwitterEmailSuccess();

        public void onTwitterEmailFailed();
    }


    public void setListener(AppCompatActivity activity, TwitterLoginCallback twitterLoginCallback) {
        this.activity=activity;
        this.twitterLoginCallback = twitterLoginCallback;
    }

    public void setListenerForEmail(TwitterEmailCallback twitterEmailCallback) {
        this.twitterEmailCallback = twitterEmailCallback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

     if(view==null)
         view = inflater.inflate(R.layout.layout_social_common, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Login to twitter...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();



        if (twitterEmailCallback == null) {
            loginButton = new TwitterLoginButton(activity);

            loginButton.setCallback(new Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> result) {
                    social = new Social();
                    TwitterSession session = Twitter.getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    String token = authToken.token;
                    String secret = authToken.secret;
                    social.setName(result.data.getUserName());
                    social.setAccessToken(token);
                    social.setSocialId(String.valueOf(result.data.getUserId()));
                    getFollower(session,result.data.getUserId());
                    Twitter.getApiClient(session).getAccountService()
                            .verifyCredentials(true, false, new Callback<User>() {


                                @Override
                                public void success(Result<User> userResult) {

                                    User user = userResult.data;
                                    social.setEmailId(user.email);
                                    social.setProfileImageUrl(user.profileImageUrlHttps);
                                    onExit();
                                    twitterLoginCallback.onTwitterLoginSuccess(social);

                                }

                                @Override
                                public void failure(TwitterException e) {
                                    onExit();
                                    twitterLoginCallback.onTwitterLoginSuccess(social);
                                }

                            });


                }

                @Override
                public void failure(TwitterException arg0) {
                    // TODO Auto-generated method stub
                    loginButton.removeCallbacks(null);
                    onExit();
                    twitterLoginCallback.onTwitterLoginFailed();

                }
            });

            loginButton.performClick();

        } else {
            requestForTwitterEmailId();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    public void requestForTwitterEmailId() {

        TwitterSession session = Twitter.getSessionManager().getActiveSession();


        TwitterAuthClient authClient = new TwitterAuthClient();

        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }


        });
    }
    public void onExit() {

        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            public void run() {

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        activity.getSupportFragmentManager().popBackStack();
            }
        });



    }

    /******************************/

    //data model
    public class Followers {
        @SerializedName("users")
        public final List<User> users;

        public Followers(List<User> users) {
            this.users = users;
        }
    }

    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        public CustomService getCustomService() {
            return getService(CustomService.class);
        }

    }

    interface CustomService {@GET("/1.1/followers/list.json")
                             void show(@Query("user_id") Long userId, @Query("screen_name") String
                    var, @Query("skip_status") Boolean var1, @Query("include_user_entities") Boolean var2, @Query("count") Integer var3, Callback < Followers > cb);
    }




    void getFollower(TwitterSession session,Long userID){
    new MyTwitterApiClient(session).getCustomService().show(userID, null, true, true, 100, new Callback < Followers > () {@Override
        public void success(Result < Followers > result) {
            Log.i("Get success", "" + result.data.users.size());
        }

        @Override
        public void failure(TwitterException e) {

        }
    });
    }



}
