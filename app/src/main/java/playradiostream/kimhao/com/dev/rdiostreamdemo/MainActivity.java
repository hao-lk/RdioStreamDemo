package playradiostream.kimhao.com.dev.rdiostreamdemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mChannelLink;
    private LinearLayout linearLayout;
    private TextView tvNameChanel;
    private RadioService mRadioService;
    private Intent mPlayIntent;
    public MediaPlayer mediaPlayer;

    public static final String CHANEL_VOV_1 = "http://118.179.219.244:8000/;audio/mpeg";
    public static final String CHANEL_VOV_3 = "http://stream2.mobiradio.vn/vovradio/vov3backup.stream/chunklist.m3u8";
    public static final String CHANEL_LAM_DONG = "http://stream2.mobiradio.vn/radiotv/lamdong/chunklist.m3u8";
    //    public static final String CHANEL_TUYEN_QUANG = "http://stream2.mobiradio.vn/radiotv/tuyenquang/chunklist.m3u8";
    public static final String CHANEL_TUYEN_QUANG = "http://117.6.129.102:1901/live/vovgthcm.sdp/playlist.m3u8";
    public static final String CHANEL_MUSIC = "http://118.69.80.81:8000/http://118.69.80.81:8000?play.m3u8";
//    public static final String CHANEL_MUSIC = "http://117.6.129.102:1901/live/vovgthcm.sdp/playlist.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("SendData"));
        initView();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mRadioService = ((RadioService.RadioBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.ll_radio_wave);
        linearLayout.setVisibility(View.VISIBLE);
        Button btn1 = (Button) findViewById(R.id.btn_1);
        btn1.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btn_2);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.btn_3);
        btn3.setOnClickListener(this);
        Button btn4 = (Button) findViewById(R.id.btn_4);
        btn4.setOnClickListener(this);
        Button btn5 = (Button) findViewById(R.id.btn_5);
        btn5.setOnClickListener(this);
        tvNameChanel = (TextView) findViewById(R.id.tv_name_radio);
    }

    public void startRadio() {
        Log.d(TAG, "startRadio: ");
        mRadioService.playRadio(mChannelLink);
    }

    private void updateUI() {
        Log.d(TAG, "updateUI: ");
        String nameChanel = "";
        switch (mChannelLink) {
            case CHANEL_LAM_DONG:
                nameChanel = "FM - LAM DONG";
                break;
            case CHANEL_TUYEN_QUANG:
                nameChanel = "FM - TUYEN QUANG";
                break;
            case CHANEL_VOV_1:
                nameChanel = "FM - VOV 1";
                break;
            case CHANEL_VOV_3:
                nameChanel = "FM - VOV 3";
                break;
            case CHANEL_MUSIC:
                nameChanel = "FM - MUSIC";
                break;
        }
        tvNameChanel.setText(nameChanel);
        linearLayout.setVisibility(View.VISIBLE);
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        linearLayout.setVisibility(View.INVISIBLE);
        switch (view.getId()) {
            case R.id.btn_1:
                mChannelLink = CHANEL_VOV_1;
                startRadio();
                break;
            case R.id.btn_2:
                mChannelLink = CHANEL_VOV_3;
                startRadio();
                break;
            case R.id.btn_3:
                mChannelLink = CHANEL_LAM_DONG;
                startRadio();
                break;
            case R.id.btn_4:
                mChannelLink = CHANEL_TUYEN_QUANG;
                startRadio();
                break;
            case R.id.btn_5:
                mChannelLink = CHANEL_MUSIC;
                startRadio();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, RadioService.class);
            bindService(mPlayIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra("data", false);
            if (success) {
                updateUI();
            }
        }
    };
}
