package com.simran.a4kwallpaper;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;

public class WallpaperActivity extends AppCompatActivity {
    WallpaperManager wallpaperManager;
    ImageView image;
    String url;
    private ProgressBar loadingPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wallpaper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        url = getIntent().getStringExtra("imgUrl");
        image = findViewById(R.id.image);
        loadingPB = findViewById(R.id.idPBLoading);

        // calling glide to load image from url on below line.
        Glide.with(this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                // making progress bar visibility
                // to gone on below line.
                loadingPB.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                // making progress bar visibility to gone
                // on below line when image is ready.
                loadingPB.setVisibility(View.GONE);
                return false;
            }
        }).into(image);

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Button setWallpaper = findViewById(R.id.idBtnSetWallpaper);

        // on below line we are adding on click
        // listener to our set wallpaper button.
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(WallpaperActivity.this)
                        .asBitmap().load(url)
                        .listener(new RequestListener<Bitmap>() {
                                      @Override
                                      public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                          Toast.makeText(WallpaperActivity.this, "Fail to load image..", Toast.LENGTH_SHORT).show();
                                          return false;
                                      }

                                      @Override
                                      public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                          // on below line we are setting wallpaper using
                                          // wallpaper manager on below line.
                                          try {
                                              wallpaperManager.setBitmap(bitmap);
                                              Toast.makeText(WallpaperActivity.this, "Wallpaper Set to Home screen.", Toast.LENGTH_SHORT).show();
                                          } catch (IOException e) {
                                              // on below line we are handling exception.
                                              Toast.makeText(WallpaperActivity.this, "Fail to set wallpaper", Toast.LENGTH_SHORT).show();
                                              e.printStackTrace();
                                          }
                                          return false;
                                      }
                                  }
                        ).submit();
                // displaying custom toast on below line.
                FancyToast.makeText(WallpaperActivity.this, "Wallpaper Set to Home Screen", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            }
        });


    }
}