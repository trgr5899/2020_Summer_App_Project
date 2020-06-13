package net.knaxel.thepod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import net.knaxel.thepod.pod.POD;
import net.knaxel.thepod.pod.PodUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private PodUser user;
    TextView mUsernamePreview, mDisplayname, mUsername, mAccountType, mWebsite,mBiography,followingCount,followerCount,postCount;
    CircleImageView mProfilePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        Button logout =  findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mProfilePicture = findViewById(R.id.user_profilePic);
        mUsernamePreview = findViewById(R.id.user_username_preview);
        mDisplayname = findViewById(R.id.user_displayname);
        mUsername = findViewById(R.id.user_username);
        mAccountType = findViewById(R.id.user_accounttype);
        mWebsite = findViewById(R.id.user_website);
        mBiography = findViewById(R.id.user_biography);
        followingCount = findViewById(R.id.user_followingcount);
        followerCount = findViewById(R.id.user_followercount);
        postCount = findViewById(R.id.user_postcount);

        if(user == null){
            mDisplayname.setText(POD.USER.getDisplayname());
            mUsername.setText("@"+POD.USER.getUsername());
            mUsernamePreview.setText("@"+POD.USER.getUsername() + "'s profile");
            mAccountType.setText(POD.USER.getAccountType().toString());
            mWebsite.setText(POD.USER.getWebsite());
            mBiography.setText(POD.USER.getBio());
            followingCount.setText(POD.USER.getFollowArray().size()+"");
            followerCount.setText(POD.USER.getFollowers().size()+"");
            //postCount.setText(MainActivity.CURRENT_USER.getDisplayname());

            URL url = null;
            try {
                url = new URL(POD.USER.getProfilePicURL());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                mProfilePicture.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            loadUser();
        }

    }
    public void loadUser(){

        mDisplayname.setText(user.getDisplayname());
        mUsername.setText("@"+user.getUsername());
        mUsernamePreview.setText("@"+user.getUsername() + "'s profile");
        mAccountType.setText(user.getAccountType().toString());
        mWebsite.setText(user.getWebsite());
        mBiography.setText(user.getBio());
        followingCount.setText(user.getFollowArray().size()+"");
        followerCount.setText(user.getFollowers().size()+"");
        //postCount.setText(MainActivity.CURRENT_USER.getDisplayname());

        URL url = null;
        try {
            url = new URL(POD.USER.getProfilePicURL());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            mProfilePicture.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUser(PodUser user){
        this.user = user;
        loadUser();
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this.getApplicationContext(), SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return;

    }
}