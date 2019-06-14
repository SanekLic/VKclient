package com.my.vkclient.entities;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("online")
    private Boolean online;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("photo_max_orig")
    private String photoMaxUrl;

    @SerializedName("photo_100")
    private String photo100Url;

    @SerializedName("crop_photo")
    private CropPhoto cropPhoto;

    @SerializedName("about")
    private String about;

    @SerializedName("common_count")
    private int commonFriendsCount;

    @SerializedName("counters")
    private UserCounters counters;

    @SerializedName("university_name")
    private String universityName;

    @SerializedName("faculty_name")
    private String facultyName;

    @SerializedName("followers_count")
    private int followersCount;

    @SerializedName("games")
    private String games;

    @SerializedName("home_town")
    private String homeTown;

    @SerializedName("interests")
    private String interests;

    @SerializedName("last_seen")
    private UserLastSeen lastSeen;

    @SerializedName("movies")
    private String movies;

    @SerializedName("music")
    private String music;

    @SerializedName("status")
    private String status;

    @SerializedName("verified")
    private Boolean verified;

    public User() {
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getCommonFriendsCount() {
        return commonFriendsCount;
    }

    public void setCommonFriendsCount(int commonFriendsCount) {
        this.commonFriendsCount = commonFriendsCount;
    }

    public UserCounters getCounters() {
        return counters;
    }

    public void setCounters(UserCounters counters) {
        this.counters = counters;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public UserLastSeen getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(UserLastSeen lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getMovies() {
        return movies;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoMaxUrl() {
        return photoMaxUrl;
    }

    public void setPhotoMaxUrl(String photoMaxUrl) {
        this.photoMaxUrl = photoMaxUrl;
    }

    public String getPhoto100Url() {
        return photo100Url;
    }

    public void setPhoto100Url(String photo100Url) {
        this.photo100Url = photo100Url;
    }

    public CropPhoto getCropPhoto() {
        return cropPhoto;
    }

    public void setCropPhoto(CropPhoto cropPhoto) {
        this.cropPhoto = cropPhoto;
    }
}
