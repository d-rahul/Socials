package com.linkedinsample;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.AccessToken;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    WebView wvAuth;

    String REDIRECT_URI = "http://www.google.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        REDIRECT_URI = Uri.encode(REDIRECT_URI);

        setContentView(R.layout.activity_auth);
        getMyConnection("");
      /*  String authCall = "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=75hnq8s9a4o8eg&redirect_uri=" + REDIRECT_URI + "&state=7896547895&scope=r_basicprofile";
        wvAuth = (WebView) findViewById(R.id.wv_auth);
        wvAuth.setWebViewClient(new MyBrowser());
        wvAuth.loadUrl(authCall);
        wvAuth.getSettings().setLoadsImagesAutomatically(true);
        wvAuth.getSettings().setJavaScriptEnabled(true);*/
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://www.google.com/")) {
                System.out.println("CODE" + url);
                Uri uri = Uri.parse(url);
                String authCode = uri.getQueryParameter("code");
                getAccessToken(authCode);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            System.out.println("onPageFinished" + url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            System.out.println("onPageStarted" + url);
        }
    }

    String authParam = "";

    public void getAccessToken(String authCode) {
        String authUrl = "https://www.linkedin.com/uas/oauth2/accessToken";

        String GRANT_TYPE = "grant_type";


        authParam = GRANT_TYPE + "=authorization_code" +
                "&code=" + authCode +
                "&redirect_uri=" + REDIRECT_URI +
                "&client_id=75hnq8s9a4o8eg&client_secret=oeoogP4jObrcdlsE";
        JSONObject authParamJson = new JSONObject();
        try {


            //   authParamJson.put("Content-Type","application/x-www-form-urlencoded");
            authParamJson.put("grant_type", "authorization_code");
            authParamJson.put("code", authCode);
            authParamJson.put("redirect_uri", REDIRECT_URI);
            authParamJson.put("client_id", "75hnq8s9a4o8eg");
            authParamJson.put("client_secret", "oeoogP4jObrcdlsE");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

        apiHelper.postRequest(this, authUrl, authParamJson, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!

                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                getMyConnection("");
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making POST request!
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }


    void getMyConnection(String accessToekn) {
        String host = "api.linkedin.com";
        String topCardUrl = "https://" + host + "/v1/people/~?";
     AccessToken token=   LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken();


        topCardUrl += "oauth2_access_token=" + token.getValue();

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

        apiHelper.getRequest(this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!

                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making POST request!
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
