package com.socialutils.facebook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socialutils.BaseFragment;
import com.socialutils.model.PostListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyesh Bhargava on 12/12/15.
 */
public class FacebookPostManager extends BaseFragment {


    private AccessToken accessToken;
    private String userid;
    boolean isScrollable = true;
    String nextPage = null;
    List<PostListModel> mPostlist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPostlist = new ArrayList<>();
        
    }

    public void callGetPost() {

        new GraphRequest(
                accessToken,
                "/" + userid + "/posts",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.i(" posts  :", response.toString());

                        JSONObject object = response.getJSONObject();
                        parseJsonData(object);
/*
                        layoutManager = new LinearLayoutManager(activity);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));

                        adapter = new PostAdapter(activity, mPostlist);
                        recyclerView.setAdapter(adapter);*/
                    }
                }
        ).executeAsync();


    }
    private void parseJsonData(JSONObject object) {
        JSONArray postList = null;

        if (object != null) {
            postList = object.optJSONArray("data");
        }

        if (postList.length() != 0 && postList != null) {

            for (int i = 0; i < postList.length(); i++) {

                JSONObject postData = postList.optJSONObject(i);

                String messege = null;
                String story = null;
                if (postData.optString("story") != null) {
                    story = postData.optString("story");
                }
                if (postData.optString("message") != null) {
                    messege = postData.optString("message");
                }


                String id = postData.optString("id");
                String created_time = postData.optString("created_time");

                PostListModel posts = new PostListModel();
                posts.setMessage(messege);
                posts.setStory(story);
                posts.setId(id);
                posts.setPosttime(created_time);

                mPostlist.add(posts);

            }
            JSONObject paging = object.optJSONObject("paging");

            String previousPage = paging.optString("previous");
            nextPage = paging.optString("next");

        } else {
            ShowSnackbar("No data to show");
            isScrollable = false;
        }
    }

    public void ShowSnackbar(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
        //Snackbar.make(rootView, s, Snackbar.LENGTH_SHORT).show();
    }


}
