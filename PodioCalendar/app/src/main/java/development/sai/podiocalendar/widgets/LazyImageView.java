package development.sai.podiocalendar.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.podio.sdk.ImageLoader;
import com.podio.sdk.PodioError;
import com.podio.sdk.internal.Utils;

/**
 * Created by sai on 8/3/16.
 */
public class LazyImageView extends ImageView implements ImageLoader.ImageListener {
    protected Bitmap fallback;
    protected String expectedUrl;

    public LazyImageView(Context context) {
        super(context);
        setup(context, null);
    }

    public LazyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public LazyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    public void loadImage(ImageLoader imageLoader, ImageLoader.Size size, String url) {
        expectedUrl = url;

        if (Utils.notEmpty(url)) {
            imageLoader.loadImage(url, size, this);
        } else {
            setImageBitmap(fallback);
        }
    }

    public void loadImage(ImageLoader imageLoader, int resourceId) {
        expectedUrl = Integer.toString(resourceId, 10);

        if (resourceId > 0) {
            imageLoader.loadImage(getContext(), resourceId, this);
        } else {
            setImageBitmap(fallback);
        }
    }

    @Override
    public void onImageReady(Bitmap bitmap, String url, boolean isFromCache) {
        if (bitmap != null && (Utils.isEmpty(expectedUrl) || expectedUrl.equals(url))) {
            setImageBitmap(bitmap);
        } else {
            setImageBitmap(fallback);
        }
    }

    @Override
    public void onErrorOccurred(PodioError podioError, String url) {
        Log.e("IMAGE", podioError.getLocalizedMessage());
        setImageBitmap(fallback);
    }

    private void setup(Context context, AttributeSet attrs) {
        fallback = null;

        if (context != null && attrs != null) {
            int id = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);

            if (id != 0) {
                fallback = BitmapFactory.decodeResource(context.getResources(), id);
            }
        }

        if (fallback == null) {
            fallback = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            new Canvas(fallback).drawColor(Color.WHITE);
        }
    }

}