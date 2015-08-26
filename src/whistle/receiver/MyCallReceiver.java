package whistle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyCallReceiver extends BroadcastReceiver {

	SharedPreferences prefs;
	boolean cut, flag = true;
	static MyPhoneStateListener listener;

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d("main", "received");
		cut = false;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		TelephonyManager tmanager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (listener == null) {
			listener = new MyPhoneStateListener(context);

			if (prefs.getBoolean("main_switch", false)) {
				Log.d("main", "Switch true & listening state");
				tmanager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
			}

		}

		// Code to exteract incoming call number
		if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			// This code will execute when the phone has an incoming call

			// get the phone number
			String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			//Toast.makeText(context, "Call from:" + incomingNumber, Toast.LENGTH_LONG).show();
			Intent inten =new Intent(context,MainActivity.class);
			inten.putExtra("incoming_number",incomingNumber);
			inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(inten);
        }

	}
}
