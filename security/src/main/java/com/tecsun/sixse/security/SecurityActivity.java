package com.tecsun.sixse.security;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class SecurityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        findViewById(R.id.textView).setOnClickListener(view -> startActivity(new Intent(SecurityActivity.this, IActivity.class)));
        checkWgrHelCode("eyJWZXIiOiJ2MS4wIiwiVHlwZSI6Indncl95a20iLCJVaWQiOiJPVjFCS1F2MWlCdFpVUEI1cG4yNWx3PT0iLCJFbmNvZGUiOiI2MjQ1ZmY3OWVmMDkxYjgwMGYxN2ZmZjY2ZTdhMWM2MGY3MjQwNDFkM2Y4Y2MxYzEwYWFkYzQ2ZmNhMTcxODQ2ODkwNmUzNzI5OGM4NDcxZmM2YzE3MTgwYWM4NDc2Y2QiLCJlMSI6IjgzYzgyY2I0NjNlNDFiODRhZGM2M2RjMTJkMmQ2ZDcxIn0=");
    }


    /**
     * 判断是否是外国人健康码
     */
    public String checkWgrHelCode(String s) {
        if (s == null) {
            return null;
        }
        try {
//            byte[] decodeBytes = Base64Utils.decode(s);
            //2.字节数组转为⼗六进制字符
            String json =  new String(Base64.decode(s.getBytes(), Base64.DEFAULT));
            JSONObject jsonObject = new JSONObject(json);
            boolean has = jsonObject.has("Type");
            if (has) {
                return s;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}