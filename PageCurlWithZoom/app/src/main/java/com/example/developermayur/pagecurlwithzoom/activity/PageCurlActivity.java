package com.example.developermayur.pagecurlwithzoom.activity;

import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.developermayur.pagecurlwithzoom.R;
import com.example.developermayur.pagecurlwithzoom.utility.BookLayout;

public class PageCurlActivity extends ActionBarActivity {

    public static int PageCount= 0;

    BookLayout bookLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_curl);

        bookLayout = (BookLayout) findViewById(R.id.mainpage);
        bookLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
