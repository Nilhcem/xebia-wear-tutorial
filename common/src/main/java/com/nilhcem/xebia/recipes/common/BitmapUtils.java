package com.nilhcem.xebia.recipes.common;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    private BitmapUtils() {
        throw new UnsupportedOperationException();
    }

    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
