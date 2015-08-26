package whistle.receiver;

import java.util.Random;

import whistle.receiver.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends Activity implements OnClickListener {
	WakeLock mlock;
	public static final int DETECT_NONE = 0;
	public static final int DETECT_WHISTLE = 1;
	public static int selectedDetection = DETECT_NONE;

	// detection parameters
	private DetectorThread detectorThread;
	private RecorderThread recorderThread;
	private Thread th;
	public static int whistleValue = 0;
	TextView result;
	String[] st = new String[] { "Excellent", "Very Good", "Nice job",
			"Superb", "Awesome", "Amazing" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mlock = manager
				.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "abc");
		mlock.acquire();
		// Button more = (Button) findViewById(R.id.button1);
		Button rate = (Button) findViewById(R.id.button2);
		Button refresh = (Button) findViewById(R.id.refresh);
		result = (TextView) findViewById(R.id.result_text);
		result.setText("Meter");
		Typeface face = Typeface
				.createFromAsset(getAssets(), "fonts/Helve.otf");
		result.setTypeface(face);
		rate.setOnClickListener(this);
		// more.setOnClickListener(this);
		refresh.setOnClickListener(this);
		ImageView iv = (ImageView) findViewById(R.id.imageView2);
		clip = (ClipDrawable) iv.getDrawable();

		clip.setLevel(0);
		selectedDetection = DETECT_WHISTLE;
		if (recorderThread == null) {

			recorderThread = new RecorderThread();
			recorderThread.start();
			detectorThread = new DetectorThread(recorderThread, this);
			detectorThread.start();
			if (th == null) {
				th = new Thread(r);
				th.start();
			}

		}

	}

	private Runnable r = new Runnable() {

		@Override
		public void run() {
			try {
				while (recorderThread != null && detectorThread != null) {
					Log.d("main", "listening "
							+ DetectorThread.totalWhistlesDetected);
					nlevel = DetectorThread.totalWhistlesDetected * 500;

					// nlevel += 1000;
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							clip.setLevel(nlevel);

						}
					});

					if (nlevel >= 10000) {

						DetectorThread.totalWhistlesDetected = 0;
						Random r = new Random();
						int i = r.nextInt(st.length - 1);
						result.setText(st[i]);
						detectorThread.stopDetection();
						recorderThread.stopRecording();
						detectorThread = null;
						recorderThread = null;
						th = null;

					}

					Thread.sleep(100);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};
	int nlevel = 0;
	int mLevel = 10000;
	ClipDrawable clip;

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.button2:
			Intent rate = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ getPackageName()));    //here , change "uri & getpackage and put whole uri of your app -ronak
			startActivity(rate);
			break;
		// case R.id.button1:
		// Intent it = new Intent(TestActivity.this, MoreActivity.class);
		// startActivity(it);
		// break;
		case R.id.refresh:
			clip.setLevel(0);
			nlevel = 0;
			DetectorThread.totalWhistlesDetected = 0;
			recorderThread = new RecorderThread();
			recorderThread.start();
			detectorThread = new DetectorThread(recorderThread, this);
			detectorThread.start();
			th = new Thread(r);
			th.start();
			break;

		}

	}

	@Override
	protected void onPause() {
		if (mlock != null)
			mlock.release();
		if (detectorThread != null) {

			detectorThread.stopDetection();
			recorderThread.stopRecording();
			detectorThread = null;
			recorderThread = null;
			th = null;
		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mlock != null)
			mlock.acquire();
		super.onResume();
	}
}
