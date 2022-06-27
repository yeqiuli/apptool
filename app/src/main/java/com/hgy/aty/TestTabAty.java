package com.hgy.aty;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hgy.adapter.TabAdapter;
import com.hgy.fragment.FourFrg;
import com.hgy.fragment.OneFrg;
import com.hgy.fragment.ThreeFrg;
import com.hgy.fragment.TwoFrg;
import com.tecsun.network.aty.BaseAty;
import com.tecsun.network.utils.LogUntil;

import java.util.ArrayList;
import java.util.List;

public class TestTabAty extends BaseAty {


    private ViewPager mViewPager;
    private TabAdapter adapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    protected int getLayout() {
        return R.layout.aty_tab;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        mFragmentList.add(OneFrg.newInstance());
        mFragmentList.add(TwoFrg.newInstance());
        mFragmentList.add(ThreeFrg.newInstance());
        mFragmentList.add(FourFrg.newInstance());
        adapter = new TabAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        tv(tv1, true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUntil.e("position：" + position);
//                LogUntil.e("positionOffset：" + positionOffset);
//                LogUntil.e("positionOffsetPixels：" + positionOffsetPixels);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {
                LogUntil.e("onPageSelected+position：" + position);
                switch (position) {
                    case 0:
                        tv(tv1, true);
                        tv(tv2, false);
                        tv(tv3, false);
                        tv(tv4, false);
                        break;
                    case 1:
                        tv(tv1, false);
                        tv(tv2, true);
                        tv(tv3, false);
                        tv(tv4, false);
                        break;
                    case 2:
                        tv(tv1, false);
                        tv(tv2, false);
                        tv(tv3, true);
                        tv(tv4, false);
                        break;
                    case 3:
                        tv(tv1, false);
                        tv(tv2, false);
                        tv(tv3, false);
                        tv(tv4, true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                LogUntil.e("state：" + state);
            }
        });
    }


    private void tv(TextView textView, boolean flag) {
        if (flag) {
            textView.setScaleX(1.2f);
            textView.setScaleY(1.2f);
//            textView.getLayoutParams().height = 48;
            textView.setElevation(2);
            textView.setBackgroundResource(R.drawable.tab_blue);
        } else {
            textView.setScaleX(1f);
            textView.setScaleY(1f);
//            textView.getLayoutParams().height = 40;
            textView.setElevation(0);
            textView.setBackgroundResource(R.drawable.tab_blue_g);
        }
        int h = textView.getLayoutParams().height;
        LogUntil.e("h=" + h);
    }

}
