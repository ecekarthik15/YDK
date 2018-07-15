package android.sales.rajesh.com.sales.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Karthik on 2/7/17.
 */

public class SFTextView extends TextView {

    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";


    public SFTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public SFTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Corbel.ttf");
        setTypeface(tf ,1);

    }

    private Typeface selectTypeface(Context context, int textStyle) {
    /*
    * information about the TextView textStyle:
    * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
    */
        switch (textStyle) {
            case Typeface.BOLD: // bold
                return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

            case Typeface.ITALIC: // italic
                return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");

            case Typeface.BOLD_ITALIC: // bold italic
                return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");

            case Typeface.NORMAL: // regular
            default:
                return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        }
    }
}