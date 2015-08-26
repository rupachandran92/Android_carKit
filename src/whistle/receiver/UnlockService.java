package whistle.receiver;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

@SuppressWarnings("deprecation")
public class UnlockService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	KeyguardManager manager;
	KeyguardLock lock;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		manager = (KeyguardManager) getApplicationContext().getSystemService(
				Context.KEYGUARD_SERVICE);
		lock = manager.newKeyguardLock("lock");
		lock.disableKeyguard();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if (lock != null) {
			lock.reenableKeyguard();
			lock = null;
			Handler h = new Handler();
			h.postDelayed(new Runnable() {

				@Override
				public void run() {
					lock = manager.newKeyguardLock("abc");
					lock.disableKeyguard();
				}
			}, 300);
		}
		super.onDestroy();
	}

}
