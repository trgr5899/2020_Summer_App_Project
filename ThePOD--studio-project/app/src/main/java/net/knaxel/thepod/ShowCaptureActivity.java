package net.knaxel.thepod;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class ShowCaptureActivity extends AppCompatActivity {
    VideoView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_capture);

        ImageView imageView = findViewById(R.id.captureImage);
        Bundle extras = getIntent().getExtras();
        String b = extras.getString("capture");
        String s = extras.getString("video");




        if(b!=null){
            Bitmap bitmap = null;
            try {
                 bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(b) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(bitmap);


        }

    if(s!=null) {
        mVideoView = (VideoView) findViewById(R.id.captureVideo);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        File file = new File(s);
        mVideoView.setVideoURI(Uri.fromFile(file));
        //mVideoView.setContentDescription("Fds");
        if (file.exists()) {
            Log.println(Log.ERROR, MainActivity.class.toString(), Uri.fromFile(file).toString());
        }
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mVideoView.requestFocus();
        mVideoView.start();

    }
    }
}