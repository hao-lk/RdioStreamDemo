package playradiostream.kimhao.com.dev.rdiostreamdemo;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

/**
 * Author by KimHao
 * Created by kimha on 20/07/2017.
 */

public class RadioService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String TAG = "RadioService";

    private MediaPlayer mPlayer;
    private RadioBinder mRadioBinder = new RadioBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        initRadioPlay();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRadioBinder;
    }


    private void initRadioPlay() {
        //set mPlayer properties
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //set listeners
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                Log.d(TAG, "onBufferingUpdate: ");
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        sendMessageToActivity(true);

    }

    public void playRadio(String url) {
        if (mPlayer.isPlaying() || mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        mPlayer = new MediaPlayer();
        //set data source
        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setOnPreparedListener(this);
        mPlayer.prepareAsync();

    }

    public class RadioBinder extends Binder {
        RadioService getService() {
            return RadioService.this;
        }
    }

    private void sendMessageToActivity(boolean isSuccess) {
        Intent intent = new Intent("SendData");
        intent.putExtra("data", isSuccess);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
