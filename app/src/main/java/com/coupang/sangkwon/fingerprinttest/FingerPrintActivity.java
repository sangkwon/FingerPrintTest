package com.coupang.sangkwon.fingerprinttest;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class FingerPrintActivity extends ActionBarActivity {

	private static final String TAG = "FingerPrintTest";
	private WebView webView1, webView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finger_print);

		webView1 = (WebView) findViewById(R.id.webview1);
		webView2 = (WebView) findViewById(R.id.webview2);

		initWebView(webView1, (TextView) findViewById(R.id.textview1));
		initWebView(webView2, (TextView) findViewById(R.id.textview2));

		findViewById(R.id.compareBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text1 = ((TextView) findViewById(R.id.textview1)).getText().toString();
				String text2 = ((TextView) findViewById(R.id.textview2)).getText().toString();

				Log.d(TAG, "onClick() text1=" + text1);
				Log.d(TAG, "onClick() text2=" + text2);

				String msg = text1.equals(text2) ? "Equals!!! :D" : "Not Eq :(";

				Toast.makeText(FingerPrintActivity.this, msg, Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setData(Uri.parse("coupang://detail"));
				intent.putExtra("referrer", "coupangSrl=1234");

				Log.d(TAG, intent.toUri(Intent.URI_INTENT_SCHEME).toString());

				//startActivity(intent);

			}
		});
	}

	private void initWebView(WebView webView, TextView textView) {
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new MyWebJSInterface(textView), "TextOut");
		webView.loadUrl("http://192.168.210.23:3000/");
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (Build.VERSION.SDK_INT >= 11) {
			webView1.onResume();
			webView2.onResume();
		}
	}

	public class MyWebJSInterface {
		TextView textView;

		public MyWebJSInterface(TextView textView) {
			this.textView = textView;
		}

		@JavascriptInterface
		public void setText(String str) {
			Log.i(TAG, "setText() " + str);

			final String msg = str;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					textView.setText(msg);
				}
			});
		}
	}

}
