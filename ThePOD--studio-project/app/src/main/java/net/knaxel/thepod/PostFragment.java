package net.knaxel.thepod;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Policy;
import java.util.Arrays;


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

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    public static PostFragment newInstance(){
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Button mCapture = view.findViewById(R.id.capture);
        Button mLogout = view.findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                logout();
            }
        });
        mCapture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                capture();
            }
        });

        mSurfaceView = view.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }else {
            mSurfaceHolder.addCallback(this);
        }
        mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


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

/*
public class PostFragment extends AppCompatActivity
{
    Button button;
    Button logout;
    TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }
    private String cameraId;
    CameraDevice cameraDevice;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimensions;
    private ImageReader imageReader;
    private  File file;
    Handler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post);

        textureView= (TextureView)findViewById(R.id.texture);
        button =(Button)findViewById(R.id.capture) ;
        logout=(Button)findViewById(R.id.logout);
        textureView.setSurfaceTextureListener(textureListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==101)
        {
            if(grantResults[0]==PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(getApplicationContext(),"sorry.camera permissions is necessary",Toast.LENGTH_LONG).show();
                // finish();
            }
        }


    }

    TextureView.SurfaceTextureListener textureListener= new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };


    private final  CameraDevice.StateCallback stateCallback= new CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice=camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();

        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice=null;

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture =textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(),imageDimensions.getHeight());
        Surface surface=new Surface(texture);

        captureRequestBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);

        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if(cameraDevice==null)
                {
                    return;
                }
                cameraCaptureSession= session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onConfigureFailed( CameraCaptureSession session)
            {
                Toast.makeText(getApplicationContext(),"Configuration Changed",Toast.LENGTH_LONG).show();

            }
        },null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updatePreview() throws CameraAccessException {
        if(cameraDevice==null)
        {
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() throws CameraAccessException {
        CameraManager manager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        cameraId=manager.getCameraIdList()[0];
        CameraCharacteristics characteristics=manager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map= characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        assert map != null;
        imageDimensions=map.getOutputSizes(SurfaceTexture.class)[0];

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PostFragment.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
            return;
        }
        manager.openCamera(cameraId,stateCallback,null);


    }

    private void takePicture()
    {


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
    startBackgroundThread();
        if(textureView.isAvailable())
        {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else
        {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBackgroundThread()
    {
        mBackgroundThread=new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler=new Handler(mBackgroundThread.getLooper());

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread=null;
        mBackgroundHandler=null;

    }
}
*/
