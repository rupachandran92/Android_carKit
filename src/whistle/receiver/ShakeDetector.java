package whistle.receiver;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by shilp on 7/10/15.
 */
public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_STOP_TIME_MS = 500;
    long timeStamp, now;

    private OnShakeListener mListener;

    public interface OnShakeListener {
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        now = System.currentTimeMillis();

        if ((timeStamp + SHAKE_STOP_TIME_MS) > now)
            return;

        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gx = x / SensorManager.GRAVITY_EARTH;
            float gy = y / SensorManager.GRAVITY_EARTH;
            float gz = z / SensorManager.GRAVITY_EARTH;

            float gforce = (float) Math.sqrt(gx * gx + gy * gy + gz * gz);


            if (gforce > SHAKE_THRESHOLD_GRAVITY) {
                mListener.onShake();
                timeStamp = now;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
