package com.example.picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.squareup.picasso.Picasso;
import static android.widget.ImageView.ScaleType.CENTER_CROP;

final class SampleGridViewAdapter extends BaseAdapter {
	private final Context context;

	public SampleGridViewAdapter(Context context) {
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SquaredImageView view = (SquaredImageView) convertView;
		if (view == null) {
			view = new SquaredImageView(context);
			view.setScaleType(CENTER_CROP);
		}

		// Get the image URL for the current position.
		String url = getItem(position);

		// Trigger the download of the URL asynchronously into the image view.
		Picasso.with(context) //
				.load(url) //
				.placeholder(R.drawable.placeholder) //
				.error(R.drawable.error) //
				.fit() //
				.into(view);

		return view;
	}

	@Override
	public int getCount() {
		return SampleGridViewActivity.urls.size();
	}

	@Override
	public String getItem(int position) {
		return SampleGridViewActivity.urls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
