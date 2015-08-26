package whistle.receiver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LockActivity extends Activity {
	public static final int DETECT_NONE = 0;
	public static final int DETECT_WHISTLE = 1;
	public static int selectedDetection = DETECT_NONE;

	// detection parameters
	private DetectorThread detectorThread;
	private RecorderThread recorderThread;
	private Thread detectedTextThread;
	public static int whistleValue = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);
		if (detectedTextThread == null) {
			detectedTextThread = new Thread() {
				public void run() {
					try {
						while (recorderThread != null && detectorThread != null) {
							runOnUiThread(new Runnable() {
								public void run() {
									if (detectorThread != null) {
										int total = detectorThread
												.getTotalWhistlesDetected();
										Log.d("main", "total: " + total);
										if (total > 3) {
											answerCall();

										}

									}
								}

							});
							sleep(100);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						detectedTextThread = null;
					}
				}
			};
			detectedTextThread.start();
		}

	}

	private void answerCall() {
		// TODO Auto-generated method stub
	}

	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
		// unbindService(conn)
	}
}
