package com.example.tefah.quran;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for helper methods and static values
 */

class Utilities {

    public static String startRecording(MediaRecorder recorder, Context context) {
        String savedAudioPath = null;

        File storageDir = mainStorageDir();
        boolean success = true;
        storageDir = new File(storageDir.getPath() + "/audio");
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",
                    Locale.getDefault()).format(new Date());
            // Record to the external cache directory for visibility
            String audioFileName = timeStamp + " audioNote.3gp";
            File imageFile = new File(storageDir, audioFileName);
            savedAudioPath = imageFile.getAbsolutePath();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(savedAudioPath);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            recorder.start();
        }
        return savedAudioPath;
    }

    public static void stopRecording(MediaRecorder recorder) {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private static File mainStorageDir(){
        File storageDir = new File(
                Environment.getExternalStorageDirectory()
                        + "/Quran");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return storageDir;
    }

    public static void startPlaying(MediaPlayer player, String audioFileName, MediaPlayer.OnSeekCompleteListener listener) {
        try {
            player.setDataSource(audioFileName);
            player.prepare();
            player.start();
            player.setOnSeekCompleteListener(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopPlaying(MediaPlayer player) {
        player.release();
        player = null;
    }
}
