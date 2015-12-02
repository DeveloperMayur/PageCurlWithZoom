package com.example.developermayur.pagecurlwithzoom.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.developermayur.pagecurlwithzoom.R;
import com.example.developermayur.pagecurlwithzoom.activity.PageCurlActivity;


/**
 * Created by seven-bits-pc11 on 4/11/15.
 */

public class BookLayout extends CustomRelativeLayout {

    private int[] imageList = {
            R.drawable.images01, R.drawable.images02, R.drawable.images03, R.drawable.images04, R.drawable.images05,
            R.drawable.images06, R.drawable.images07, R.drawable.images08
    };

    private BookView bookView;

    private Context mContext;
    private int screenWidth;
    private int screenHeight;

    private int totalPages;

    TouchImageView book_page1_touchImageView;
    ImageView page1_bg_ImageView,page2_bg_image;

    private GestureDetector gestures;

    public BookLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BookLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {

        totalPages = 2;

        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        LayoutInflater factory = LayoutInflater.from(mContext);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView page1_bg=new ImageView(mContext);
        page1_bg.setId(R.id.page1_bg);
        page1_bg.setScaleType(ImageView.ScaleType.FIT_XY);
        page1_bg.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView page2_bg=new ImageView(mContext);
        page2_bg.setId(R.id.page2_bg);
        page2_bg.setScaleType(ImageView.ScaleType.FIT_XY);
        page2_bg.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        linearLayout.addView(page1_bg, screenWidth,screenHeight);
        linearLayout.addView(page2_bg, screenWidth,screenHeight);

        ScrollView scrollView = new ScrollView(mContext);
        scrollView.setId(R.id.scrollView);
        scrollView.addView(linearLayout);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setFadingEdgeLength(0);
        addView(scrollView);

        // add true view and some actions
        View book_page1 = factory.inflate(R.layout.book_page, null);
        addView(book_page1, screenWidth, screenHeight);

        book_page1_touchImageView = (TouchImageView) book_page1.findViewById(R.id.book_page1_image);
        page1_bg_ImageView=(ImageView) findViewById(R.id.page1_bg);
        page2_bg_image=(ImageView) findViewById(R.id.page2_bg);

        Bitmap bitmap_page=null;
        bitmap_page=decodeSampledBitmapFromResource(getResources(),imageList[0],screenWidth,screenHeight);

        book_page1_touchImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        book_page1_touchImageView.setOnTouchListener(onTouchListener);

        book_page1_touchImageView.setImageBitmap(bitmap_page);
        page1_bg_ImageView.setImageBitmap(bitmap_page);

        bitmap_page=decodeSampledBitmapFromResource(getResources(),imageList[1],screenWidth,screenHeight);
        page2_bg_image.setImageBitmap(bitmap_page);
        bitmap_page=null;

        GestureListener listener = new GestureListener();
        gestures = new GestureDetector(mContext, listener, null, true);

        bookView = new BookView(mContext, screenWidth, screenHeight);
        addView(bookView);
        bookView.setView(book_page1);
    }

    public void next() {

        if (PageCurlActivity.PageCount + 1 <= imageList.length - 1) {

            resetBitmap(true);

            PageCurlActivity.PageCount = PageCurlActivity.PageCount + 1;
            book_page1_touchImageView.resetZoom();

            Bitmap bitmap_page=null;
            bitmap_page=((BitmapDrawable)page2_bg_image.getDrawable()).getBitmap();

            ((BitmapDrawable)book_page1_touchImageView.getDrawable()).getBitmap().recycle();
            book_page1_touchImageView.setImageBitmap(bitmap_page);

            ((BitmapDrawable)page1_bg_ImageView.getDrawable()).getBitmap().recycle();
            page1_bg_ImageView.setImageBitmap(bitmap_page);

            bitmap_page=decodeSampledBitmapFromResource(getResources(),imageList[PageCurlActivity.PageCount+1],screenWidth,screenHeight);
            page2_bg_image.setImageBitmap(bitmap_page);
            bitmap_page=null;

        }
    }

