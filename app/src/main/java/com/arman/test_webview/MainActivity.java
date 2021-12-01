package com.arman.test_webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Entity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements PhantomGenerationFragment.PhanGenCallbacks{

    LineChart lineChart;

    //**
    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private PhantomGenerationFragment mTaskFragment;
    FragmentManager fm = getSupportFragmentManager();
    //**

    public static final String TAG = "CCCP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        WebView myWebView = (WebView) findViewById(R.id.webview);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSaveFormData(true);


        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://univer.kaznu.kz");

        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);

        //PhantomGeneration.PrintMatrix();

        mTaskFragment = (PhantomGenerationFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        if (mTaskFragment == null) {
            mTaskFragment = new PhantomGenerationFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        lineChart=(LineChart) findViewById(R.id.lineChart);
        ArrayList<String> xAXES=new ArrayList<>();
        ArrayList<Entry> yAXESsin=new ArrayList<>();
        ArrayList<Entry> yAXEScos=new ArrayList<>();
        double x=0;
        int numDataPoints=100;
        for(int i=0;i<numDataPoints;i++){
            float sinFunction=Float.parseFloat(String.valueOf(Math.sin(x)));
            float cosFunction=Float.parseFloat(String.valueOf(Math.cos(x)));
            x = x+ 0.1;
            yAXESsin.add(new Entry(sinFunction,i));
            yAXEScos.add(new Entry(cosFunction,i));
            xAXES.add(i,String.valueOf(x));
        }
        String[] xaxes =  new String[xAXES.size()];
        for(int i=0;i<xAXES.size();i++){
            xaxes[i]=xAXES.get(i).toString();
        }
        ArrayList<ILineDataSet> lineDataSets=new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAXEScos,"cos");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(yAXESsin,"sin");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.RED);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        lineChart.setData(new LineData(lineDataSets));
        lineChart.setVisibleXRangeMaximum(65f);

    }

    public static Bitmap slice_bitmap;

    @Override
    public void onProgressUpdate(String text) {
       // Log.v(TAG, String.valueOf(999));
        TextView tv = (TextView)findViewById(R.id.TextField);
        tv.setText(text.toString());
        //TextField.setImageBitmap(slice_bitmap);
    }

    @Override
    public void onCancelled() {

        PhantomGenerationFragment running_fragment = (PhantomGenerationFragment)
                getSupportFragmentManager().findFragmentByTag(TAG_TASK_FRAGMENT);

    }


    @Override
    public void onPostExecute() {

        mTaskFragment=null; // can restart backprojection by clicking the button
        // or should I call on detach?...

    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission to READ EXTERNAL STORAGE is granted");
                return true;
            } else {

                Log.v(TAG, "Permission to READ EXTERNAL STORAGE is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "permission to READ EXTERNAL STORAGE is automatically granted on sdk<23 upon installation");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission to WRITE EXTERNAL STORAGE is granted");
                return true;
            } else {

                Log.v(TAG, "Permission to WRITE EXTERNAL STORAGE is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "permission to WRITE EXTERNAL STORAGE is automatically granted on sdk<23 upon installation");
            return true;
        }
    }

}