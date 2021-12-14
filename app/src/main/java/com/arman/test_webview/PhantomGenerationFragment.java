package com.arman.test_webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhantomGenerationFragment extends Fragment {

    //private PhantomGenerationTask PGTask;
    private static final String TAG = "CCCP";
    private PhanGenCallbacks PGCallbacks;
    private Runnable PhantomGenerationTask = new Runnable() {

        @Override
        public void run() {

            //Get the text file
            File file = new File("/sdcard/Download/Attenuation coefficients/NIST elements/Aluminum.txt");
            //Read text from file
            StringBuilder text = new StringBuilder();
            BufferedReader br;
            int firstPlus;
            ArrayList< Double > myDoubles = new ArrayList < Double >();
            Matcher DoubleFound;
            // 08.11.2021 Need some safety for possibly wrong table data
            try {
                br = new BufferedReader(new FileReader(file));
                String line = null;
                while (true) {
                    try {
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // find first data with +e
                    firstPlus = line.indexOf('+');
                    if(!(firstPlus<0)){
                        DoubleFound = Pattern.compile( "[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?" ).matcher( line );
                        while ( DoubleFound.find() )
                        {
                            double element = Double.parseDouble( DoubleFound.group() );
                            myDoubles.add( element );
                            text.append(element);
                            text.append('\n');
                            //PGCallbacks.onProgressUpdate(String.valueOf(element));

                        }

                        //    Log.v(TAG, line);
                        //    Log.v(TAG, String.valueOf(firstEplus));
                    }
                    //text.append(line);
                    //text.append('\n');
                }
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //for ( double element: myDoubles ){
            //    Log.v(TAG, String.valueOf(element));
            //Log.v(TAG, String.valueOf(text));
            PGCallbacks.onProgressUpdate(text.toString());
            //}
        }

    };

     //public void onActivityCancelled() {
    //    PGTask.cancel(true);
    //}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        PGCallbacks = (PhanGenCallbacks) activity;
        Log.v(TAG, "I'm in onAttach!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // OpenCVLoader.initDebug();
        // Retain this fragment across configuration changes.
        setRetainInstance(true); // does not really make a difference if it is even false

        Executor mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        mSingleThreadExecutor.execute(PhantomGenerationTask);

        //PGTask = new PhantomGenerationTask();
        //PGTask.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        PGCallbacks = null;
        Log.v(TAG, "I'm on onDetach");
    }

    interface PhanGenCallbacks {
        void onProgressUpdate(String text);
        void onCancelled();
        void onPostExecute();
    }

}