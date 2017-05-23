package com.s10576019.cyel.screensaverdemo.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.s10576019.cyel.screensaverdemo.R;

public class ScreenSaver {
    private Activity activity;
    private View savingView;
    private PowerManager.WakeLock proximityWakeLock;
    private Handler runnableHandler;
    private Runnable screenSavingDelayedRunnable;

    public ScreenSaver(@NonNull Activity activity) {
        this.activity = activity;
    }

    public void startProximityDetecting() {
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);

        //檢查API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i(String.valueOf(this), "確認此裝置API(" + Build.VERSION.SDK_INT + ") 大於 API(" + Build.VERSION_CODES.LOLLIPOP + ")");

            //檢查裝置是否支援PROXIMITY_SCREEN_OFF_WAKE_LOCK
            if (powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
                Log.i(String.valueOf(this), "確認此裝置支援PROXIMITY_SCREEN_OFF_WAKE_LOCK。");

                //取得鎖
                if (proximityWakeLock == null) {
                    proximityWakeLock = powerManager.newWakeLock(
                            PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "proximityWakeLock");
                }

                //獲得鎖
                if (proximityWakeLock != null) {
                    proximityWakeLock.acquire();
                    Log.i(String.valueOf(this), "獲得鎖");
                } else {
                    Log.i(String.valueOf(this), "取得鎖失敗");
                }

            } else {
                Log.i(String.valueOf(this), "此裝置不支援PROXIMITY_SCREEN_OFF_WAKE_LOCK，停止要求。");
            }

        } else {
            Log.i(String.valueOf(this), "此裝置的API(" + Build.VERSION.SDK_INT + ") 小於 API(" + Build.VERSION_CODES.LOLLIPOP + ")，停止要求。");
        }
    }

    public void stopProximityDetecting() {
        //釋放鎖
        if (proximityWakeLock.isHeld()) {
            proximityWakeLock.release();
            Log.i(String.valueOf(this), "釋放鎖");
        } else {
            Log.i(String.valueOf(this), "鎖未曾獲得，釋放失敗。");
        }
    }

    public void startScreenSaving() {
        //開始螢幕保護前先取消任何預約
        cancelScreenSavingDelayed();

        Log.i(this.getClass().getName(), "開始螢幕保護");
        //換到螢幕保護頁面
        if (savingView == null) {
            savingView = activity.getLayoutInflater().inflate(R.layout.view_screen_saving, null);
        }

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.getWindow().addContentView(savingView, layoutParams);

        //點擊頁面就恢復操作
        savingView.setClickable(true);
        savingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopScreenSaving();
            }
        });
        //螢幕黯淡
        Log.i(this.getClass().getName(), "螢幕黯淡");
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = 0;
        activity.getWindow().setAttributes(params);
    }

    public void stopScreenSaving() {
        Log.i(this.getClass().getName(), "停止螢幕保護");
        //移開螢幕保護頁面
        ViewGroup viewGroup = (ViewGroup) savingView.getParent();
        viewGroup.removeView(savingView);
        //螢幕恢復亮度
        Log.i(this.getClass().getName(), "恢復螢幕亮度");
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = -1;
        activity.getWindow().setAttributes(params);
    }

    public void screenSavingDelayed(long millSeconds) {
        //建立任務
        if (screenSavingDelayedRunnable == null) {
            screenSavingDelayedRunnable = new Runnable() {
                @Override
                public void run() {
                    startScreenSaving();
                }
            };
        }

        //預約任務
        if (runnableHandler == null) {
            runnableHandler = new Handler();
        }
        cancelScreenSavingDelayed();
        runnableHandler.postDelayed(screenSavingDelayedRunnable, millSeconds);
        Log.i(this.getClass().getName(), "預約"+millSeconds+"毫秒後開始螢幕保護");
    }

    public void cancelScreenSavingDelayed() {
        if (runnableHandler != null && screenSavingDelayedRunnable != null) {
            Log.i(this.getClass().getName(), "取消先前已存在的預約");
            runnableHandler.removeCallbacks(screenSavingDelayedRunnable);
        }
    }
}