package com.example.developermayur.pagecurlwithzoom.utility;

/**
 * Created by sevenbits-pc12 on 10/11/15.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by seven-bits-pc11 on 4/11/15.
 */

public abstract class CustomRelativeLayout extends RelativeLayout {

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        try {
            getPageContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract protected void getPageContent();
}