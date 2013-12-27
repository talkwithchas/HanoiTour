package com.example.hanoivisit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnniActivity extends Activity implements AnimationListener {
	ImageView imageView1, imageView2;
	Animation animBounce;
	static Activity myActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anni);
		
		myActivity = this;
		
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		
		
		animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.activity_anni);

		
		animBounce.setAnimationListener(this);
		
		
		imageView1.setVisibility(View.VISIBLE);
		imageView1.startAnimation(animBounce);
		imageView2.setVisibility(View.VISIBLE);
		imageView2.startAnimation(animBounce);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == animBounce) {
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			//this.finish();
		}
		
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
	public static Activity getInstance(){
		   return   myActivity;
	}
	
}
