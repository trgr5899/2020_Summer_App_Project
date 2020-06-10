package net.knaxel.thepod;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;

public class PostFragment extends Fragment implements SurfaceHolder.Callback {

    final int CAMERA_REQUEST_CODE = 0;
    private boolean safeToTakePicture = false;
    final Camera camera = Camera.open();
    Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            safeToTakePicture = true;
            Intent intent = new Intent(getActivity(), ShowCaptureActivity.class);

            File picture_file = null;
            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Matrix matrix = new Matrix();

            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            Context applicationContext = MainActivity.getContextOfApplication();
            String s = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), bitmap, "pod-"+ Calendar.getInstance().getTime().toString(), "");

            intent.putExtra("capture", s);

            startActivity(intent);
        }
    };

    VideoView mVideoView ;
    MediaRecorder recorder;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    public void startRecording() {
        if(capturePressedLong){
            return;
        }
        camera.unlock();
        prepareRecorder();

        recorder.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopRecording() {
        recorder.stop();
        camera.lock();

        Intent intent = new Intent(getActivity(), ShowCaptureActivity.class);
        intent.putExtra("video", getActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/temp1.mp4");
        startActivity(intent);


        //initRecorder();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initRecorder() {
        recorder = new MediaRecorder();
        recorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        recorder.setCamera(camera);
        recorder.setOrientationHint(90);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        //recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/temp1.mp4");
         recorder.setOutputFile(file.getAbsolutePath());
        recorder.setMaxDuration(50000); // 50 seconds
        //recorder.setVideoEncodingBitRate(6000000);
        recorder.setVideoFrameRate(30);
        recorder.setMaxFileSize(500000000); // Approximately 5 megabyte
       camera.lock();

    }

    private void prepareRecorder() {
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        }
    }

    @Override
    public void onDestroy() {

        //camera.stopPreview();
        //camera.release();
        super.onDestroy();

    }

    private boolean capturePressedLong = false;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Button mCapture = view.findViewById(R.id.buttonCapture);
        Button mButtonPost = view.findViewById(R.id.buttonPost);
        LinearLayout mLogout = view.findViewById(R.id.profileButton);

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_up, R.anim.still);
                startActivity(intent, options.toBundle());
            }
        });
        mCapture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startRecording();
                capturePressedLong = true;
                return true;
            }
        });
        mCapture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                // We're only interested in when the button is released.
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (capturePressedLong) {
                        // Do something when the button is released.
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                stopRecording();
                            }
                        }, 350);
                        capturePressedLong = false;
                    }else{
                        camera.lock();
                        takePicture();
                    }
                }
                return false;
            }
        });

        mSurfaceView = view.findViewById(R.id.surfaceView);
       // mSurfaceView.setMinimumWidth(995438594);//mSurfaceView.getHeight()*(9/16));
        mSurfaceHolder = mSurfaceView.getHolder();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            mSurfaceHolder.addCallback(this);
        }
        mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        return view;
    }
    public void takePicture(){
        camera.takePicture(null, null, jpegCallBack);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Camera.Parameters parameters  = camera.getParameters();
        camera.setDisplayOrientation(90);

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        parameters.setPictureSize(4032,2268);
        parameters.setPreviewSize(1280,720);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(9*(getView().getHeight())/16, getView().getHeight());
        lp.setMargins(-(9*(getView().getHeight())/16 - getView().getWidth())/2,0,0,0);
        mSurfaceView.setLayoutParams( lp);
        //mSurfaceHolder.setFixedSize(getView().getWidth(),(16*getView().getWidth()/9));
        //mSurfaceHolder.setFixedSize(8*getView().getHeight()/16,getView().getHeight());
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);

        initRecorder();

        try {
            camera.setPreviewDisplay(mSurfaceHolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

        safeToTakePicture = true;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                } else {
                    Toast.makeText(getContext(), "POD needs your permission to use your camera", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
