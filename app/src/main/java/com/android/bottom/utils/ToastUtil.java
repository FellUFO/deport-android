package com.android.bottom.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void getLongToast(String content, Context context) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
    public static void getLongToast(int content, Context context) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void getShortToast(String content, Context context) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
    public static void getShortToast(int content, Context context) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
