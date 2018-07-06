package com.example.tefah.quran;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.CountDownTimer;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tefah.quran.Adapters.Asma2ElsewarAdapter;
import com.example.tefah.quran.data.DataBaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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

public class MainActivity extends AppCompatActivity implements Asma2ElsewarAdapter.ListItemClickListener {

    //-------------------------------------------------
    public static final int MY_PERMISSIONS_REQUEST_RECORDE_AUDIO = 100;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 200;
    private static final int NUM_LIST_ITEMS = 114;
    private static boolean RECODER_TIME_OK = false;
    public MediaRecorder recorder;
    //------------------------------------------------
    List<String> mSewarNames;
        MediaPlayer player;
    @BindView(R.id.voice_recorder)
    FloatingActionButton voiceRecorder;
    @BindView(R.id.test_player)
    FloatingActionButton testPlayer;
    private Asma2ElsewarAdapter mAdapter;
    private RecyclerView mSewarList;
    private String audioPath;
    private CountDownTimer timer;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    Button searchButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSewarNames = DataBaseHelper.suraNames();
        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mSewarList = findViewById(R.id.namesOfsewar);

    /*
     * A LinearLayoutManager is responsible for measuring and positioning item views within a
     * RecyclerView into a linear list. This means that it can produce either a horizontal or
     * vertical list depending on which parameter you pass in to the LinearLayoutManager
     * constructor. By default, if you don't specify an orientation, you get a vertical list.
     * In our case, we want a vertical list, so we don't need to pass in an orientation flag to
     * the LinearLayoutManager constructor.
     */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSewarList.setLayoutManager(layoutManager);
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mSewarList.setHasFixedSize(true);
        //Pass in this as the ListItemClickListener to the Asma2ElsewarAdapter constructor
        /*
         * The Asma2ElsewarAdapter is responsible for displaying each item in the list.
         */
        mAdapter = new Asma2ElsewarAdapter(NUM_LIST_ITEMS, this, mSewarNames);
        mSewarList.setAdapter(mAdapter);
        //-------------------------------------------------------------------

//        it has a problem with accessability i can search for it later
        voiceRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
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
    public void play() {
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

    public boolean grantPermission() {
        boolean audioGranted = false;
        boolean readGranted = false;
        boolean writeGranted = false;
        if (Build.VERSION.SDK_INT >= 23) {
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
            return audioGranted && writeGranted;
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

    //Override ListItemClickListener's onListItemClick method
    /**
     * This is where we receive our callback from
     * {}
     * <p>
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Context context = MainActivity.this;

        Intent startChildActivityIntent = new Intent(context, Main2Activity.class);
        startChildActivityIntent.putExtra(Intent.EXTRA_INDEX, clickedItemIndex);

        startActivity(startChildActivityIntent);
        //---------------------------------------------------------
        //(12) Show a Toast when an item is clicked, displaying that item number that was clicked

//        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
//        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
//
//        mToast.show();
    }

    public void onSearch(View view) {
        //get text from searchEditText when click button
        String AyaWrittenInSearchBox = searchEditText.getText().toString();
        Context context = MainActivity.this;
        Intent startChildActivityIntent = new Intent(context, Main2Activity.class);
        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, AyaWrittenInSearchBox);
        startActivity(startChildActivityIntent);
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