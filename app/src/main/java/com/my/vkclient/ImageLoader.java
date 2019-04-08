package com.my.vkclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.my.vkclient.entities.Rect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoader {
    public static final int PERCENTAGE = 100;
    private boolean isCancelled = false;
    private boolean isCircular = false;
    private String imageCacheFilePath;
    private Rect crop;

    public ImageLoader(Context context) {
        imageCacheFilePath = context.getCacheDir().toString();
    }

    public void cancel() {
        isCancelled = true;
    }

    public ImageLoader setCircular(boolean isCircular) {
        this.isCircular = isCircular;

        return this;
    }

    public ImageLoader setCrop(Rect crop) {
        this.crop = crop;

        return this;
    }

    public void getImageFromUrl(final String requestUrl, final ResultCallback<Bitmap> resultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String imageCacheFileName = requestUrl.substring(requestUrl.lastIndexOf("/") + 1, requestUrl.lastIndexOf(".") + 4);
                    File imageCacheFile = new File(imageCacheFilePath, imageCacheFileName);
                    if (imageCacheFile.exists()) {
                        setPostProcessing(BitmapFactory.decodeFile(imageCacheFile.getPath()), resultCallback);
                    } else {
                        InputStream urlInputStream = new URL(requestUrl).openStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(imageCacheFile);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;

                        while ((length = urlInputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, length);
                        }

                        buffer = byteArrayOutputStream.toByteArray();
                        fileOutputStream.write(buffer);
                        fileOutputStream.flush();
                        fileOutputStream.close();

                        setPostProcessing(BitmapFactory.decodeByteArray(buffer, 0, buffer.length), resultCallback);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setPostProcessing(Bitmap resultBitmap, ResultCallback<Bitmap> resultCallback) {
        if (resultBitmap == null) {
            return;
        }

        if (isCircular) {
            resultBitmap = bitmapToCircle(resultBitmap);
        }

        if (crop != null) {
            resultBitmap = cropBitmap(resultBitmap, crop);
        }

        if (!isCancelled) {
            resultCallback.onResult(resultBitmap);
        }
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
