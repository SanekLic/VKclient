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

import com.my.vkclient.Entities.Rect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadImageToImageViewAsync extends AsyncTask<String, Void, Bitmap> {
    public static final int PERCENTAGE = 100;
    private boolean isCircular = false;
    private Rect crop;
    private ImageView imageView;

    public LoadImageToImageViewAsync(ImageView imageView) {
        this.imageView = imageView;
    }

    public LoadImageToImageViewAsync setCircular(boolean isCircular) {
        this.isCircular = isCircular;

        return this;
    }

    public LoadImageToImageViewAsync setCrop(Rect crop) {
        this.crop = crop;

        return this;
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
            resultBitmap = bitmapToCircle(resultBitmap);
        }

        if (crop != null) {
            resultBitmap = cropBitmap(resultBitmap, crop);
        }

        imageView.setImageBitmap(resultBitmap);

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

    private Bitmap cropBitmap(Bitmap input, Rect crop) {
        int width = input.getWidth();
        int height = input.getHeight();
        float x = width * crop.getX() / PERCENTAGE;
        float y = height * crop.getY() / PERCENTAGE;
        float x2 = width * crop.getX2() / PERCENTAGE;
        float y2 = height * crop.getY2() / PERCENTAGE;
        Bitmap output = Bitmap.createBitmap(input, (int) x, (int) y, (int) (x2 - x), (int) (y2 - y));

        return output;
    }
}
