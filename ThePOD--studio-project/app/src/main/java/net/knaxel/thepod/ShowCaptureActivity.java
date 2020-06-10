package net.knaxel.thepod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.tabs.TabItem;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class ShowCaptureActivity extends AppCompatActivity implements UCropFragmentCallback {

    Button mButtonBack, mButtonSave, mButtonSend, mButtonPost;

    VideoView mVideoView;
    ImageView mImageView;

    public void sendToPost(String path){

        Uri imageUri = Uri.parse(path);
        Uri newUri = Uri.fromFile(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/new.bmp"));
        UCrop.Options options = new UCrop.Options();
        options.setAspectRatioOptions(0,
                new AspectRatio("square", 1, 1),
                new AspectRatio("wide", 16, 9),
                new AspectRatio("less-wide", 4, 3),
                new AspectRatio("wide-rotated", 9, 16));

        UCrop.of(imageUri, newUri).withOptions(options)
                .start(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_capture);

        Bundle extras = getIntent().getExtras();
        final String b = extras.getString("capture");
        final String s = extras.getString("video");
        mImageView = findViewById(R.id.captureImage);

        mButtonBack = findViewById(R.id.buttonBack);
        mButtonSend = findViewById(R.id.buttonSendCapture);
        mButtonPost = findViewById(R.id.buttonPostCapture);
        mButtonSave = findViewById(R.id.buttonSaveCapture);

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToPost(b);
            }
        });

        if (b != null) {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(b));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mImageView.setImageBitmap(bitmap);


        }

        if (s != null) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == UCrop.REQUEST_CROP) {


                    Intent intent = new Intent(ShowCaptureActivity.this, PostActivity.class);
                    intent.putExtra("media", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/new.bmp");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case UCrop.RESULT_ERROR:
                Log.println(Log.ERROR, ShowCaptureActivity.class.toString(), requestCode + "|" + RESULT_OK);
        }
    }

    @Override
    public void loadingProgress(boolean showLoader) {

    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {

    }
}