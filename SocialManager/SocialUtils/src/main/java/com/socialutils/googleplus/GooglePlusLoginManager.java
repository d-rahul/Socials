package com.socialutils.googleplus;


/**
 * @author Priyesh Bhargava
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.socialutils.BaseFragment;
import com.socialutils.Constants;
import com.socialutils.R;
import com.socialutils.model.Social;
import com.socialutils.utils.SharedPrefrenceUtil;
import com.socialutils.utils.SocialUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GooglePlusLoginManager extends BaseFragment implements Constants, ConnectionCallbacks,
        OnConnectionFailedListener {

    GPLoginCallBack loginCallBack;
    GPLogoutCallBack logoutCallBack;
    Social social;
    AppCompatActivity mContext;
    ProgressDialog progressDialog;

    public interface GPLoginCallBack {
        public void onGPLoginSuccess(Social social);

        public void onGPLoginFailed();
    }


    public void setLoginListener(AppCompatActivity mContext, GPLoginCallBack loginCallBack) {
        this.mContext = mContext;
        this.loginCallBack = loginCallBack;


    }


    /**
     * logout
     */


    public interface GPLogoutCallBack {
        public void onGPLogoutSuccess();

        public void onGPLogoutFailed();
    }


    public void setLogoutListener(GPLogoutCallBack logoutCallBack) {

        this.logoutCallBack = logoutCallBack;


    }


    /* google plus data */

    private static final String TAG = "LoginActivity";
    private static final int PROFILE_PIC_SIZE = 400;
    protected static final int REQUEST_CODE_TOKEN_AUTH = 0;
    private GoogleApiClient mGoogleApiClient;
    /**/
    String userName;


    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    // private ImageButton btnSignIn;
    String emailId;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (view == null) {
            view = inflater.inflate(R.layout.layout_social_common, container, false);

        }

        social = new Social();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        if (loginCallBack != null && (!SharedPrefrenceUtil.getPrefrence(mContext, DB_IS_GP_LOGIN, false)))
            onSignInWithGplus();
        /*else
            //signOutFromGplus();
*/
        return view;
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        if ((!SharedPrefrenceUtil.getPrefrence(mContext, DB_IS_GP_LOGIN, false)))
            mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (mGoogleApiClient.isConnected()) {

            mGoogleApiClient.disconnect();

        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {

        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(mContext, SocialUtils.REQUEST_CODE_GOOGLE_PLUS_LOGIN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        //	loginCallBack.onGPLoginFailed();


        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), mContext, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {

        if(responseCode==getActivity().RESULT_OK) {

            if (requestCode == SocialUtils.REQUEST_CODE_GOOGLE_PLUS_LOGIN) {
                if (responseCode != getActivity().RESULT_OK) {
                    mSignInClicked = false;
                }

                mIntentInProgress = false;

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }

            }
        }
       else if(responseCode==getActivity().RESULT_CANCELED) {
            loginCallBack.onGPLoginFailed();
            onExit();
        }




    }


    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;

        // Get user's information
        if (!SharedPrefrenceUtil.getPrefrence(mContext, DB_IS_GP_LOGIN, false))
            getProfileInformation();


    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {


            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                userName = currentPerson.getName().getGivenName();


                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                emailId = Plus.AccountApi.getAccountName(mGoogleApiClient);
                social.setFirstName(currentPerson.getName().getGivenName());
                social.setLastName(currentPerson.getName().getFamilyName());
                social.setSocialId(currentPerson.getId());
                social.setName(userName);
                social.setEmailId(emailId);
                social.setProfileImageUrl(personPhotoUrl);
                //Toast.makeText(getActivity(), "Name="+userName,Toast.LENGTH_SHORT).show();
                //

                // Log.e(TAG, "Name: " + personName + ", plusProfile: "
                // + personGooglePlusProfile + ", email: " + email
                // + ", Image: " + personPhotoUrl);

                // txtName.setText(personName);
                // txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                // personPhotoUrl = personPhotoUrl.substring(0,
                // personPhotoUrl.length() - 2)
                // + PROFILE_PIC_SIZE;
                //
                // new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

                final String SCOPES = "https://www.googleapis.com/auth/plus.login "
                        + "https://www.googleapis.com/auth/drive.file";

                AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String token = null;

                        try {
                            token = GoogleAuthUtil.getToken(mContext, Plus.AccountApi.getAccountName(mGoogleApiClient), "oauth2:" + SCOPES);
                        } catch (IOException transientEx) {
                            // Network or server error, try later
                            Log.e(TAG, transientEx.toString());
                        } catch (UserRecoverableAuthException e) {
                            // Recover (with e.getIntent())
                            Log.e(TAG, e.toString());
                            Intent recover = e.getIntent();
                            mContext.startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
                        } catch (GoogleAuthException authEx) {
                            // The call is not ever expected to succeed
                            // assuming you have already verified that
                            // Google Play services is installed.
                            Log.e(TAG, authEx.toString());
                        }
                        return token;
                    }

                    @Override
                    protected void onPostExecute(String token) {
                        Log.i(TAG, "Access token retrieved:" + token);

                        SharedPrefrenceUtil.setPrefrence(mContext, Constants.DB_IS_GP_LOGIN, true);

                        social.setAccessToken(token);
                        mGoogleApiClient.clearDefaultAccountAndReconnect();
                        loginCallBack.onGPLoginSuccess(social);
                        onExit();

                    }
                };
                task.execute();
            } else {

                loginCallBack.onGPLoginFailed();
                onExit();


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();

    }
    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.main, menu);
    // return true;
    // }
    /**
     * Button on click listener
     * */

    /**
     * Sign-in into google
     */
    public void onSignInWithGplus() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Login to google+...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        if (!mGoogleApiClient.isConnecting()) {

            mSignInClicked = true;
            resolveSignInError();
        }

    }

    /**
     * Sign-out from google
     */
    public void signOutFromGplus() {
        /*if (mGoogleApiClient.isConnected()) {

		}*/
        //Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        /*	mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();*/
			/*logoutCallBack.onGPLogoutSuccess();
		}
		else{
			logoutCallBack.onGPLogoutFailed();
		}*/
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();

                        }
                    });
        }
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            saveBitmap(result);
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


    public void onExit() {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            public void run() {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                mContext.getSupportFragmentManager().popBackStack();
                SharedPrefrenceUtil.setPrefrence(mContext, DB_IS_GP_LOGIN, false);
            }
        });
    }
}