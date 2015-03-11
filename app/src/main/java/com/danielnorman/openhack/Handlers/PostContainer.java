package com.danielnorman.openhack.Handlers;

import android.graphics.Bitmap;

import com.danielnorman.openhack.MainActivity;
import com.parse.ParseObject;

/**
 * Created by Daniel on 2/28/15.
 */
public class PostContainer {
    public static final int SMALL_BITMAP_SIZE = 140;

    private ParseObject mParseObject;
    private Bitmap mBitmap;
    private Bitmap mSmallBitmap;
    private boolean mShouldReloadBitmap;
    public boolean mShouldLoadSmallBitmapOnly;

    public PostContainer(ParseObject parseObject) {
        this.mParseObject = parseObject;
        this.mBitmap = null;
        this.mShouldReloadBitmap = true;
        this.mShouldLoadSmallBitmapOnly = true;
    }
    public void recycleBitmap() {
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
            this.mBitmap = null;
            mShouldReloadBitmap = true;
        }
    }
    public void reloadBitmap(MainActivity mainActivity) {
        if (mShouldReloadBitmap && this.mBitmap == null) {
            mShouldReloadBitmap = false;
            ImageDownloader downloader = new ImageDownloader(mainActivity);
            downloader.execute(this);
        }
    }

    public void setBitmap(Bitmap bmp) {
        this.mBitmap = bmp;
        this.mSmallBitmap = Bitmap.createScaledBitmap(this.mBitmap, PostContainer.SMALL_BITMAP_SIZE, PostContainer.SMALL_BITMAP_SIZE, false);
    }

    public void setSmallBitmap(Bitmap bmp) {
        this.mSmallBitmap = bmp;
    }

    public ParseObject getParseObject() { return this.mParseObject; }
    public Bitmap getBitmap() { return this.mBitmap; }
    public Bitmap getSmallBitmap() { return this.mSmallBitmap; }
}
