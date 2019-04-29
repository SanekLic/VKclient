package com.my.vkclient;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.util.LruCache;
import android.widget.ImageView;

import com.my.vkclient.entities.Rect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class ImageLoader {
    private static final int PERCENTAGE = 100;
    private static final Executor cachedThreadPool = Executors.newFixedThreadPool(20);

    private static final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024 / 2)) {
        @Override
        protected int sizeOf(final String key, final Bitmap value) {
            return value.getByteCount() / 1024;
        }
    };

    static void getImageFromUrl(final ImageView imageView, final String requestUrl) {
        imageView.setImageDrawable(null);
        imageView.setAlpha(0f);
        imageView.setTag(R.id.IMAGE_TAG_URL, requestUrl);

        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap resultBitmap = getFromMemoryCache(requestUrl);

                if (resultBitmap == null) {
                    File imageCacheFile = new File(imageView.getContext().getCacheDir().toString(), Uri.parse(requestUrl).getLastPathSegment());
                    resultBitmap = getFromDiskCache(imageCacheFile);

                    if (resultBitmap == null) {
                        resultBitmap = getFromNetwork(requestUrl, imageCacheFile);
                    }

                    putInMemoryCache(resultBitmap, requestUrl);
                }

                if (resultBitmap != null) {
                    setResultToImageView(imageView, resultBitmap, requestUrl);
                }
            }
        });
    }

    private static Bitmap getFromMemoryCache(final String requestUrl) {
        synchronized (lruCache) {
            return lruCache.get(requestUrl);
        }
    }

    private static void putInMemoryCache(final Bitmap bitmap, final String requestUrl) {
        synchronized (lruCache) {
            lruCache.put(requestUrl, bitmap);
        }
    }

    private static Bitmap getFromDiskCache(final File imageCacheFile) {
        if (imageCacheFile.exists()) {
            return BitmapFactory.decodeFile(imageCacheFile.getPath());
        }

        return null;
    }

    private static void putInDiskCache(final File imageCacheFile, final byte[] buffer) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageCacheFile);
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getFromNetwork(final String requestUrl, final File imageCacheFile) {
        try {
            InputStream urlInputStream = new URL(requestUrl).openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            while ((length = urlInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            buffer = byteArrayOutputStream.toByteArray();
            putInDiskCache(imageCacheFile, buffer);

            return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void setResultToImageView(final ImageView imageView, final Bitmap resultBitmap, final String requestUrl) {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (imageView.getTag(R.id.IMAGE_TAG_URL).equals(requestUrl)) {
                    boolean isCircular = imageView.getTag(R.id.IMAGE_TAG_IS_CIRCULAR) == null ? false : (Boolean) imageView.getTag(R.id.IMAGE_TAG_IS_CIRCULAR);
                    Rect crop = (Rect) imageView.getTag(R.id.IMAGE_TAG_CROP);
                    imageView.setImageBitmap(getPostProcessedBitmap(resultBitmap, isCircular, crop));

                    Animator animator = AnimatorInflater.loadAnimator(imageView.getContext(), R.animator.image_change_visibility_animator);
                    animator.setTarget(imageView);
                    animator.start();
                }
            }
        });
    }

    private static Bitmap getPostProcessedBitmap(final Bitmap inputBitmap, final boolean isCircular, final Rect crop) {
        if (inputBitmap == null) {
            return null;
        }

        Bitmap outputBitmap = inputBitmap;

        if (isCircular) {
            outputBitmap = bitmapToCircle(outputBitmap);
        }

        if (crop != null) {
            outputBitmap = cropBitmap(outputBitmap, crop);
        }

        return outputBitmap;
    }

    private static Bitmap bitmapToCircle(Bitmap input) {
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

    private static Bitmap cropBitmap(Bitmap input, Rect crop) {
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
