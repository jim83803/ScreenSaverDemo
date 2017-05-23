package com.s10576019.cyel.screensaverdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.s10576019.cyel.screensaverdemo.tools.ScreenSaver;

public class MainActivity extends AppCompatActivity {
    private ScreenSaver screenSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenSaver = new ScreenSaver(this);
    }

    public void clickStartSavingScreenButton(View view) {
        screenSaver.startScreenSaving();
    }

    public void clickSavingScreenButtonDelayed5s(View view) {
        screenSaver.screenSavingDelayed(5000);
    }

    public void clickCancelRunnableButton(View view) {
        screenSaver.cancelScreenSavingDelayed();
    }

    public void clickStartingProximityDetectingButton(View view) {
        screenSaver.startProximityDetecting();
    }

    public void clickStopProximityDetectingButton(View view) {
        screenSaver.stopProximityDetecting();
    }
}
