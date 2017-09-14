package com.socialutils.model;

import java.io.Serializable;

/**
 * @author Priyesh Bhargava
 */
public class Social implements Serializable{

    String name;
    String accessToken;
    String profileImageUrl;
    String socialId;
    String emailId;
    String firstName;
    String lastName;

    /**
     *
     * @return userEmailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     *
     * @param emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     *
     * @return userFirstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return userLastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**

     *
     * @return userName
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        accessToken = accessToken;
    }

    /**
     *
     * @return profileImageUrl
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     *
     * @param profileImageUrl
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     *
     * @return socialId
     */
    public String getSocialId() {
        return socialId;
    }

    /**
     *
     * @param socialId
     */
    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }
}

