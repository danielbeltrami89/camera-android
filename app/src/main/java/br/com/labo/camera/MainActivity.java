package br.com.labo.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;

    private CameraPreview mImageSurfaceView;
    private Camera camera;

    private ImageView ultimaImg;
    private TextView numImagens;
    public static Boolean resetaview = false;

    public static Bitmap bitmapImg;
    public static ArrayList<Bitmap> bitmaps = new ArrayList<>();

    private LinearLayout cameraPreviewLayout;

    Camera mCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Fixa a rotação da tela para vertical
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraPreviewLayout = findViewById(R.id.cPreview);

        camera = checkDeviceCamera();
        mImageSurfaceView = new CameraPreview(MainActivity.this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);

        ultimaImg = findViewById(R.id.ultimaImg);
        numImagens = findViewById(R.id.numImg);


        ImageButton captureButton = findViewById(R.id.btnCam);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

        ImageButton galleryButton = findViewById(R.id.btnGaleria);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        if (resetaview) {
            numImagens.setVisibility(View.GONE);
            ultimaImg.setVisibility(View.GONE);
        } else if (bitmaps.size() > 0) {
            numImagens.setVisibility(View.VISIBLE);
            numImagens.setText(String.valueOf(bitmaps.size()));

            ultimaImg.setVisibility(View.VISIBLE);
            ultimaImg.setImageBitmap(bitmapImg);
        }

    }


    private Camera checkDeviceCamera(){

        try {
            mCamera = Camera.open();
            // Configura a rotação para vertical
            mCamera.setDisplayOrientation(90);

            // Cria paramentros para abertura da camera, nesse bloco habilita o auto focus
            Camera.Parameters params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if(bitmap==null){
                Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }

            bitmapImg = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmapImg = RotateBitmap(bitmapImg,90);
            bitmaps.add(bitmapImg);
            Intent intent = new Intent(MainActivity.this,PictureActivity.class);
            startActivity(intent);
        }
    };

    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        Intent intent = new Intent(MainActivity.this,PictureActivity.class);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        Log.d("tag", "RotateBitmap: ");
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }



//    private Camera mCamera;
//    private CameraPreview mPreview;
//    private Camera.PictureCallback mPicture;
//    private ImageButton capture, switchCamera;
//    private Context myContext;
//    private LinearLayout cameraPreview;
//    private boolean cameraFront = false;
//    public static Bitmap bitmap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        Bundle extra = getIntent().getExtras();
//        if (extra != null && extra.containsKey("mensagem")) {
//            Toast.makeText(this, extra.getString("mensagem"), Toast.LENGTH_SHORT).show();
//        }
//
//        myContext = this;
//
//        mCamera =  Camera.open();
//        mCamera.setDisplayOrientation(90);
//        cameraPreview = (LinearLayout) findViewById(R.id.cPreview);
//        mPreview = new CameraPreview(myContext, mCamera);
//        cameraPreview.addView(mPreview);
//
//        capture = findViewById(R.id.btnCam);
//        capture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Main", "onClick: ");
//                mCamera.takePicture(null, null, mPicture);
//            }
//        });
//
//        switchCamera = findViewById(R.id.btnSwitch);
//        switchCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //get the number of cameras
//                int camerasNumber = Camera.getNumberOfCameras();
//                if (camerasNumber > 1) {
//                    //release the old camera instance
//                    //switch camera, from the front and the back and vice versa
//
//                    releaseCamera();
//                    chooseCamera();
//                } else {
//
//                }
//            }
//        });
//
//        mCamera.startPreview();
//
//    }
//    private int findFrontFacingCamera() {
//
//        int cameraId = -1;
//        // Search for the front facing camera
//        int numberOfCameras = Camera.getNumberOfCameras();
//        for (int i = 0; i < numberOfCameras; i++) {
//            Camera.CameraInfo info = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, info);
//            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                cameraId = i;
//                cameraFront = true;
//                break;
//            }
//        }
//        return cameraId;
//
//    }
//
//    private int findBackFacingCamera() {
//        int cameraId = -1;
//        //Search for the back facing camera
//        //get the number of cameras
//        int numberOfCameras = Camera.getNumberOfCameras();
//        //for every camera check
//        for (int i = 0; i < numberOfCameras; i++) {
//            Camera.CameraInfo info = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, info);
//            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                cameraId = i;
//                cameraFront = false;
//                break;
//
//            }
//
//        }
//        return cameraId;
//    }
//
//    public void onResume() {
//
//        super.onResume();
//        if(mCamera == null) {
//            mCamera = Camera.open();
//            mCamera.setDisplayOrientation(90);
//            mPicture = getPictureCallback();
//            mPreview.refreshCamera(mCamera);
//            Log.d("nu", "null");
//        }else {
//            Log.d("nu","no null");
//        }
//
//    }
//
//    public void chooseCamera() {
//        //if the camera preview is the front
//        if (cameraFront) {
//            int cameraId = findBackFacingCamera();
//            if (cameraId >= 0) {
//                //open the backFacingCamera
//                //set a picture callback
//                //refresh the preview
//
//                mCamera = Camera.open(cameraId);
//                mCamera.setDisplayOrientation(90);
//                mPicture = getPictureCallback();
//                mPreview.refreshCamera(mCamera);
//            }
//        } else {
//            int cameraId = findFrontFacingCamera();
//            if (cameraId >= 0) {
//                //open the backFacingCamera
//                //set a picture callback
//                //refresh the preview
//                mCamera = Camera.open(cameraId);
//                mCamera.setDisplayOrientation(90);
//                mPicture = getPictureCallback();
//                mPreview.refreshCamera(mCamera);
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //when on Pause, release camera in order to be used from other applications
//        releaseCamera();
//    }
//
//    private void releaseCamera() {
//        // stop and release camera
//        if (mCamera != null) {
//            mCamera.stopPreview();
//            mCamera.setPreviewCallback(null);
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//
//    private Camera.PictureCallback getPictureCallback() {
//        Camera.PictureCallback picture = new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] data, Camera camera) {
//                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                Intent intent = new Intent(MainActivity.this,PictureActivity.class);
//                startActivity(intent);
//            }
//        };
//        return picture;
//    }
}



