package com.example.netscrolldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author dufy25013
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewAndSetListener();
    }

    private void findViewAndSetListener() {
        findViewById(R.id.btn_nested_scrolling_tradition).setOnClickListener(this);
        findViewById(R.id.btn_nested_scrolling).setOnClickListener(this);
//        findViewById(R.id.btn_nested_scrolling2).setOnClickListener(this);
//        findViewById(R.id.btn_nested_scrolling2Demo).setOnClickListener(this);
//        findViewById(R.id.btn_coordinator_layout).setOnClickListener(this);
//        findViewById(R.id.btn_coor_with_appbar).setOnClickListener(this);
//        findViewById(R.id.btn_coor_with_appbar_with_coll).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_nested_scrolling_tradition:
                startActivity(new Intent(this, NestedTraditionActivity.class));
                break;
            case R.id.btn_nested_scrolling:
                startActivity(new Intent(this, NestedScrollingParentActivity.class));
                break;
            default:
                break;
        }
    }
}