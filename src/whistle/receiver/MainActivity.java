package whistle.receiver;


import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnCheckedChangeListener,
        OnClickListener {
    SharedPreferences prefs;
    Editor edit;
    public static int whistleValue = 0;
    // Variables for shake detector
    private String phoneNumber = "4082039246";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Button voiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        edit = prefs.edit();
        ToggleButton tb1 = (ToggleButton) findViewById(R.id.switch1);
        tb1.setChecked(prefs.getBoolean("main_switch", false));
        tb1.setOnCheckedChangeListener(this);
        Button test = (Button) findViewById(R.id.button_test);
        test.setOnClickListener(this);
        Button settings = (Button) findViewById(R.id.button_setting);
        settings.setOnClickListener(this);
        Button more = (Button) findViewById(R.id.button_more);
        more.setOnClickListener(this);
        Button rate = (Button) findViewById(R.id.button_rate);
        rate.setOnClickListener(this);
        Button star = (Button) findViewById(R.id.button_star);
        star.setOnClickListener(this);

        voiceButton = (Button) findViewById(R.id.button_voice);
        voiceButton.setOnClickListener(this);


        //Code for detecting phone shake and send message.
        Intent intent = getIntent();
        if (intent != null) {
            phoneNumber = intent.getStringExtra("incoming_number");
        }
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                sendMessage();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        switch (arg0.getId()) {
            case R.id.switch1:
                edit.putBoolean("main_switch", arg1);
                edit.commit();
                break;
        }
    }

    public void sendMessage() {

        String message = "I am driving now.. will call you after some time";

        if (phoneNumber == null) phoneNumber = "4082039246";
        Toast.makeText(this, "Number " + phoneNumber, Toast.LENGTH_SHORT).show();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.button_test:
                startActivity(new Intent(getApplicationContext(),
                        TestActivity.class));
                break;
            case R.id.button_setting:
                startActivity(new Intent(getApplicationContext(),
                        SettingsActivity.class));
                break;
            case R.id.button_more:
                startActivity(new Intent(getApplicationContext(),
                        MoreActivity.class));
                break;
            case R.id.button_voice:
                startVoiceRecognitionActivity();
                break;
            case R.id.button_star:
            case R.id.button_rate:
                Intent rat = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id="
                                + getPackageName()));
                startActivity(rat);

                break;
        }
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice commands..");
        startActivityForResult(intent, 0);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);

    }
    //Code for Voice recognition

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this, "You talked " + matches.get(0), Toast.LENGTH_SHORT).show();
            String query;

            if (!matches.isEmpty()) {
                if (matches.get(0).contains("camera") || matches.get(0).contains("photo") || matches.get(0).contains("pic")) {
                    query = "camera";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(SearchManager.QUERY, query);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                    }

                } else if (matches.get(0).contains("music") || matches.get(0).contains("song") || matches.get(0).contains("play")) {
                    query = "music";
                    Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                    intent.putExtra(SearchManager.QUERY, query);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                    }
                } else if (matches.get(0).contains("near") || matches.get(0).contains("nearby") || matches.get(0).contains("near by")) {
                    query = matches.get(0);
                    String uri = "geo:0,0?q=" + query;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            }
        }

    }
}


