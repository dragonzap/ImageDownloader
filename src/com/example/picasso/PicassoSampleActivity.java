package com.example.picasso;

import com.example.picasso.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

abstract class PicassoSampleActivity extends FragmentActivity {
	private FrameLayout sampleContent;
	public EditText urlText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.picasso_sample_activity);
		sampleContent = (FrameLayout) findViewById(R.id.sample_content);

		urlText = (EditText) findViewById(R.id.editText1);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void setContentView(int layoutResID) {
		getLayoutInflater().inflate(layoutResID, sampleContent);
	}

	@Override
	public void setContentView(View view) {
		sampleContent.addView(view);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		sampleContent.addView(view, params);
	}
}
