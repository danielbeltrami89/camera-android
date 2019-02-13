package br.com.labo.camera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import br.com.labo.cameracustom.CameraActivity;

public class MainActivity extends AppCompatActivity {

    private Button btncamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btncamera = findViewById(R.id.button);
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivityForResult(new Intent(MainActivity.this, CameraActivity.class ),1);
                //startActivity(new Intent(MainActivity.this, CameraActivity.class).setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY));

                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 1) {
//            if(resultCode == 1){
//                ArrayList<Bitmap> bitmapsResult = data.getParcelableExtra("arrayBitmaps");
//
//                for (int i=0; bitmapsResult.size()>i; i++ ){
//                    Log.d("Bitmaps", "onActivityResult: " + String.valueOf(bitmapsResult.get(i)));
//                }
//            }
//
//        }
//    }
}
