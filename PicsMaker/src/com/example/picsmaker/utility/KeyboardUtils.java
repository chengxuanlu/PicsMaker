package com.example.picsmaker.utility;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


public class KeyboardUtils {
	/**
     * Òþ²Ø¼üÅÌµÄ·½·¨
     *
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Òþ²ØÈí¼üÅÌ
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }


}
