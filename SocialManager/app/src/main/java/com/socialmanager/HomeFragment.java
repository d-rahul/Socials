package com.socialmanager;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.widget.LikeView;
import com.socialutils.Constants;
import com.socialutils.facebook.FacebookLoginManager;
import com.socialutils.facebook.FacebookShareManager;
import com.socialutils.googleplus.GooglePlusLoginManager;
import com.socialutils.manager.FacebookManager;
import com.socialutils.manager.GooglePlusManager;
import com.socialutils.manager.TwitterManager;
import com.socialutils.model.Social;
import com.socialutils.twitter.TwitterLoginManager;

import java.io.File;


public class HomeFragment extends BaseFragment implements View.OnClickListener, FacebookLoginManager.FBLoginCallBack, GooglePlusLoginManager.GPLoginCallBack, TwitterLoginManager.TwitterLoginCallback, FacebookShareManager.FBShareCallBack {


    Button btnFBLogin, btnFBShare, btnFBShareImg, btnFBShareLink, btnFBShareTxt, btnFBShareVdo, btnGPLogin, btnGPShare, btnTwitterLogin, btnTwitterShare, btnTwitterLoadTweet, btnFbLike;
    View view;
    LikeView mLikeView;

    String imgUrl = "https://static.pexels.com/photos/248797/pexels-photo-248797.jpeg";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnFBLogin = (Button) view.findViewById(R.id.btn_fb_login);
        btnFBShare = (Button) view.findViewById(R.id.btn_fb_share);

        btnFBShareImg = (Button) view.findViewById(R.id.btn_fb_share_img);
        btnFBShareLink = (Button) view.findViewById(R.id.btn_fb_share_link);
        btnFBShareTxt = (Button) view.findViewById(R.id.btn_fb_share_txt);
        btnFBShareVdo = (Button) view.findViewById(R.id.btn_fb_share_vdo);


        btnGPLogin = (Button) view.findViewById(R.id.btn_gp_login);
        btnGPShare = (Button) view.findViewById(R.id.btn_gp_share);
        btnTwitterLogin = (Button) view.findViewById(R.id.btn_twitter_login);
        btnTwitterShare = (Button) view.findViewById(R.id.btn_twitter_tweet);
        btnFbLike = (Button) view.findViewById(R.id.btn_fb_like);
        btnTwitterLoadTweet = (Button) view.findViewById(R.id.btn_twitter_load_tweet);

        /**
         * setting listener
         */
        btnFBLogin.setOnClickListener(this);
        btnFBShare.setOnClickListener(this);

        btnFBShareImg.setOnClickListener(this);
        btnFBShareLink.setOnClickListener(this);
        btnFBShareTxt.setOnClickListener(this);
        btnFBShareVdo.setOnClickListener(this);


        btnGPLogin.setOnClickListener(this);
        btnGPShare.setOnClickListener(this);
        btnTwitterLogin.setOnClickListener(this);
        btnTwitterShare.setOnClickListener(this);
        btnFbLike.setOnClickListener(this);
        btnTwitterLoadTweet.setOnClickListener(this);
        mLikeView = (LikeView) view.findViewById(R.id.like_view);
       /* mLikeView.setObjectIdAndType(
                "https://www.facebook.com/TimesofIndia/",
                LikeView.ObjectType.OPEN_GRAPH);*/


        mLikeView.setLikeViewStyle(LikeView.Style.BOX_COUNT);
        mLikeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
  /*      mLikeView.setObjectIdAndType(
                "http://inthecheesefactory.com/blog/understand-android-activity-launchmode/en",
                LikeView.ObjectType.OPEN_GRAPH);*/


