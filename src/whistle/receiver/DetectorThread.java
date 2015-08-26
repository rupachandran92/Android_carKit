/*
 * Copyright (C) 2012 Ronak Kosamia

*/

package whistle.receiver;

import java.util.LinkedList;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.util.Log;
import android.widget.Toast;

import com.musicg.api.WhistleApi;
import com.musicg.wave.WaveHeader;

public class DetectorThread extends Thread {

	private RecorderThread recorder;
	private WaveHeader waveHeader;
	private WhistleApi whistleApi;
	private volatile Thread _thread;

	private LinkedList<Boolean> whistleResultList = new LinkedList<Boolean>();
	private int numWhistles;
	public static int totalWhistlesDetected = 0;
	private int whistleCheckLength = 3;
	private int whistlePassScore = 3;

	public DetectorThread(RecorderThread recorder, Context context) {
		this.recorder = recorder;
		AudioRecord audioRecord = recorder.getAudioRecord();

		int bitsPerSample = 0;
		if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) {
			bitsPerSample = 16;
		} else if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_8BIT) {
			bitsPerSample = 8;
		}

		int channel = 0;
		// whistle detection only supports mono channel
		if (audioRecord.getChannelConfiguration() == AudioFormat.CHANNEL_CONFIGURATION_MONO) {
			channel = 1;
			Log.d("main", "Channel1");
		} else {
			channel = 1;
			Toast.makeText(
					context,
					"Your Device doesn't support whistle recording..\nor records in very high whistles",
					Toast.LENGTH_LONG).show();
		}

		waveHeader = new WaveHeader();
		waveHeader.setChannels(channel);
		waveHeader.setBitsPerSample(bitsPerSample);
		waveHeader.setSampleRate(audioRecord.getSampleRate());
		whistleApi = new WhistleApi(waveHeader);
	}

	private void initBuffer() {
		numWhistles = 0;
		whistleResultList.clear();

		// init the first frames
		for (int i = 0; i < whistleCheckLength; i++) {
			whistleResultList.add(false);
		}
		// end init the first frames
	}

	public void start() {
		_thread = new Thread(this);
		_thread.start();
	}

	public void stopDetection() {
		_thread = null;
	}

	public void run() {
		try {
			byte[] buffer;
			initBuffer();

			Thread thisThread = Thread.currentThread();
			while (_thread == thisThread) {
				// detect sound
				buffer = recorder.getFrameBytes();

				// audio analyst
				if (buffer != null) {
					// sound detected
					MainActivity.whistleValue = numWhistles;

					// whistle detection
					// System.out.println("*Whistle:");
					boolean isWhistle = whistleApi.isWhistle(buffer);
					if (whistleResultList.getFirst()) {
						numWhistles--;
					}

					whistleResultList.removeFirst();
					whistleResultList.add(isWhistle);

					if (isWhistle) {
						numWhistles++;
						// Log.d("main", "num whistle: " + numWhistles);
					}
					// System.out.println("num:" + numWhistles);

					if (numWhistles >= whistlePassScore) {
						// clear buffer
						initBuffer();
						totalWhistlesDetected++;

					}
					// end whistle detection
				} else {
					// no sound detected
					if (whistleResultList.getFirst()) {
						numWhistles--;
					}
					whistleResultList.removeFirst();
					whistleResultList.add(false);

					MainActivity.whistleValue = numWhistles;
					// Log.d("main", "num whistle1: " + numWhistles);
				}
				// end audio analyst
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTotalWhistlesDetected() {
		return totalWhistlesDetected;
	}
}