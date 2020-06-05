package net.knaxel.thepod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;
    private static PostFragment postfragment = PostFragment.newInstance();
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextOfApplication = getApplicationContext();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ViewPager viewPager = findViewById(R.id.view_pager);

        final FragmentPagerAdapter adapterViewPager= new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        viewPager.setCurrentItem(1);


        Button mCapture = findViewById(R.id.capture);
        Button mMessages = findViewById(R.id.messages);
        Button mFeed = findViewById(R.id.feed);
        mCapture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
    if(viewPager.getCurrentItem()==1){

        postfragment.capture();
    }else{
        viewPager.setCurrentItem(1);
    }
            }
        });
        mMessages.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(0);

            }
        });
        mFeed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(2);
            }
        });


    }
    public static class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0 :
                {
                    return MessagingFragment.newInstance();
                }
                case 1 :
                {
                    return postfragment;
                }
                case 2 :
                {
                    return FeedFragment.newInstance();
                }

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}