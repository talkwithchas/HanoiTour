package com.example.hanoitour;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.hanoivisit.R;


import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.JavascriptInterface;


public class WebActivity extends Activity {

	private WebView mWebView;
	String name, pix, desc, wiki;
	TextToSpeech tts;
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
            case KeyEvent.KEYCODE_BACK:
                if(mWebView.canGoBack() == true){
                    mWebView.goBack();
                }else{
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		SoundPool sp = new SoundPool(5, AudioManager.STREAM_NOTIFICATION, 0);
		int iTmp = sp.load(getApplicationContext(), R.raw.beep, 1);
		sp.play(iTmp, 1, 1, 0, 0, 1);
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		pix = intent.getStringExtra("pix");
		desc = intent.getStringExtra("desc");
		wiki = intent.getStringExtra("wiki");

		mWebView = (WebView) findViewById(R.id.activity_main_webview);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		
		
		
		mWebView.addJavascriptInterface(new MyData(), "info");
		mWebView.loadUrl("file:///android_asset/www/index.html");
		
		final Activity MyActivity = this;
		mWebView.setWebViewClient(new WebViewClient());
		//tts
		//text to speech
		tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if(status != TextToSpeech.ERROR){
					tts.setLanguage(Locale.US);
				}
						
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_speak:
	        	tts.speak(desc, TextToSpeech.QUEUE_FLUSH, null);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(tts != null){
			tts.stop();
			tts.shutdown();
		}
	}
	public class MyData {
		@JavascriptInterface
		public String getData() throws JSONException {
			
			JSONObject json = new JSONObject();
			json.put("name", name);
			json.put("pix", pix);
			json.put("desc", desc);
			json.put("wiki", wiki);
			return (json.toString());
		}
	}

}
