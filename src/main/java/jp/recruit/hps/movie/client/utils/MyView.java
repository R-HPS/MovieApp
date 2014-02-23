package jp.recruit.hps.movie.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

@SuppressLint("ViewConstructor")
public class MyView extends View {
	int mMeter;
	Paint paint;
	double percent;

	public MyView(Context context, int meter, double percent) {
		super(context);
		mMeter = meter;
		paint = new Paint();
		this.percent = percent;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStrokeWidth(2);// 太さを2に
		if (percent >= 60) {
			paint.setColor(Color.parseColor("#dd768e"));// 赤セット
		}else if(percent>=30){
			paint.setColor(Color.parseColor("#daa000"));// 黄セット
		}else{
			paint.setColor(Color.parseColor("#00a29a"));// 青セット
		}
		paint.setAlpha(122);
		canvas.drawRect(0, 0, mMeter, 100, paint);// 四角形描画

	}

}
