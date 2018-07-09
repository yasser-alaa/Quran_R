package com.example.tefah.quran.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tefah.quran.R;
import com.example.tefah.quran.Utils;
import com.example.tefah.quran.Adapters.Asma2ElsewarAdapter;
import com.example.tefah.quran.data.DataBaseHelper;
import com.example.tefah.quran.network.FileUploadService;
import com.example.tefah.quran.network.ServiceGenerator;

import java.io.File;
import java.io.IOException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Asma2ElsewarAdapter.ListItemTouchListner,
        MediaPlayer.OnSeekCompleteListener{

    private Asma2ElsewarAdapter mAdapter;
    private RecyclerView mSewarList;
    List<String> mSewarNames ;
    private static final int NUM_LIST_ITEMS = 114;
    private DataBaseHelper mDBHelper;
    //------------------------------------------------

    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 100 ;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 200 ;
    private static boolean RECODER_TIME_OK = false;
    private static boolean IS_TOO_MUCH = false;
    private static boolean IS_GESTURE_READ = true;



    private String audioPath;
    public MediaRecorder recorder;
    MediaPlayer player;
    private CountDownTimer minTimer;
    private CountDownTimer maxTimer;
    private CountDownTimer readTimer;


@BindView(R.id.voice_recorder)
    FloatingActionButton voiceRecorder;
    Button searchButton;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        mDBHelper = new DataBaseHelper(this);
      Log.d("stop after query","msg");
        // check exists Database

        mSewarNames =  DataBaseHelper.suraNames();

        mSewarList= findViewById(R.id.namesOfsewar);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSewarList.setLayoutManager(layoutManager);

        mSewarList.setHasFixedSize(true);
        mAdapter = new Asma2ElsewarAdapter(NUM_LIST_ITEMS,this,mSewarNames);
        mSewarList.setAdapter(mAdapter);
//        mSewarList.setOnTouchListener(new TouchListener());

        voiceRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        actionDown();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (IS_TOO_MUCH)
                            break;
                        actionUP();
                        break;
                    default:
                }
                    return true;
            }

        });
    }

    private void actionUP() {

        if (RECODER_TIME_OK) {
            Utils.stopRecording(recorder);
            if (audioPath != null) {
                Toast.makeText(MainActivity.this, getString(R.string.uploading), Toast.LENGTH_SHORT)
                        .show();
                uploadFile();
            }
        } else {
            recordStopSound();
            Toast.makeText(MainActivity.this, "Hold to record voice",
                    Toast.LENGTH_SHORT).show();
            recorder.release();
            recorder = null;
        }
        minTimer.cancel();
        maxTimer.cancel();
        readTimer.cancel();
        RECODER_TIME_OK = false;
    }

    private void actionDown() {
        recorder = new MediaRecorder();
        if (grantPermission()) {
            tennn();
            record();
        }

    }

    /**
     * play sound to indicate something wrong with recording
     */
    private void recordStopSound() {
        if (player != null){
            onSeekComplete(player);
        }
        player = new MediaPlayer();
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd("recorde_stop.mp3");
            Utils.startPlaying(player, assetFileDescriptor, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * play sound to indicate recording
     */
    private void tennn() {
        if (player != null){
            onSeekComplete(player);
        }
        player = new MediaPlayer();
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd("recording.mp3");
            Utils.startPlaying(player, assetFileDescriptor, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void record() {
        IS_TOO_MUCH = false;
        IS_GESTURE_READ = true;
        audioPath = Utils.startRecording(recorder);
        minTimer =  new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                RECODER_TIME_OK = true;
            }
        }.start();
        maxTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                IS_TOO_MUCH = true;

                Toast.makeText(MainActivity.this, getString(R.string.time_exceeded), Toast.LENGTH_SHORT)
                        .show();
            }
        }.start();
        readTimer = new CountDownTimer(500, 1) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                IS_GESTURE_READ = false;
            }
        }.start();

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

       // finally, execute the request
        Call<ResponseBody> call = service.upload(body);

//        Call<ResponseBody> call = service.getHtml();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    String returned = response.body().string();
                    int suraNumber = Integer.valueOf(returned.split("-")[0]);
                    int ayaNumber = Integer.valueOf(returned.split("-")[1]);
                    Log.i("POST REQUEST", suraNumber + "-" + ayaNumber);

                    //todo should checkif the return didn't fail
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra(getString(R.string.aya_returned), ayaNumber);
                    intent.putExtra(getString(R.string.sura_returned), suraNumber);
                    startActivity(intent);
                    Log.i("POST REQUEST", response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("POST REQUEST", response.toString());
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
                            MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
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
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
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


    /**
     * This is where we receive our callback from
     * {}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
//    @Override
//    public void onListItemClick(int clickedItemIndex) {
//        Context context = MainActivity.this;
//        // String nameOfSura = messages.get(clickedItemIndex);
//
//        Intent startChildActivityIntent = new Intent(context, Main2Activity.class);
//        startChildActivityIntent.putExtra(Intent.EXTRA_INDEX, clickedItemIndex);
//
//        startActivity(startChildActivityIntent);
//
//    }

    public void onSearch(View view) {
//        //get text from searchEditText when click button
//        String AyaWrittenInSearchBox = searchEditText.getText().toString();
//        Context context = MainActivity.this;
//        Intent startChildActivityIntent = new Intent(context, Main2Activity.class);
//        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, AyaWrittenInSearchBox);
//        startActivity(startChildActivityIntent);
    }



    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Utils.stopPlaying(mediaPlayer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            onSeekComplete(player);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Context context = MainActivity.this;
                Intent startChildActivityIntent = new Intent(context, Main2Activity.class);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, query);
                startActivity(startChildActivityIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

//        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchViewAndroidActionBar.clearFocus();
//
//
//                Intent intent = new Intent(HeadActivity.this, SearchActivity.class);
//                intent.putExtra("some_key", query);
//                startActivity(intent);
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.blind_mode:
                break;
                default:

        }
        return true;
    }

    @Override
    public void onListTouch(MotionEvent event, int position) {
        Log.i("ON_TOUCH_LISTENER_BEGIN", String.valueOf(recorder));
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (recorder != null){
                    abort();
                    break;
                }
                Log.i("ON_TOUCH_LISTENER_DOWN", String.valueOf(recorder));
                actionDown();
                break;
            case MotionEvent.ACTION_UP:
                if (recorder == null){
                    // making new recorder to use the same function only :D
                    recorder = new MediaRecorder();
                    abort();
                    break;
                }
                Log.i("ON_TOUCH_LISTENER_UP", String.valueOf(recorder));
                if (IS_TOO_MUCH) {
                   actionUP();
                    break;
                }
                if (IS_GESTURE_READ){
                    abort();
                    gotoRead(position);
                    break;
                }
                actionUP();
                break;
                default:

        }
    }

    private void abort() {
        recorder.release();
        recorder = null;
        minTimer.cancel();
        maxTimer.cancel();
        readTimer.cancel();
        RECODER_TIME_OK = false;
    }

    private void gotoRead(int clickedItemIndex){
        Context context = MainActivity.this;
//        String nameOfSura = messages.get(clickedItemIndex);

        Intent startChildActivityIntent = new Intent(context, Main2Activity.class);
        startChildActivityIntent.putExtra(Intent.EXTRA_INDEX, clickedItemIndex);

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

