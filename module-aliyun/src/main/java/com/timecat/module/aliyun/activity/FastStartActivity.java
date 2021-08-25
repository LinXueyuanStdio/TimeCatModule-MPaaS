package com.timecat.module.aliyun.activity;

import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.mpaas.nebula.adapter.api.MPNebula;

public class FastStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout f = new FrameLayout(this);
        Button b = new Button(this);
        f.addView(b);
        setContentView(f);
        b.setText("app");
        b.setOnClickListener(v -> MPNebula.startApp("0000000000000000"));
    }
}
