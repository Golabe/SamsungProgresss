package top.golabe.samsungprogresss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import top.golabe.library.SamsungProgressView;

public class MainActivity extends AppCompatActivity {

    private SamsungProgressView mSamsungProgressView;
    private static int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSamsungProgressView = findViewById(R.id.progress);
//        mSamsungProgressView.setBgColor();
//        mSamsungProgressView.setBorder();
//        mSamsungProgressView.setDuration();
//        mSamsungProgressView.setMax();
//        mSamsungProgressView.setMin();
//        mSamsungProgressView.setSuffix();
//        mSamsungProgressView.setTextColor();
//        mSamsungProgressView.setTextSize();
    }

    public void add(View view) {

        progress += 10;
        mSamsungProgressView.setProgress(progress);


    }

    public void lower(View view) {
        progress -= 10;
        mSamsungProgressView.setProgress(progress);

    }
}
