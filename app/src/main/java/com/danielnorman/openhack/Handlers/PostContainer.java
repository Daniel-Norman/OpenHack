package com.danielnorman.openhack.Handlers;

import android.graphics.Bitmap;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/28/15.
 */
public class PostContainer {
    private ParseObject mParseObject;
    private Bitmap mBitmap;


    public PostContainer(ParseObject parseObject) {
        this.mParseObject = parseObject;
        this.mBitmap = null;
    }
    public PostContainer(ParseObject parseObject, Bitmap bitmap) {
        this.mParseObject = parseObject;
        this.mBitmap = bitmap;
    }

    public void setBitmap(Bitmap bmp) {
        this.mBitmap = bmp;
    }

    public ParseObject getParseObject() { return this.mParseObject; }
    public Bitmap getBitmap() { return this.mBitmap; }
}
