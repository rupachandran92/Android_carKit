package whistle.receiver;

import whistle.receiver.R;
import android.app.Activity;
import android.os.Bundle;

public class TranspaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transpa);
		finish();
	}

}
