package br.com.labo.cameracustom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;

    private CameraPreview mImageSurfaceView;
    private Camera camera;

    private CardView cardView;
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
        setContentView(R.layout.activity_camera);
        cameraPreviewLayout = findViewById(R.id.cPreview);

        camera = checkDeviceCamera();
        mImageSurfaceView = new CameraPreview(CameraActivity.this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);

        ultimaImg = findViewById(R.id.ultimaImg);
        numImagens = findViewById(R.id.numImg);
        cardView = findViewById(R.id.cardView);

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
                startActivityForResult(Intent.createChooser(intent, "Escolha a imagem"),SELECT_PICTURE);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this,PictureActivity.class));
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        if (resetaview) {
            numImagens.setVisibility(View.GONE);
            ultimaImg.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        } else if (bitmaps.size() > 0) {
            numImagens.bringToFront();
            numImagens.setVisibility(View.VISIBLE);
            numImagens.setText(String.valueOf(bitmaps.size()));

            cardView.setVisibility(View.VISIBLE);
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
                Toast.makeText(CameraActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }

            bitmapImg = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmapImg = RotateBitmap(bitmapImg,90);
            bitmaps.add(bitmapImg);
            startActivity(new Intent(CameraActivity.this,PictureActivity.class));
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
                        Intent intent = new Intent(CameraActivity.this,PictureActivity.class);
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("arrayBitmap",bitmaps);
//        setResult(1, returnIntent);
//        finish();
//    }
}



