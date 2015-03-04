package com.danielnorman.openhack.Handlers;

import android.graphics.Bitmap;

import com.danielnorman.openhack.MainActivity;
import com.parse.ParseObject;

/**
 * Created by Daniel on 2/28/15.
 */
public class PostContainer {
    private ParseObject mParseObject;
    private Bitmap mBitmap;
    private boolean mShouldReloadBitmap;


    public PostContainer(ParseObject parseObject) {
        this.mParseObject = parseObject;
        this.mBitmap = null;
        this.mShouldReloadBitmap = false;
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
    }

    public ParseObject getParseObject() { return this.mParseObject; }
    public Bitmap getBitmap() { return this.mBitmap; }
}
