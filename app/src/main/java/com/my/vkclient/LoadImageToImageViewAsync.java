package com.my.vkclient;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class LoadImageToImageViewAsync extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    LoadImageToImageViewAsync(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".") + 4);
            String filePath = imageView.getContext().getCacheDir().toString();
            File file = new File(filePath, fileName);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            } else {
                InputStream inputStream = new java.net.URL(url).openStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                bitmap = BitmapFactory.decodeByteArray(result.toByteArray(), 0, result.size());
                fileOutputStream.write(result.toByteArray());
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
        Animator animator = AnimatorInflater.loadAnimator(imageView.getContext(), R.animator.image_change_visibility_animator);
        animator.setTarget(imageView);
        animator.start();
    }
}
