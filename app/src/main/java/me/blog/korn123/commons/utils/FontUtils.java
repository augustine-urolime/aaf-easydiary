package me.blog.korn123.commons.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import me.blog.korn123.commons.constants.Path;

/**
 * Created by CHO HANJOONG on 2017-03-16.
 */

public class FontUtils {

    private static Typeface mTypeface;

    public static void setTypefaceDefault(TextView view) {
        view.setTypeface(Typeface.DEFAULT);
    }

    public static void setTypeface(Context context, AssetManager assetManager, TextView view) {
        view.setTypeface(getTypeface(context, assetManager));
    }

    public static Typeface getTypeface(Context context, AssetManager assetManager) {
        if (mTypeface == null) {
            mTypeface = setCurrentTypeface(context, assetManager);
        }
        return  mTypeface;
    }

    public static Typeface setCurrentTypeface(Context context, AssetManager assetManager) {
        String currentFont = CommonUtils.loadStringPreference(context, "font_setting", "NanumPen.ttf");
        if (StringUtils.equals(currentFont, "Default")) {
            mTypeface = Typeface.DEFAULT;
        } else {
            try {
                String[] fonts = context.getAssets().list("fonts");
                boolean isEmbeddedFont = false;
                for (String font : fonts) {
                    if (StringUtils.equals(font, currentFont)) {
                        isEmbeddedFont = true;
                        break;
                    }
                }
                if (isEmbeddedFont) {
                    mTypeface = Typeface.createFromAsset(assetManager, "fonts/" + currentFont);
                } else {
                    mTypeface = Typeface.createFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + Path.USER_CUSTOM_FONTS_DIRECTORY + currentFont);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mTypeface;
    }

    public static Typeface createTypeface(Context context, AssetManager assetManager, String fontName) {
        Typeface typeface = null;
        try {
            String[] fonts = context.getAssets().list("fonts");
            boolean isEmbeddedFont = false;
            for (String font : fonts) {
                if (StringUtils.equals(font, fontName)) {
                    isEmbeddedFont = true;
                    break;
                }
            }
            if (isEmbeddedFont) {
                typeface = Typeface.createFromAsset(assetManager, "fonts/" + fontName);
            } else {
                typeface = Typeface.createFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + Path.USER_CUSTOM_FONTS_DIRECTORY + fontName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return typeface;
    }

    public static void setToolbarTypeface(Context context, Toolbar toolbar, AssetManager assetManager) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof  TextView) {
                setTypeface(context, assetManager, (TextView)view);
//                ((TextView) view).setTypeface(Typeface.DEFAULT);
            }
        }
    }

    public static void setToolbarTypeface(Toolbar toolbar, Typeface typeface) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof  TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        }
    }
}
