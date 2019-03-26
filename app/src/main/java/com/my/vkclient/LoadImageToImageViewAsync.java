package com.my.vkclient;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap resultBitmap) {

        if (isCircular) {
            imageView.setImageBitmap(bitmapToCircle(resultBitmap));
        } else {
            imageView.setImageBitmap(resultBitmap);
        }

        Animator animator = AnimatorInflater.loadAnimator(imageView.getContext(), R.animator.image_change_visibility_animator);
        animator.setTarget(imageView);
        animator.start();
    }

    private Bitmap bitmapToCircle(Bitmap input) {
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        BitmapShader shader = new BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        final float radius = Math.min(input.getWidth(), input.getHeight()) / 2F;
        canvas.drawCircle(input.getWidth() / 2F, input.getHeight() / 2F, radius, paint);

        return output;
    }
}
