package com.my.vkclient.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnimRes;
import android.util.LruCache;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Rect;

import java.io.ByteArrayOutputStream;
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

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void getImageFromUrl(final ImageView imageView, final String requestUrl, int initialWidth, int initialHeight, final @AnimRes int animation) {
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

        imageView.setTag(R.id.IMAGE_TAG_SHOW_URL, null);

        if (requestUrl == null) {
            return;
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap resultBitmap = getFromMemoryCache(requestUrl);

                if (resultBitmap == null) {
                    File imageCacheFile = new File(imageView.getContext().getCacheDir().toString(), Uri.parse(requestUrl).getLastPathSegment());
                    resultBitmap = getFromDiskCache(imageCacheFile, imageView);

                    if (resultBitmap == null) {
                        resultBitmap = getFromNetwork(imageCacheFile, imageView, requestUrl);
                    }

                    if (resultBitmap != null) {
                        putInMemoryCache(resultBitmap, requestUrl);
                    }
                }

                if (resultBitmap != null &&
                        imageView.getTag(R.id.IMAGE_TAG_URL).equals(requestUrl) &&
                        !imageView.getTag(R.id.IMAGE_TAG_URL).equals(imageView.getTag(R.id.IMAGE_TAG_SHOW_URL))) {
                    imageView.setTag(R.id.IMAGE_TAG_SHOW_URL, requestUrl);
                    setResultToImageView(imageView, resultBitmap, animation);
                }
            }
        });
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

    private Bitmap getFromDiskCache(final File imageCacheFile, ImageView imageView) {
        if (imageCacheFile.exists()) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(imageCacheFile.getPath(), options);
            options.inSampleSize = calculateInSampleSize(options, imageView.getWidth(), imageView.getHeight());
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(imageCacheFile.getPath(), options);
        }

        return null;
    }

    private void putInDiskCache(final File imageCacheFile, final byte[] buffer) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(imageCacheFile)) {
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getFromNetwork(final File imageCacheFile, final ImageView imageView, final String requestUrl) {
        try (InputStream urlInputStream = new URL(requestUrl).openStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[Constants.INT_ONE_KB];
            int length;

            while ((length = urlInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            buffer = byteArrayOutputStream.toByteArray();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
            options.inSampleSize = calculateInSampleSize(options, imageView.getWidth(), imageView.getHeight());
            options.inJustDecodeBounds = false;

            putInDiskCache(imageCacheFile, buffer);

            return BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setResultToImageView(final ImageView imageView, final Bitmap resultBitmap, final @AnimRes int animation) {
        final Boolean isCircular = (Boolean) imageView.getTag(R.id.IMAGE_TAG_IS_CIRCULAR);
        final Rect crop = (Rect) imageView.getTag(R.id.IMAGE_TAG_CROP);
        final Bitmap postProcessedBitmap = getPostProcessedBitmap(resultBitmap, isCircular, crop);

        mainLooperHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(postProcessedBitmap);

                imageView.startAnimation(AnimationUtils.loadAnimation(imageView.getContext(), animation));
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
}
