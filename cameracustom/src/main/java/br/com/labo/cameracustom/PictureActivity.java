package br.com.labo.cameracustom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class PictureActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView, imageViewG;
    private static final String IMAGE_DIRECTORY = "/LABO";
    private ImageButton btnFechar, btnMais, btnok, btnApaga;
    LinearLayout layout;

    private int posicaoClick = -1;
    private boolean ultima = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        btnFechar = findViewById(R.id.btnFechar);
        btnMais = findViewById(R.id.btnMais);
        btnok = findViewById(R.id.btnOk);
        btnApaga = findViewById(R.id.btnApaga);

        layout = findViewById(R.id.linear);

        btnFechar.setOnClickListener(this);
        btnMais.setOnClickListener(this);
        btnok.setOnClickListener(this);

        imageView = findViewById(R.id.img);
        imageView.setImageBitmap(CameraActivity.bitmapImg);

        if (CameraActivity.bitmaps.size() > 1) {
            exibeImagensGaleria(posicaoClick, ultima);
        }

    }

    public void exibeImagensGaleria(final int posicao, final boolean ultima) {

            layout.setVisibility(View.VISIBLE);

            if (CameraActivity.bitmaps.size() == 1) {
                btnApaga.setVisibility(View.GONE);
            } else {
                btnApaga.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < CameraActivity.bitmaps.size(); i++) {
                imageViewG = new ImageView(this);
                imageViewG.setId(i);

                imageViewG.setPadding(5, 5, 5, 5);
                imageViewG.setImageBitmap(CameraActivity.bitmaps.get(i));
                imageViewG.setAdjustViewBounds(true);
                imageViewG.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageViewG.setCropToPadding(false);

                layout.addView(imageViewG);

                final int finalI = i;

                if (!ultima) {
                    imageViewG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imageView.setImageBitmap(CameraActivity.bitmaps.get(finalI));
                            posicaoClick = finalI;
                            layout.removeAllViews();
                            exibeImagensGaleria(posicaoClick, ultima);

                        }
                    });
                }

                btnApaga.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (posicao == -1)
                            removeImagem(CameraActivity.bitmaps.size()-1);
                        else
                            removeImagem(posicaoClick);
                    }
                });

                if (posicao == i){
                    imageViewG.setBackgroundResource(R.drawable.image_border);

                }
            }

            if (posicao == -1) {
                imageViewG.setBackgroundResource(R.drawable.image_border);
            }


    }

    public void removeImagem(int idImagem){
        CameraActivity.bitmaps.remove(idImagem);
        layout.removeViewAt(idImagem);

        if (CameraActivity.bitmaps.size() == 1) {
            imageView.setImageBitmap(CameraActivity.bitmaps.get(0));

            layout.removeAllViews();
            exibeImagensGaleria(-1, true);
            ultima = true;

        } else if (CameraActivity.bitmaps.size() > 1) {
            imageView.setImageBitmap(CameraActivity.bitmaps.get(0));

            layout.removeAllViews();
            exibeImagensGaleria(-1, ultima);
        }


    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "Arquivo salvo:--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    @Override
    public void onClick(View view) {

        int i1 = view.getId();
        if (i1 == R.id.btnFechar) {
            CameraActivity.bitmaps.clear();
            CameraActivity.bitmapImg = null;
            CameraActivity.resetaview = true;

            onExit();


        } else if (i1 == R.id.btnMais) {
            CameraActivity.resetaview = false;
            onBackPressed();

        } else if (i1 == R.id.btnOk) {
            for (int i = 0; i < CameraActivity.bitmaps.size(); i++) {
                saveImage(CameraActivity.bitmaps.get(i));
            }

            Toast.makeText(this, "Imagem salva!", Toast.LENGTH_SHORT).show();

            voltaTelas();

        }
    }

    public void onExit () {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        PictureActivity.this.finish();
        Log.d("Fechar tudo", "onExit: ");
    }

    public void voltaTelas() {

        String origem = "CameraActivity";

        Intent myIntent = null;
        myIntent = new Intent(this, CameraActivity.class);
        Log.d("OK", "voltaTelas: ");
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear the backstack
        startActivity(myIntent);
        finish();
        return;
    }
}