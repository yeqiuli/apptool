package com.tecsun.sixse.security;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecurityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        findViewById(R.id.textView).setOnClickListener(view -> startActivity(new Intent(SecurityActivity.this, IActivity.class)));
    }
}