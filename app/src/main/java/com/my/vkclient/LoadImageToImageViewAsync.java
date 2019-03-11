package com.my.vkclient;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class LoadImageToImageViewAsync extends AsyncTask<String, Void, Bitmap> {
    private boolean isCircular;
    private ImageView imageView;

    LoadImageToImageViewAsync(ImageView imageView, boolean isCircular) {
        this.imageView = imageView;
        this.isCircular = isCircular;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            String imageFileName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".") + 4);
            String imageFilePath = imageView.getContext().getCacheDir().toString();
            File imageFile = new File(imageFilePath, imageFileName);
            if (imageFile.exists()) {
                if (!this.isCancelled()) {
                    bitmap = BitmapFactory.decodeFile(imageFile.getPath());
                }
            } else {
                InputStream urlInputStream = new URL(url).openStream();
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                ByteArrayOutputStream byteArrayFromUrl = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;

                while ((length = urlInputStream.read(buffer)) != -1) {
                    byteArrayFromUrl.write(buffer, 0, length);
                }

                fileOutputStream.write(byteArrayFromUrl.toByteArray());
                fileOutputStream.flush();
                fileOutputStream.close();

                if (!this.isCancelled()) {
                    bitmap = BitmapFactory.decodeByteArray(byteArrayFromUrl.toByteArray(), 0, byteArrayFromUrl.size());
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (isCircular) {
            RoundedBitmapDrawable circularDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), result);
            circularDrawable.setCircular(true);
            imageView.setImageDrawable(circularDrawable);
        } else {
            imageView.setImageBitmap(result);
        }

        Animator animator = AnimatorInflater.loadAnimator(imageView.getContext(), R.animator.image_change_visibility_animator);
        animator.setTarget(imageView);
        animator.start();
    }
}
