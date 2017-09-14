package com.linkedinsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    boolean isLoginEnable;

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /* LISessionManager.getInstance(getApplicationContext()).clearSession();*/

    }

    public void onLogin(View view) {
        isLoginEnable = true;

        checkForSession();


    }


    private void checkForSession() {

        if (!LISessionManager.getInstance(getApplicationContext()).getSession().isValid()) {
   /*LISessionManager.getInstance(getApplicationContext()).clearSession();*/
            LISessionManager.getInstance(getApplicationContext()).init(this,
                    buildScope(), new AuthListener() {
                        @Override
                        public void onAuthSuccess() {

                            Toast.makeText(getApplicationContext(), "onAuthSuccess ",
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAuthError(LIAuthError error) {

                            Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }, true);

        } else {
            if (isLoginEnable) {
                getUserData();
            } else {
                onLinkedInShare();
            }
        }
    }


// This method is used to make permissions to retrieve data from linkedin

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.W_SHARE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);

        if (isLoginEnable) {
            getUserData();
        } else {
            onLinkedInShare();
        }

    }


      /*Once User's can authenticated,
      It make an HTTP GET request to LinkedIn's REST API using the currently authenticated user's credentials.
      If successful, A LinkedIn ApiResponse object containing all of the relevant aspects of the server's response will be returned.
     */

    public void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    Toast.makeText(getApplicationContext(), "Login data success ",
                            Toast.LENGTH_LONG).show();
                    setUserProfile(result.getResponseDataAsJson());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                Toast.makeText(getApplicationContext(), "Login data failed ",
                        Toast.LENGTH_LONG).show();

            }
        });
    }
 /*
       Set User Profile Information in Navigation Bar.
     */

    public void setUserProfile(JSONObject response) {

        try {

            System.out.println("" + response);
          /*  user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());

            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_pic);*/

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onLinkedInShare(View view) {
        isLoginEnable = false;
        checkForSession();

    }


    private void onLinkedInShare() {
        String payload = "{\n" +
                "  \"comment\": \"Check out developer.linkedin.com!\",\n" +
                "  \"content\": {\n" +
                "    \"title\": \"LinkedIn Developers Resources\",\n" +
                "    \"description\": \"Leverage LinkedIn's APIs to maximize engagement\",\n" +
                "    \"submitted-url\": \"https://developer.linkedin.com\",  \n" +
                "    \"submitted-image-url\": \"https://example.com/logo.png\"\n" +
                "  },\n" +
                "  \"visibility\": {\n" +
                "    \"code\": \"anyone\"\n" +
                "  }  \n" +
                "}";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

        apiHelper.postRequest(this, shareUrl, payload, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                Toast.makeText(getApplicationContext(), "Share Success!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making POST request!
                Toast.makeText(getApplicationContext(), "Share Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

