package org.cloudfoundry.android.cfdroid.about;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

@ContentView(R.layout.about)
public class AboutActivity extends RoboSherlockActivity {

	@InjectView(R.id.about)
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		textView.setText(Html.fromHtml(getResources().getString(R.string.about_text)));
	}
}
