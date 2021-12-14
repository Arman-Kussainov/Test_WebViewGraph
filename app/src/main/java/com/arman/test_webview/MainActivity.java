package com.arman.test_webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements PhantomGenerationFragment.PhanGenCallbacks{
    LineChart MyChart;
    ArrayList<Entry> MyFun =new ArrayList<>();
    ArrayList<ILineDataSet> lineDataSets=new ArrayList<>();
   // LineDataSet MylineDataSet = new LineDataSet(MyFun,"My Function");

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

        //MyChart =(LineChart) findViewById(R.id.lineChart);
//        ArrayList<Entry> MyFun =new ArrayList<>();

        /*for(float x=-6.14f;x<3.14f;x+=0.1f){
            float sinFunction=Float.parseFloat(String.valueOf(Math.tan(x)));
            MyFun.add(new Entry((float) x, sinFunction));
        }
        ArrayList<ILineDataSet> lineDataSets=new ArrayList<>();
        LineDataSet MylineDataSet = new LineDataSet(MyFun,"My Function");

        MylineDataSet.setDrawCircles(false);
        MylineDataSet.setColor(Color.BLUE);

        lineDataSets.add(MylineDataSet);
        MyChart.setData(new LineData(lineDataSets));*/

    }

    public static Bitmap slice_bitmap;

    @Override
    public void onProgressUpdate(String text) {
        TextView tv = (TextView)findViewById(R.id.TextField);
        tv.setText(text);
        //TextField.setImageBitmap(slice_bitmap);

        DrawMyChart(text);
    }

    public void DrawMyChart(String text){
        int c_ounter=1;
        Matcher DoubleFound;
        //Log.v(TAG, String.valueOf(Double.valueOf(text)));
        DoubleFound = Pattern.compile( "[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?" ).matcher( text );
        float E=0,mu=0;
        while ( DoubleFound.find() )
        {
            float element = Float.parseFloat( DoubleFound.group() );
            if(c_ounter%3==1){
                E=element;
            }
            if(c_ounter%3==2){
                mu=element;
            }

            if((E!=0)&&(mu!=0)){
                Log.v(TAG, String.valueOf(mu));
                MyFun.add(new Entry((float) Math.log(E), (float) Math.log(mu)));
                E=0;mu=0;
            }

            c_ounter++;

        }

        LineDataSet MylineDataSet = new LineDataSet(MyFun,"My Function");
        MyChart =(LineChart) findViewById(R.id.lineChart);
        MylineDataSet.setDrawCircles(false);
        MylineDataSet.setColor(Color.BLUE);
        lineDataSets.add(MylineDataSet);
        MyChart.setData(new LineData(lineDataSets));
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