        mLikeView.setObjectIdAndType(
                "https://www.facebook.com/TimesofIndia/",
                LikeView.ObjectType.PAGE);
        mLikeView.setOnErrorListener(new LikeView.OnErrorListener() {
            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        });


    }

    @Override
    public void onClick(View view) {
        FacebookManager facebookManager = new FacebookManager();
        GooglePlusManager googlePlusManager = new GooglePlusManager();
        TwitterManager twitterManager = new TwitterManager();


        switch (view.getId()) {
            case R.id.btn_fb_login: {
                Intent i = new Intent();
                i.setAction(Constants.SOCIAL_ACTION_FB_LOGIN);
                i.setType("text/plain");
                getActivity().startActivityForResult(i, Utils.REQUEST_CODE_FB_LOGIN);

            }
                //   facebookManager.likeFBPage(getActivity());
                //  facebookManager.onFacebookLogin((AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;
            case R.id.btn_fb_share:
                Intent i=new Intent();
                i.setAction(Constants.SOCIAL_ACTION_FB_DIALOG_SHARE);
                i.putExtra(Constants.FB_SHARE_TITLE, "Sample App");
                i.putExtra(Constants.FB_SHARE_URL,"http://plurro.com/");
                i.putExtra(Constants.FB_SHARE_DESCRIPTION,"test description from social manager");
                i.setType("text/plain");
                getActivity().startActivityForResult(i, Utils.REQUEST_CODE_FB_SHARE_DIALOG);


               // facebookManager.onFacebookShare("Sample App", "http://plurro.com/", "test description from social manager", (AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;

            case R.id.btn_fb_share_img:

                Uri uri = Uri.parse("/storage/emulated/0/Jackpotz/1449678435_074391-201512091627162918.png");

                int id = R.drawable.com_facebook_profile_picture_blank_square;
                Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), id);

                //  facebookManager.shareImageOnFacebook(imgUrl,(AppCompatActivity) getActivity(), R.id.frame_content, this);
                facebookManager.shareImageOnFacebook(id, (AppCompatActivity) getActivity(), R.id.frame_content, this);

                //  facebookManager.shareImageOnFacebook(uri,(AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;
            case R.id.btn_fb_share_link:

                facebookManager.shareLinkOnFacebook(imgUrl, (AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;
            case R.id.btn_fb_share_txt:

                facebookManager.shareTextOnFacebook("Hello sacascab", (AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;
            case R.id.btn_fb_share_vdo:
                /**
                 * Uri will not work with hardcoded value
                 */


                File dir = Environment.getExternalStorageDirectory();
                File dcim = new File(dir.getAbsolutePath() + "/Download/a.mp4");
                Uri videoUri = Uri.fromFile(dcim);

                facebookManager.shareVDOOnFacebook(videoUri, (AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;

            case R.id.btn_gp_login:
                googlePlusManager.onGooglePlusLogin((AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;
            case R.id.btn_gp_share:
                googlePlusManager.onGooglePlusShare(getActivity(), "Hello From google+", "https://static.pexels.com/photos/248797/pexels-photo-248797.jpeg");
                break;
            case R.id.btn_twitter_login:
                twitterManager.onTwitterLogin((AppCompatActivity) getActivity(), R.id.frame_content, this);
                break;
            case R.id.btn_twitter_tweet:
                twitterManager.onTwitterShare(getActivity(), "Hello From Twitter", "https://static.pexels.com/photos/248797/pexels-photo-248797.jpeg");
                break;
            case R.id.btn_fb_like:

                /* make the API call */
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/40796308305/likes",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                                System.out.println("DATA=" + response.toString());
            /* handle the result */
                            }
                        }
                ).executeAsync();
                break;
            case R.id.btn_twitter_load_tweet:

                twitterManager.loadTweets();
                break;


        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onFBLoginSuccess(AccessToken accessToken, Social social) {
        Toast.makeText(getActivity(), "" + social.getFirstName(), Toast.LENGTH_LONG).show();


           /* make the API call */
        new GraphRequest(
                accessToken,
                "/" + accessToken.getUserId() + "/posts",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        System.out.println("DATA=" + response.toString());
            /* handle the result */
                    }
                }
        ).executeAsync();


    }

    @Override
    public void onFBLoginFailed() {
        Toast.makeText(getActivity(), "Facebook login failed ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGPLoginSuccess(Social social) {
        Toast.makeText(getActivity(), "" + social.getFirstName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGPLoginFailed() {
        Toast.makeText(getActivity(), "Google+ login failed ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTwitterLoginSuccess(Social social) {
        Toast.makeText(getActivity(), "" + social.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTwitterLoginFailed() {
        Toast.makeText(getActivity(), "Twitter login failed ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFBShareSuccess() {
        Toast.makeText(getActivity(), "Facebook share success! ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFBShareFailed() {
        Toast.makeText(getActivity(), "Facebook share failed! ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}