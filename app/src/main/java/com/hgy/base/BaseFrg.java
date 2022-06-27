package com.hgy.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hgy.aty.R;
import com.tecsun.network.frg.KeyBack;

public abstract class BaseFrg extends Fragment implements KeyBack {
    protected View mView;
    protected boolean isPrepared;
    protected boolean isVisible;
    protected BaseActivity baseAty;

    public abstract
    @LayoutRes
    int getLayout();

    public int getRes(int color) {
        return getResources().getColor(color);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayout(), container, false);
        mView.setClickable(true);
        return mView;
    }

    public void setClickListener(View.OnClickListener onClickListener, int... views) {
        for (int view : views) {
            getView(view).setOnClickListener(onClickListener);
        }
    }

    protected <T extends View> T getView(int id) {
        return mView.findViewById(id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initBack() {
        View back = getView(R.id.imgBack);
        if (back != null) {
            back.setOnClickListener(v -> baseAty.onBackPressed());
        }


    }
    public void setTitle(String title) {
        TextView titleTxt = getView(R.id.tvTitle);
        if (titleTxt != null) {
            titleTxt.setText(title);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finishCreateView(savedInstanceState);
        initBack();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseAty = (BaseActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public abstract void finishCreateView(Bundle state);//创建视图结束

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyBack() {
        return false;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
    }

    protected void onInvisible() {
    }
}
