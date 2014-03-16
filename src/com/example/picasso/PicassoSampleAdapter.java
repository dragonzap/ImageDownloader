package com.example.picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

final class PicassoSampleAdapter extends BaseAdapter {
	public static int NOTIFICATION_ID = 666;

	enum Sample {
		GRID_VIEW("Image Grid View", SampleGridViewActivity.class) {
			@Override
			public void launch(Activity activity) {

			}
		};

		private final Class<? extends Activity> activityClass;
		private final String name;

		Sample(String name, Class<? extends Activity> activityClass) {
			this.activityClass = activityClass;
			this.name = name;
		}

		public void launch(Activity activity) {
			activity.startActivity(new Intent(activity, activityClass));
			activity.finish();
		}
	}

	private final LayoutInflater inflater;

	public PicassoSampleAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Sample.values().length;
	}

	@Override
	public Sample getItem(int position) {
		return Sample.values()[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return (TextView) convertView;
	}
}
