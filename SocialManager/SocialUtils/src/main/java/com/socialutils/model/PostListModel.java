package com.socialutils.model;

/**
 * Created by hb on 10/12/15.
 */
public class PostListModel {

    String message;
    String story;
    String id;
    String posttime;


    public String getPosttime() {
        return posttime;
    }


    public void setPosttime(String posttime) {
        this.posttime = posttime;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public String getStory() {
        return story;
    }

    public String getId() {
        return id;
    }
}
