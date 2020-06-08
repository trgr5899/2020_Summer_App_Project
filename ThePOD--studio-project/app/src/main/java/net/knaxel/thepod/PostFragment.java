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
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;

public class PostFragment extends Fragment implements SurfaceHolder.Callback{

    final int CAMERA_REQUEST_CODE = 0;
    private boolean safeToTakePicture = false;
     final Camera camera = Camera.open(); ;
     Camera.PictureCallback jpegCallBack = new Camera.PictureCallback(){
         @Override
         public void onPictureTaken(byte[] bytes, Camera camera){
             safeToTakePicture = true;
             Intent intent = new Intent(getActivity(), ShowCaptureActivity.class);

             File picture_file = null;
              String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


             Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
             Matrix matrix = new Matrix();

             matrix.postRotate(90);
             bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix, true);
             Context applicationContext = MainActivity.getContextOfApplication();
             String s = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), bitmap, "temp0" , "");
             intent.putExtra("capture",s);

             startActivity(intent);
         }
     };

    MediaRecorder recorder;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    public static PostFragment newInstance(){
        PostFragment fragment = new PostFragment();
        return fragment;
    }
    private void startRecording(){
        prepareRecorder();


        recorder.start();
    }
    private void stopRecording(){

        recorder.stop();
        initRecorder();
    }

    private void initRecorder() {
        recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        CamcorderProfile profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,CamcorderProfile.QUALITY_LOW);

        recorder.setProfile(profile);
        recorder.setOutputFile(this.getActivity().getFilesDir() + "/temp.mp4");
        recorder.setMaxDuration(50000); // 50 seconds
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }
    private void prepareRecorder() {
//        recorder.setPreviewDisplay(mSurfaceHolder.getSurface());

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
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Button mCapture = view.findViewById(R.id.capture);
        LinearLayout mLogout = view.findViewById(R.id.profileButton);
        mLogout.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_up, R.anim.still);
                startActivity(intent, options.toBundle());
            }
        });/*
        mCapture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //startRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //stopRecording();
                        break;
                }
                return false;
            }
        });*/

        mCapture.setOnClickListener(new View.OnClickListener(){
            boolean t = false;
            @Override
            public void onClick(View v) {
                if(t){
                    stopRecording();
                    t=false;
                }else{
                    startRecording();
                    t=true;
                }
            }
        });

        mSurfaceView = view.findViewById(R.id.surfaceView);

        mSurfaceView.setOnTouchListener(new OnSwipeTouchListener(container.getContext()){
            @Override
            public void onSwipeTop() {

                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_up, R.anim.still);
                startActivity(intent, options.toBundle());
            }

        });
        mSurfaceHolder = mSurfaceView.getHolder();

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }else {
            mSurfaceHolder.addCallback(this);
        }
        mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        initRecorder();
        return view;
    }
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return;

    }
    public void capture(){

            camera.takePicture(null, null , jpegCallBack);
            safeToTakePicture = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Camera.Parameters parameters;
        parameters = camera.getParameters();

        camera.setDisplayOrientation(90);

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        parameters.setPictureSize(4032,2268);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        camera.setParameters(parameters);

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
        switch(requestCode){
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                }else{
                    Toast.makeText(getContext(), "POD needs your permission to use your camera", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
