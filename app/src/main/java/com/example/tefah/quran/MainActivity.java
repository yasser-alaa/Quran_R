package com.example.tefah.quran;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.tefah.quran.data.DataBaseHelper;
import com.example.tefah.quran.data.QuranDbContract;
import com.example.tefah.quran.data.QuranDbHelper;
import com.example.tefah.quran.data.TestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GreenAdapter.ListItemClickListener {

    private GreenAdapter mAdapter;
    private RecyclerView mNumbersList;
    ArrayList<String> messages = new ArrayList<String>();
    List<String> mnames ;
    private SQLiteDatabase mDb;
    Cursor mCursor;
    Cursor cursor2;
    private static final int NUM_LIST_ITEMS = 114;
    private DataBaseHelper mDBHelper;
    //------------------------------------------------

    //-------------------------------------------------
    public static final int MY_PERMISSIONS_REQUEST_RECORDE_AUDIO = 100 ;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 200 ;

    private static  boolean RECODER_TIME_OK = false;
    private String audioPath;
    public MediaRecorder recorder;
    MediaPlayer player;
    private CountDownTimer timer;



@BindView(R.id.voice_recorder)
    FloatingActionButton voiceRecorder;
@BindView(R.id.test_player)
    FloatingActionButton testPlayer;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //------------------------------------------------------------------
        mDBHelper = new DataBaseHelper(this);
        // check exists Database
        File database = getApplicationContext().getDatabasePath(DataBaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mnames =  mDBHelper.suraNames();

               /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);

    /*
     * A LinearLayoutManager is responsible for measuring and positioning item views within a
     * RecyclerView into a linear list. This means that it can produce either a horizontal or
     * vertical list depending on which parameter you pass in to the LinearLayoutManager
     * constructor. By default, if you don't specify an orientation, you get a vertical list.
     * In our case, we want a vertical list, so we don't need to pass in an orientation flag to
     * the LinearLayoutManager constructor.
     */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mNumbersList.setHasFixedSize(true);
        QuranDbHelper quranDbHelper = new QuranDbHelper(this);
        mDb = quranDbHelper.getWritableDatabase();

        //Fill the database with fake data
        TestUtil.insertFakeData(mDb);

        // COMPLETED (13) Pass in this as the ListItemClickListener to the GreenAdapter constructor
        /*
         * The GreenAdapter is responsible for displaying each item in the list.
         */
        mCursor = getallSwarNames() ;
        cursor2 = suraAyaNumber("113-2");

        // mAdapter = new GreenAdapter(NUM_LIST_ITEMS,mCursor,this,messages);
        mAdapter = new GreenAdapter(NUM_LIST_ITEMS,cursor2,this,mnames);
        mNumbersList.setAdapter(mAdapter);


        //-------------------------------------------------------------------

//        it has a problem with accessability i can search for it later
        voiceRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        recorder = new MediaRecorder();
                        if (grantPermission()) {
                            record();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (RECODER_TIME_OK) {
                            Utils.stopRecording(recorder);
                        } else {
                            Toast.makeText(MainActivity.this, "Hold to record voice",
                                    Toast.LENGTH_SHORT).show();
                            recorder.release();
                            recorder = null;
                        }
                        timer.cancel();
                        RECODER_TIME_OK = false;
                        break;
                    default:
                }
                    return true;
            }

        });
    }

    private void record() {
        audioPath = Utils.startRecording(recorder);
        timer = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                RECODER_TIME_OK = true;
            }
        }.start();
    }


    @OnClick(R.id.test_player)
    public void play(){
        if (audioPath != null)
            uploadFile();
        else
            Toast.makeText(this, getString(R.string.no_audio_recorded), Toast.LENGTH_SHORT).show();
    }

    private void uploadFile() {
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);
        File file = new File(audioPath);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("audio/3gpp"),
                        file
                );
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("audio", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(body);

//        Call<ResponseBody> call = service.getHtml();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
//                Log.i("GET REQUEST", response.toString());
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public boolean grantPermission(){
        boolean audioGranted = false;
        boolean readGranted = false;
        boolean writeGranted = false;
        if(Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
                    Log.v(this.getLocalClassName(), "SHOULD Audio Permission be explained");
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            MY_PERMISSIONS_REQUEST_RECORDE_AUDIO);
                }
            } else audioGranted = true;
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Log.v(this.getLocalClassName(), "SHOULD Write storage be explained");
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            } else writeGranted = true;
            if (audioGranted  && writeGranted)
                return true;
            else return false;
        } else
                return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORDE_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(this.getLocalClassName(), "AUDIO PERMISSION GRANTED");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Audio Permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(this.getLocalClassName(), "Storage PERMISSION GRANTED");
                    // permission was granted, yay! Do the
                    recorder = new MediaRecorder();
                    record();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Storage Permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public Cursor getallSwarNames(){
        return mDb.query(
                QuranDbContract.QuranEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null, QuranDbContract.QuranEntry.COLUMN_SURA_ID
        );
    }

    // COMPLETED (10) Override ListItemClickListener's onListItemClick method
    /**
     * This is where we receive our callback from
     * {}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Context context = MainActivity.this;
        // String nameOfSura = messages.get(clickedItemIndex);

        Intent startChildActivityIntent = new Intent(context, Main2Activity.class);

        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT,clickedItemIndex);

        startActivity(startChildActivityIntent);

        //---------------------------------------------------------

        // (11) In the beginning of the method, cancel the Toast if it isn't null
        /*
         * Even if a Toast isn't showing, it's okay to cancel it. Doing so
         * ensures that our new Toast will show immediately, rather than
         * being delayed while other pending Toasts are shown.
         *
         * Comment out these three lines, run the app, and click on a bunch of
         * different items if you're not sure what I'm talking about.
         */
//        if (mToast != null) {
//            mToast.cancel();
//        }

        //(12) Show a Toast when an item is clicked, displaying that item number that was clicked
        /*
         * Create a Toast and store it in our Toast field.
         * The Toast that shows up will have a message similar to the following:
         *
         *                     Item #42 clicked.
         */
//        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
//        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
//
//        mToast.show();

    }


    public Cursor suraAyaNumber(String suraAyaNum){
        // int ayaNumberINT =  suraAyaNum.charAt(suraAyaNum.length()-1);
        String ayaNumberSTR = suraAyaNum.substring(suraAyaNum.length()-1);
        String suraNumberSTR = suraAyaNum.substring(0,suraAyaNum.length()-2);
        // int suraNumberINT = Integer.getInteger(suraNumberSTR) ;

        Cursor cursor2=mDb.rawQuery("SELECT * FROM "+ QuranDbContract.QuranEntry.TABLE_NAME + " WHERE "
                + QuranDbContract.QuranEntry.COLUMN_SURA_ID + " = " + suraNumberSTR + " AND " + QuranDbContract.QuranEntry.COLUMN_VERSE_ID +
                " = "+ ayaNumberSTR,null);

        return cursor2;
        // mDb.query(QuranDbContract.QuranEntry.TABLE_NAME, QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT,"WHERE "+ QuranDbContract.QuranEntry.COLUMN_VERSE_ID + " = "+ String.valueOf(ayaNumber),null,null,)
    }
    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DataBaseHelper.DBNAME);
            String outFileName = DataBaseHelper.DBLOCATION + DataBaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

/**
 * a class to custom view for accessibility issues
 */
abstract class CustomTouchView extends FloatingActionButton {
    public CustomTouchView(Context context) {
        super(context);
    }

    @Override
    public abstract boolean onTouchEvent(MotionEvent event);

    @Override
    public abstract boolean performClick();
}
