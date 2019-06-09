package com.my.vkclient.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Rect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImageLoader {
    private static ImageLoader instance;
    private final LruCache<String, Bitmap> lruCache;
    private final Handler mainLooperHandler;
    private Executor executor;

    private ImageLoader() {
        executor = Executors.newCachedThreadPool();
        mainLooperHandler = new Handler(Looper.getMainLooper());

        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / Constants.INT_ONE_KB / 4)) {
            @Override
            protected int sizeOf(final String key, final Bitmap value) {
                return value.getByteCount() / Constants.INT_ONE_KB;
            }
        };
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }

        return instance;
    }

    private Bitmap getFromMemoryCache(final String requestUrl) {
        synchronized (lruCache) {
            return lruCache.get(requestUrl);
        }
    }

    private void putInMemoryCache(final Bitmap bitmap, final String requestUrl) {
        synchronized (lruCache) {
            lruCache.put(requestUrl, bitmap);
        }
    }

    private Bitmap getFromDiskCache(final File imageCacheFile) {
        if (imageCacheFile.exists()) {
            return BitmapFactory.decodeFile(imageCacheFile.getPath());
        }

        return null;
    }

    private void putInDiskCache(final Bitmap bitmap, final File imageCacheFile) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(imageCacheFile)) {
            bitmap.compress(Bitmap.CompressFormat.WEBP, Constants.ImageLoader.IMAGE_COMPRESS_QUALITY, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getFromNetwork(final String requestUrl) {
        try (InputStream urlInputStream = new URL(requestUrl).openStream()) {
            return BitmapFactory.decodeStream(urlInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setResultToImageView(final ImageView imageView, final Bitmap resultBitmap) {
        final Boolean isCircular = (Boolean) imageView.getTag(R.id.IMAGE_TAG_IS_CIRCULAR);
        final Rect crop = (Rect) imageView.getTag(R.id.IMAGE_TAG_CROP);
        final Bitmap postProcessedBitmap = getPostProcessedBitmap(resultBitmap, isCircular, crop);

        mainLooperHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(postProcessedBitmap);

                Animator animator = AnimatorInflater.loadAnimator(imageView.getContext(), R.animator.image_change_visibility_animator);
                animator.setTarget(imageView);
                animator.start();
            }
        });
    }

    private Bitmap getPostProcessedBitmap(final Bitmap inputBitmap, final Boolean isCircular, final Rect crop) {
        if (inputBitmap == null) {
            return null;
        }

        Bitmap outputBitmap = inputBitmap;

        if (isCircular != null && isCircular) {
            outputBitmap = bitmapToCircle(outputBitmap);
        }

        if (crop != null) {
            outputBitmap = cropBitmap(outputBitmap, crop);
        }

        return outputBitmap;
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
        float x = width * crop.getX() / Constants.ImageLoader.PERCENTAGE;
        float y = height * crop.getY() / Constants.ImageLoader.PERCENTAGE;
        float x2 = width * crop.getX2() / Constants.ImageLoader.PERCENTAGE;
        float y2 = height * crop.getY2() / Constants.ImageLoader.PERCENTAGE;

        return Bitmap.createBitmap(input, (int) x, (int) y, (int) (x2 - x), (int) (y2 - y));
    }

    public void getImageFromUrl(final ImageView imageView, final String requestUrl, int initialWidth, int initialHeight) {
        if (requestUrl == null || requestUrl.equals(imageView.getTag(R.id.IMAGE_TAG_URL))) {
            return;
        }

        imageView.setTag(R.id.IMAGE_TAG_URL, requestUrl);

        if (initialWidth == 0 || initialHeight == 0) {
            imageView.setImageDrawable(null);
        } else {
            Rect crop = (Rect) imageView.getTag(R.id.IMAGE_TAG_CROP);

            if (crop != null) {
                imageView.setImageBitmap(cropBitmap(Bitmap.createBitmap(initialWidth, initialHeight, Bitmap.Config.ALPHA_8), crop));
            } else {
                imageView.setImageBitmap(Bitmap.createBitmap(initialWidth, initialHeight, Bitmap.Config.ALPHA_8));
            }
        }

        Integer imageTagOldLoadIteration = (Integer) imageView.getTag(R.id.IMAGE_TAG_LOAD_ITERATION);
        final Integer imageTagNewLoadIteration = imageTagOldLoadIteration == null ? 0 : imageTagOldLoadIteration + 1;

        imageView.setTag(R.id.IMAGE_TAG_LOAD_ITERATION, imageTagNewLoadIteration);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap resultBitmap = getFromMemoryCache(requestUrl);

                if (resultBitmap == null) {
                    File imageCacheFile = new File(imageView.getContext().getCacheDir().toString(), Uri.parse(requestUrl).getLastPathSegment());
                    resultBitmap = getFromDiskCache(imageCacheFile);

                    if (resultBitmap == null) {
                        resultBitmap = getFromNetwork(requestUrl);

                        if (resultBitmap != null) {
                            putInDiskCache(resultBitmap, imageCacheFile);
                        }
                    }

                    if (resultBitmap != null) {
                        putInMemoryCache(resultBitmap, requestUrl);
                    }
                }

                if (resultBitmap != null &&
                        imageView.getTag(R.id.IMAGE_TAG_URL).equals(requestUrl) &&
                        imageView.getTag(R.id.IMAGE_TAG_LOAD_ITERATION).equals(imageTagNewLoadIteration)) {
                    setResultToImageView(imageView, resultBitmap);
                }
            }
        });
    }
}
