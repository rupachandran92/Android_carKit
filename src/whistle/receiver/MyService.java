package whistle.receiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;

public class MyService extends Service {
	public static final int DETECT_NONE = 0;
	public static final int DETECT_WHISTLE = 1;
	public static int selectedDetection = DETECT_NONE;
	public static boolean flag;
	// detection parameters
	private DetectorThread detectorThread;
	private RecorderThread recorderThread;
	private Thread h;
	public static int whistleValue = 0;
	public static long count;

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("main", "Bind");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.d("main", "oncreate");
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// setHeadSetConnectEmulated();
		Log.d("main", "startcommand");
		selectedDetection = DETECT_WHISTLE;
		if (recorderThread == null) {

			recorderThread = new RecorderThread();
			recorderThread.start();
			detectorThread = new DetectorThread(recorderThread,
					getApplicationContext());
			detectorThread.start();
			if (h == null) {
				h = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							while (recorderThread != null
									&& detectorThread != null) {
								Log.d("main", "listening "
										+ DetectorThread.totalWhistlesDetected);
								if (DetectorThread.totalWhistlesDetected > 1) {
									Log.d("main", "Great than 	1");
									if (!flag) {
										Log.d("main", "flag ma gayu");
										detectorThread.stopDetection();
										recorderThread.stopRecording();
										detectorThread = null;
										recorderThread = null;

										flag = true;
										DetectorThread.totalWhistlesDetected = 0;
										acceptCall();
										stopSelf();

										// Intent homeIntent = new Intent(
										// Intent.ACTION_MAIN);
										// homeIntent
										// .addCategory(Intent.CATEGORY_HOME);
										// homeIntent
										// .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
										// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
										// startActivity(homeIntent);

										break;
									}
								}
								Thread.sleep(100);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							h = null;
						}
					}

				});
				h.start();
			}
		}
		return START_NOT_STICKY;
	}

	private void setHeadSetConnectEmulated() {
		Intent headSetUnPluggedintent = new Intent(Intent.ACTION_HEADSET_PLUG);
		headSetUnPluggedintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
		headSetUnPluggedintent.putExtra("state", 1); // 0 = unplugged 1 =
														// Headset with
														// microphone 2 =
														// Headset without
														// microphone
		headSetUnPluggedintent.putExtra("name", "Headset");
		// TODO: Should we require a permission?
		getApplicationContext().sendOrderedBroadcast(headSetUnPluggedintent,
				null);
	}

	@Override
	public void onDestroy() {
		Log.d("main", "Destroy");
		// android.os.Process.killProcess(android.os.Process.myPid());
		flag = false;
		if (detectorThread != null) {
			Log.d("main", "make all null");

			detectorThread.stopDetection();
			recorderThread.startRecording();

			recorderThread = null;
			detectorThread = null;
			h = null;
			DetectorThread.totalWhistlesDetected = 0;
		}

		super.onDestroy();
	}

	private void acceptCall() {
		// Intent mIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
		// mIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
		// mIntent.putExtra("state", 1); // 0 = unplugged 1 =
		// // Headset with
		// // microphone 2 =
		// // Headset without
		// // microphone
		// mIntent.putExtra("name", "Headset");
		// // TODO: Should we require a permission?
		// sendOrderedBroadcast(mIntent, null);
		Intent buttonUP = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonUP.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		getApplicationContext().sendOrderedBroadcast(buttonUP,
				"android.permission.CALL_PRIVILEGED");

		// stopSelf();
	}

}
