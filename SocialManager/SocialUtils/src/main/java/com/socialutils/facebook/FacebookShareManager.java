package com.socialutils.facebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.socialutils.BaseFragment;
import com.socialutils.R;
import com.socialutils.utils.SocialUtils;

import java.io.File;


public class FacebookShareManager extends BaseFragment{
	private CallbackManager callbackManager;

	private ShareLinkContent content;
	AppCompatActivity activity;
	String mShareTitle,mShareUrl,mShareContent;
	FBShareCallBack shareCallBack;




	public	interface FBShareCallBack{
		 void onFBShareSuccess();
		 void onFBShareFailed();
	}





	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_social_common, container, false);
	}
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		//FBVideoShare(null);
		//	onShareContentUri();
		/*	facebookDialogShare();*/
		//onInvite();
		//postPhoto();

	}










	/*public void likeFBPage(Context mContext) {
		LikeView likeView = new LikeView(mContext);

		likeView.setObjectIdAndType(
				"https://www.facebook.com/TimesofIndia/",
				LikeView.ObjectType.PAGE);
		likeView.performClick();

	}



	public void onInvite() {
		String appLinkUrl, previewImageUrl;

		appLinkUrl = "http://plurro.com";
		previewImageUrl = "http://plurro.com/public/upload/buiseness_other_image/277/5_1348701025_souvlaki1.jpg";

		if (AppInviteDialog.canShow()) {
			AppInviteContent content = new AppInviteContent.Builder()
			.setApplinkUrl(appLinkUrl)
			.setPreviewImageUrl(previewImageUrl)
			.build();
			AppInviteDialog.show(this, content);
		}
	}*/


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}



	public void onExit() {

		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			public void run() {
				activity.getSupportFragmentManager().popBackStack();
			}
		});



	}


	/**
	 * Image Sharing
	 */

	/**
	 *
	 * @param activity
	 * @param mShareTitle
	 * @param mShareUrl
	 * @param mShareContent
	 * @param shareCallBack
	 */
	public void setDialogShareListener(AppCompatActivity activity, String mShareTitle, String mShareUrl, String mShareContent, FBShareCallBack shareCallBack)
	{
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.mShareTitle=mShareTitle;
		this.mShareUrl=mShareUrl;
		this.mShareContent=mShareContent;
		this.shareCallBack=shareCallBack;
		facebookDialogShare(mShareTitle, mShareUrl, mShareContent);
	}

	/**
	 *
	 * @param activity
	 * @param mShareUri
	 * @param shareCallBack
	 */
	public void setDialogShareListener(AppCompatActivity activity, Uri mShareUri, FBShareCallBack shareCallBack)
	{
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.shareCallBack=shareCallBack;

		/**getting bitmap from uri path**/
		File f = new File(mShareUri.getPath());

		Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());

		if(bmp==null){
			Toast.makeText(activity,"Image Uri Not Available!",Toast.LENGTH_SHORT).show();
			return;
		}


		facebookDialogShare(bmp);
	}
	public void setDialogShareListener(AppCompatActivity activity, Bitmap mBitmap, FBShareCallBack shareCallBack)
	{
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.shareCallBack=shareCallBack;
		facebookDialogShare(mBitmap);
	}


	public void setTextShareListener(AppCompatActivity activity, String shareText, FBShareCallBack shareCallBack)
	{
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.shareCallBack=shareCallBack;
		facebookDialogShare(shareText);
	}


	private void facebookDialogShare(String title,String contentUrl,String description) {
		// TODO Auto-generated method stub



		callbackManager = CallbackManager.Factory.create();

		ShareDialog shareDialog = new ShareDialog(activity);
		shareDialog.registerCallback(callbackManager,
				new FacebookCallback<Result>() {

					@Override
					public void onSuccess(Result result) {

						shareCallBack.onFBShareSuccess();
						onExit();
					}

					@Override
					public void onCancel() {
						shareCallBack.onFBShareFailed();

						onExit();
					}

					@Override
					public void onError(FacebookException error) {
						shareCallBack.onFBShareFailed();

						onExit();
					}
				}, SocialUtils.REQUEST_CODE_FACEBOOK_SHARE);

		if (ShareDialog.canShow(ShareLinkContent.class)) {

			content = new ShareLinkContent.Builder()
					.setContentUrl(
							Uri.parse(contentUrl))
					.setContentTitle(title)
					.setContentDescription(description).build();


			shareDialog.show(content);
		}
	}


	public void facebookDialogShare(Bitmap mBitmap) {




		callbackManager = CallbackManager.Factory.create();

		ShareDialog shareDialog = new ShareDialog(activity);
		shareDialog.registerCallback(callbackManager,
				new FacebookCallback<Sharer.Result>() {

					@Override
					public void onSuccess(Result result) {

						shareCallBack.onFBShareSuccess();

					}

					@Override
					public void onCancel() {
						shareCallBack.onFBShareFailed();


					}

					@Override
					public void onError(FacebookException error) {
						shareCallBack.onFBShareFailed();


					}
				});

		if (ShareDialog.canShow(ShareLinkContent.class)) {



			SharePhoto photo = new SharePhoto.Builder()
					.setBitmap(mBitmap)
					.build();
			SharePhotoContent content = new SharePhotoContent.Builder()
					.addPhoto(photo)
					.build();
			shareDialog.show(content);



		}




	}



	public void facebookDialogShare(String  text) {

		callbackManager = CallbackManager.Factory.create();

		ShareDialog shareDialog = new ShareDialog(activity);
		shareDialog.registerCallback(callbackManager,
				new FacebookCallback<Result>() {

					@Override
					public void onSuccess(Result result) {

						shareCallBack.onFBShareSuccess();
						onExit();
					}

					@Override
					public void onCancel() {
						shareCallBack.onFBShareFailed();

						onExit();
					}

					@Override
					public void onError(FacebookException error) {
						shareCallBack.onFBShareFailed();

						onExit();
					}
				}, SocialUtils.REQUEST_CODE_FACEBOOK_SHARE);

		if (ShareDialog.canShow(ShareLinkContent.class)) {

			content = new ShareLinkContent.Builder()
					.setContentTitle(text)
					.setContentDescription(text).build();


			shareDialog.show(content);
		}




	}


	/**
	 * Image Sharing
	 */

	/*************Text Share**************/

	/**************VDO Sharing***********************/
	public void setVDODialogShareListener(AppCompatActivity activity,Uri vdoUri, FBShareCallBack shareCallBack)
	{
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.shareCallBack=shareCallBack;
		facebookVDODialogShare(vdoUri);
	}

	public void facebookVDODialogShare(Uri videoUrl) {






		callbackManager = CallbackManager.Factory.create();

		ShareDialog shareDialog = new ShareDialog(activity);
		shareDialog.registerCallback(callbackManager,
				new FacebookCallback<Result>() {

					@Override
					public void onSuccess(Result result) {

						shareCallBack.onFBShareSuccess();

					}

					@Override
					public void onCancel() {
						shareCallBack.onFBShareFailed();


					}

					@Override
					public void onError(FacebookException error) {
						shareCallBack.onFBShareFailed();


					}
				});

		if (ShareDialog.canShow(ShareVideoContent.class))
		{
			ShareVideo shareVideo = new ShareVideo.Builder().setLocalUrl(videoUrl).build();
			ShareVideoContent shareVideoContent = new ShareVideoContent.Builder()
					.setVideo(shareVideo)
									.build();
			shareDialog.show(shareVideoContent);
		}

		//Uri selectedVideo = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Video" + "/fight.mp4");



		/*if (ShareDialog.canShow(ShareVideoContent.class)) {
			ShareVideo video= new ShareVideo.Builder()
					.setLocalUrl(videoUrl)
					.build();
			ShareVideoContent videoContent = new ShareVideoContent.Builder()
					.setVideo(video)
					.build();

			ShareButton shareButton = new ShareButton(activity);
			shareButton.setShareContent(videoContent);
			shareButton.performClick();

		} else{
			Log.d("Activity", "you cannot share videos :(");
		}*/

	}

}
