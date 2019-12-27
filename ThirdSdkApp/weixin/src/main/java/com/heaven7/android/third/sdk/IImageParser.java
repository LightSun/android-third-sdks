package com.heaven7.android.third.sdk;

import android.graphics.Bitmap;

public interface IImageParser {

    Bitmap parse(String path,int requireWidth, int requireHeight);

}
