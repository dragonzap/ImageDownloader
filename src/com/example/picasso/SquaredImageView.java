package com.example.picasso;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/** An image view which always remains square with respect to its width. */
final class SquaredImageView extends ImageView {
	public SquaredImageView(Context context) {
		super(context);
	}

	public SquaredImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquaredImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
	}
}
