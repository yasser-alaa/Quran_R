package com.example.tefah.quran;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class SpeechRecognizerDemo extends Service implements RecognitionListener{

    private final IBinder mBinder = new LocalBinder();
    private static final String MODEL = "t5";
    private int test = 1;
    private boolean order = false;
    public class LocalBinder extends Binder {
        public SpeechRecognizerDemo getService() {
            // Return this instance of LocalService so clients can call public methods
            return SpeechRecognizerDemo.this;
        }
    }

    public String out;
    public boolean toast = false;


    private SpeechRecognizer recognizer;

    @Override
    public void onCreate() {
        //super.onCreate();
        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(SpeechRecognizerDemo.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);

            return;
        }
        runRecognizerSetup();
        Toast.makeText(this, "برجاء الإنتظار حتى يتم إعداد الباحث الصوتى ...", Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    @SuppressLint("StaticFieldLeak")
    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(SpeechRecognizerDemo.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                } else {
                    switchSearch(MODEL);
                }
            }
        }.execute();
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {

    }

    public void restart(){
       // this.recognizer.stop();
        out="";
        order = true;
        switchSearch(MODEL);
    }
    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {

        //((TextView) findViewById(R.id.caption_text)).setText("This is what I recognized:");
        if (hypothesis != null) {
            out = hypothesis.getHypstr();
            //String text = hypothesis.getHypstr();
            // ((TextView) findViewById(R.id.result_text)).setText(((TextView) findViewById(R.id.partial_text)).getText() + "\n" +((TextView) findViewById(R.id.result_text)).getText());
            //makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            if(toast)
                Toast.makeText(this, hypothesis.getHypstr(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        //((TextView) findViewById(R.id.caption_text)).setText("I am listening...");
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        switchSearch(MODEL);
    }


    private void switchSearch(String searchName) {

        if(order & test==0){
            Toast.makeText(this, "start speech.!", Toast.LENGTH_LONG).show();
            //recognizer.stop();
            recognizer.startListening(searchName, 10000);
            order = false;
            return;
        }else if(order){
            Toast.makeText(this, "من فضلك إنتظر قليلاً", Toast.LENGTH_LONG).show();
            out = "Please wait while Preparing the Speech Recognizer.!";
        }else if (test != 0) {
            Toast.makeText(this, "تم إعداد الباحث الصوتى يمكنك إستخدامه الآن ", Toast.LENGTH_LONG).show();
            recognizer.stop();
            recognizer.startListening(searchName, 100);
            test = test -1 ;
        }else {
            recognizer.stop();
        }
        //((TextView) findViewById(R.id.caption_text)).setText("You can recite now!");

    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        SpeechRecognizerSetup recognizerSetup = SpeechRecognizerSetup.defaultSetup();
        recognizerSetup.setAcousticModel(new File(assetsDir, "t5.cd_cont_700"));
        recognizerSetup.setDictionary(new File(assetsDir, "t5.dic"));
        recognizerSetup.setRawLogDir(assetsDir); // To disable logging of raw audio comment out this call (takes a lot of space on the device)
        //Didn't seem to has any significant impact
        //recognizerSetup.setBoolean("-remove_noise", true);
        //recognizerSetup.setKeywordThreshold(1e-1f);

        try {
            recognizer = recognizerSetup.getRecognizer();
            recognizer.addListener(this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create language model search
        File languageModel = new File(assetsDir, "t5.lm");
        recognizer.addNgramSearch(MODEL, languageModel);

        // Phonetic search
/*        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
        */
    }

    @Override
    public void onError(Exception error) {
        //((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(MODEL);
    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        Toast.makeText(this, "Speech Recognition Service Destroyed", Toast.LENGTH_LONG).show();

    }

}
