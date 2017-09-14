package com.socialutils.facebook;
/**
 * @author Priyesh Bhargava
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.socialutils.BaseFragment;
import com.socialutils.R;
import com.socialutils.model.Social;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class FacebookLoginManager extends BaseFragment {


    FBLoginCallBack loginCallBack;
    AppCompatActivity activity;

    public interface FBLoginCallBack {
         void onFBLoginSuccess(AccessToken accessToken,Social social);
         void onFBLoginFailed();
    }


    public void setListener(AppCompatActivity activity, FBLoginCallBack loginCallBack) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.loginCallBack = loginCallBack;
    }

    public FacebookLoginManager() {
        // TODO Auto-generated constructor stub
    }

    ProgressDialog progressDialog;

    CallbackManager callbackManager;
    LoginButton loginButton;
    Social social;
    private static final List<String> PERMISSIONS = Arrays.asList("email,user_location,user_posts,publish_actions");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_social_common, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Login to facebook...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        social = new Social();


        //	loginButton = (LoginButton) view.findViewById(R.id.fb_login_btn);

        if (loginButton == null) {
            loginButton = new LoginButton(getActivity());


            callbackManager = CallbackManager.Factory.create();

            loginButton.setReadPermissions(PERMISSIONS);

            // If using in a fragment
            loginButton.setFragment(this);
            // Other app specific specialization

            loginButton.performClick();


            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    progressDialog.setMessage("Fetching Data ...");
                    progressDialog.show();

                    setUserDetails(loginResult.getAccessToken());

                }

                @Override
                public void onCancel() {
                    onExit();
                    loginCallBack.onFBLoginFailed();

                }

                @Override
                public void onError(FacebookException exception) {
                    onExit();
                    loginCallBack.onFBLoginFailed();
                }

            });
        }
        //


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }


    public void setUserDetails(final AccessToken accessToken) {


        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {


                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // TODO Auto-generated method stub


                        try {


                            JSONObject pictureObject = object.getJSONObject("picture");
                            JSONObject dataObject = pictureObject.getJSONObject("data");
                            String picUrl = dataObject.optString("url");

                            social.setProfileImageUrl(picUrl);
                            social.setName(object.optString("name").toString());
                            social.setEmailId(object.optString("email").toString());
                            social.setAccessToken(accessToken.getToken());
                            social.setFirstName(object.optString("first_name").toString());
                            social.setLastName(object.optString("last_name").toString());
                            social.setSocialId(object.optString("id").toString());


                            LoginManager loginManager = LoginManager.getInstance();
                            loginManager.logOut();


                            onExit();
                            loginCallBack.onFBLoginSuccess(accessToken,social);


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,first_name,last_name,picture");
        request.setParameters(parameters);
        request.executeAsync();


    }

    public Bitmap getBitmapfromUrl(String scr) {
        try {
            URL url = new URL(scr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);
            return bmp;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    public void saveBitmap(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + DOWNLOADED_IMAGE_FOLDER);
        myDir.mkdirs();

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


    class ProgressTaskBar extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub		e.printStackTrace();
            super.onPreExecute();


        }


        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            URL url = null;

            HttpURLConnection connection = null;
            InputStream input = null;

            try {

                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Bitmap bmp = BitmapFactory.decodeStream(input);


            return bmp;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            saveBitmap(result);


         //   loginCallBack.onFBLoginSuccess(social);

            onExit();


        }
    }


    public void onExit() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        activity.getSupportFragmentManager().popBackStack();




    }





}