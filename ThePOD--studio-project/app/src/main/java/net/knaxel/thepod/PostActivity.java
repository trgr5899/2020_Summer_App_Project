package net.knaxel.thepod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

public class PostActivity extends AppCompatActivity {

    String mediaSrc;

    Button mButtonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);

        mButtonBack = findViewById(R.id.buttonBack);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras!= null) {
            mediaSrc = extras.getString("media");

        }
        TabLayout tabLayout = findViewById(R.id.postingTabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab.getText().toString());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(2).select();

    }
    private void selectTab(String tabText) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (tabText) {
            case "media":
                FragmentPostingMedia fragment = new FragmentPostingMedia(mediaSrc);
                fragmentTransaction.replace(R.id.view, fragment,null);
                fragmentTransaction.commit();

                break;
            case "status":
                fragmentTransaction.replace(R.id.view, new FragmentPostingStatus(),null);
                fragmentTransaction.commit();

                break;
            case "thread":
                fragmentTransaction.replace(R.id.view, new FragmentPostingThread(),null);
                fragmentTransaction.commit();

                break;

        }

    }

}