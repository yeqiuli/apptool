package com.hgy.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.hgy.aty.R;
import com.tecsun.network.frg.BaseFrg;

public class TwoFrg extends BaseFrg {

    public static TwoFrg newInstance() {
        return new TwoFrg();
    }

    @Override
    public int getLayout() {
        return R.layout.frg_one;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initView();
    }

    private void initView() {
        TextView textView =  getView(R.id.tv);
        textView.setText("TwoFrg");
    }

}
