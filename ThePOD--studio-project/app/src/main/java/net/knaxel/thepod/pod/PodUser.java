package net.knaxel.thepod.pod;

import android.accounts.Account;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import net.knaxel.thepod.FeedFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

enum AccountType {
    ARTIST,INFLUENCER,ENTERTAINER,PERSONAL,CORPORATE,FANPAGE;
}

public class PodUser {

    private String uuid, displayname,username,email,profilePicURL,bio,phone;
    private AccountType accountType;
    private List<String> following, followers;

    public PodUser(String uuid, String displayname, String username, String email, String profilePicURL, String bio, String phone, AccountType accountType, List<String> following, List<String> followers) {
        this.uuid = uuid;
        this.displayname = displayname;
        this.username = username;
        this.email = email;
        this.profilePicURL = profilePicURL;
        this.bio = bio;
        this.phone = phone;
        this.accountType = accountType;
        this.following = following;
        this.followers = followers;
    }
    public PodUser(String uuid, Map<String, Object> userData){
        this.uuid = uuid;
        this.displayname = (String) userData.get("display_name");
        this.username = (String) userData.get("user_name");
        this.email = (String) userData.get("email");
        this.profilePicURL = (String) userData.get("profile_image_url");
        if(userData.containsKey("biography")) this.bio = (String) userData.get("biography");
        if(userData.containsKey("phone")) this.phone = (String) userData.get("phone");
        if(userData.containsKey("account_type")) this.accountType = AccountType.valueOf((String) userData.get("account_type"));
        if(userData.containsKey("following")) this.following = (List<String>) userData.get("following"); else this.following = new ArrayList<>();
        if(userData.containsKey("followers")) this.followers = (List<String>) userData.get("followers"); else this.followers = new ArrayList<>();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public List<String> getFollowArray() {
        return following;
    }

    public void setFollowArray(List<String> following) {
        this.following = following;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}
