package com.s10576019.cyel.screensaverdemo.tools;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jim on 2017/6/5.
 */

public class CoveringPage {
    private Activity activity;
    private View view;

    public CoveringPage(@NonNull Activity activity) {
        this.activity = activity;
    }
    public void start(int layoutId) {
        Log.i(this.getClass().getName(), "開始");

        //建置介紹頁面
        if (view == null) {
            view = activity.getLayoutInflater().inflate(layoutId, null);
        }
        stop();

        //把介紹頁面覆蓋上去
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.getWindow().addContentView(view, layoutParams);

        //點擊頁面就恢復操作
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }
    public void stop() {
        Log.i(this.getClass().getName(), "停止");
        //移開螢幕保護頁面
        if (view != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
            } else {
                Log.e(this.getClass().getName(), "view沒有parent");
            }
        }
    }
}
