package com.linkedinsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONObject;

public class LinkedInUser extends AppCompatActivity {

    String YOUR_API_KEY = "75hnq8s9a4o8eg";
    String SCOPE = "";
    String STATE = "oeoogP4jObrcdlsE";
    String YOUR_REDIRECT_URI = "http://www.google.com/";


    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  ShareComment();
     // getUserData();
   //  getConnections(null);


        Intent i=new Intent(getApplicationContext(),AuthActivity.class);
        startActivity(i);
    }

    public void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(LinkedInUser.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    setUserProfile(result.getResponseDataAsJson());
              //      getConnections(null);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                System.out.println("onApiError");
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });
    }
 /*
       Set User Profile Information in Navigation Bar.
     */


    public void setUserProfile(JSONObject response) {

        try {

           /* user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());

            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_pic);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ShareComment() {


        String shareJsonText = "{ \n" +
                "   \"comment\":\"" + "Share Text" + "\"," +
                "   \"visibility\":{ " +
                "      \"code\":\"anyone\"" +
                "   }," +
                "   \"content\":{ " +
                "      \"title\":\"Android LinkedIn Integration/Login and Make User Profile\"," +
                "      \"description\":\"Login Integration with LinkedIn\"," +
                "      \"submitted-url\":\"https://www.numetriclabz.com/android-linkedin-integrationlogin-and-make-userprofile\"," +
                "      \"submitted-image-url\":\"https://www.numetriclabz.com/?attachment_id=11320\"" +
                "   }" +
                "}";
        // Call the APIHealper.getInstance method and pass the current context.

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

                /* We need to share text call apiHelper.postRequest method. This Method post the text on your linkedin profile. If successful, it will return reposne from Linkedin containing json string. In this Json string, containing statuscode: 200 and shared url.
                */

        apiHelper.postRequest(LinkedInUser.this, shareUrl, shareJsonText, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                Log.e("Response", apiResponse.toString());
                Toast.makeText(getApplicationContext(), "Shared Sucessfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onApiError(LIApiError error) {
                Log.e("Response", error.toString());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getConnections(View view) {

        String API = " https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=" + YOUR_API_KEY +
                "&scope=" + SCOPE + "&state=" + STATE + "&redirect_uri=" + YOUR_REDIRECT_URI;

        String authCall="https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=75hnq8s9a4o8eg&redirect_uri=https://www.google.co.in/&state=7896547895&scope=r_basicprofile";

        String LINKED_IN_CONNECTIONS_API = "http://api.linkedin.com/v1/people/~/connections";
        String urlStr = LINKED_IN_CONNECTIONS_API;
        urlStr += "?format=json";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, authCall, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    System.out.println("Res Data=" + result.getResponseDataAsJson());
                    //setUserProfile(result.getResponseDataAsJson());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                System.out.println("onApiError");
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });


    }

}
