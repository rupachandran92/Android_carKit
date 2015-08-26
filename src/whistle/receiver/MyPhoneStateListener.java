package whistle.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener extends PhoneStateListener {
	Context c;
	boolean cut, flag = true;
	PhoneStateListener listener;
	SharedPreferences prefs;

	public MyPhoneStateListener(Context c) {
		// TODO Auto-generated constructor stub
		this.c = c;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);

		if (state == TelephonyManager.CALL_STATE_RINGING) {
			if (flag) {

				cut = true;
				flag = false;

				prefs = PreferenceManager.getDefaultSharedPreferences(c);
				if (!prefs.getBoolean("main_switch", false)) {
					return;
				}
				Log.d("main", "sservice start");
				c.startService(new Intent(c, MyService.class));
				Handler h = new Handler();
				h.postDelayed(new Runnable() {

					@Override
					public void run() {
						c.startActivity(new Intent(c, TranspaActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					}
				}, 2000);

			}
		} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
			Log.d("main", "Offhook");
		} else {
			if (cut) {
				Log.d("main", "sservice stop");
				c.stopService(new Intent(c, MyService.class));
				flag = true;
			}
		}

	}

}