    public void pre() {

        if (PageCurlActivity.PageCount - 1 >= 0) {

            PageCurlActivity.PageCount = PageCurlActivity.PageCount - 1;
            book_page1_touchImageView.resetZoom();

            Bitmap bitmap_page=null;
            bitmap_page=((BitmapDrawable)page1_bg_ImageView.getDrawable()).getBitmap();

            ((BitmapDrawable)page2_bg_image.getDrawable()).getBitmap().recycle();
            page2_bg_image.setImageBitmap(bitmap_page);

            bitmap_page=decodeSampledBitmapFromResource(getResources(),imageList[PageCurlActivity.PageCount],screenWidth,screenHeight);

            book_page1_touchImageView.setImageBitmap(bitmap_page);
            page1_bg_ImageView.setImageBitmap(bitmap_page);
            bitmap_page=null;

            resetBitmap(false);

        }
    }

    private Bitmap mBitmap;
    private Bitmap page1, page2;

    public void getPageContent() {
        if(mBitmap==null){
            setBitmap();
        }
    }

    private void setBitmap() {
        try{

            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight*totalPages, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(mBitmap);
            ((ScrollView)this.findViewById(R.id.scrollView)).draw(canvas);

            page1 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            Canvas canvas1 = new Canvas(page1);
            canvas1.drawBitmap(mBitmap, 0, 0, new Paint());

            page2 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            Canvas canvas2 = new Canvas(page2);
            canvas2.drawBitmap(mBitmap, 0, -screenHeight, new Paint());

            ((ScrollView)this.findViewById(R.id.scrollView)).setVisibility(View.GONE);
            bookView.init(page1, page2);

        } catch (OutOfMemoryError ex) {

            System.gc();
            System.out.println("****"+ex.getStackTrace());

        }
    }

    private void resetBitmap(boolean isNext) {
        try{

            ((ScrollView)this.findViewById(R.id.scrollView)).setVisibility(View.VISIBLE);
            if(mBitmap == null){
                mBitmap = Bitmap.createBitmap(screenWidth, screenHeight*totalPages, Bitmap.Config.RGB_565);
            }
            Canvas canvas=new Canvas(mBitmap);
            ((ScrollView)this.findViewById(R.id.scrollView)).draw(canvas);
            if (isNext){
                page1=page2;
                page2 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            } else {
                page2=page1;
                page1 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            }

            Canvas canvas1 = new Canvas(page1);
            canvas1.drawBitmap(mBitmap, 0, 0, new Paint());

            Canvas canvas2 = new Canvas(page2);
            canvas2.drawBitmap(mBitmap, 0, -screenHeight, new Paint());

            ((ScrollView)this.findViewById(R.id.scrollView)).setVisibility(View.GONE);
            bookView.setPage(page1, page2);

            if (isNext)
                bookView.next();
            else
                bookView.pre();

        } catch (OutOfMemoryError ex) {
            System.gc();
            System.out.println("****"+ex.getStackTrace());
        }

    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        try{

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            return BitmapFactory.decodeResource(res, resId, options);

        } catch (OutOfMemoryError ex){

            System.gc();
            System.out.println("****"+ex.getStackTrace());
            return null;

        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            gestures.onTouchEvent(event);
            return true;
        }
    };

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 100;
        private static final int SWIPE_MAX_OFF_PATH = 400;
        private static final int SWIPE_THRESHOLD_VELOCITY = 50;

        public GestureListener() {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                    if (!book_page1_touchImageView.isZoomed()) {
                        next();
                    }

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                    if (!book_page1_touchImageView.isZoomed()) {
                        pre();
                    }

                }
            } catch (Exception ex) {

                System.out.println("******Error GestureListener:" + ex.getStackTrace());

            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            System.out.println("******onLongPress is called");
        }


        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

}