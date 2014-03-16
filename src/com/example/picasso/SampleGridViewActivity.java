package com.example.picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SampleGridViewActivity extends PicassoSampleActivity {
	public static String lastURL = "";
	public static final List<String> urls = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_gridview_activity);

		GridView gv = (GridView) findViewById(R.id.grid_view);
		gv.setAdapter(new SampleGridViewAdapter(this));

		urlText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event == null || !event.isShiftPressed()) {
					starDownload();
					return true;
				}
				return false;
			}
		});
	}

	public void showMessage(String text) {
		Toast toast = Toast.makeText(getApplicationContext(), text,
				Toast.LENGTH_SHORT);
		toast.show();
	}

	public void starDownload() {
		String stringUrl = urlText.getText().toString();
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadWebpageTask().execute(stringUrl);
		} else {
			showMessage(getString(R.string.no_connection));
		}
	}

	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			try {
				return downloadUrl(urlFixer(urls[0]));
			} catch (IOException e) {
				return getString(R.string.wrong_url); // Unable to retrieve web
														// page. URL may be
														// invalid
			}
		}

		// Make valid URL string
		private String urlFixer(String inURL) {
			String url = "";

			if (inURL.indexOf("://") == -1) // Check http://
				url = "http://";

			url += inURL.replaceFirst("www.", ""); // Remove www.
			return url;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			showMessage(result);

			GridView gv = (GridView) findViewById(R.id.grid_view);
			gv.setAdapter(new SampleGridViewAdapter(getApplicationContext()));

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(urlText.getWindowToken(), 0);
		}

		private String downloadUrl(String myurl) throws IOException {
			if (lastURL.equals( myurl)) // Reload protect
				return "";

			String result = getString(R.string.page_not_found);
			urls.clear(); // Clear list
			InputStream is = null;
			try {
				URL url = new URL(myurl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				if (conn.getResponseCode() == 200) { // Connection is correct
					is = conn.getInputStream();
					lastURL = myurl;
					result = htmlReader(myurl, readIt(is));
				}
				conn.disconnect();
			} finally {
				if (is != null) {
					is.close();
				}
			}
			return result;
		}

		// Reads an InputStream and converts it to a String.
		public String readIt(InputStream stream) throws IOException,
				UnsupportedEncodingException {
			StringBuilder inputStringBuilder = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(stream, "UTF-8"));
			String line = bufferedReader.readLine();
			while (line != null) {
				inputStringBuilder.append(line);
				inputStringBuilder.append('\n');
				line = bufferedReader.readLine();
			}
			return inputStringBuilder.toString();
		}

		private String htmlReader(String baseURL, String html) {
			int count = 0;

			// Get Base URL
			int start = html.indexOf("<base");
			if (start != -1) { // If has base tag
				start = html.indexOf("href=", start) + 6;
				html = html.substring(start);
				baseURL = html.substring(0, html.indexOf('"'));
			} else {
				start = baseURL.lastIndexOf('.');
				if (start != -1) { // If has dot
					start = baseURL.indexOf('/', start); // and this not file
															// extension
					if (start != -1)
						baseURL = baseURL.substring(0, start);
				}
			}

			start = html.indexOf("<img");
			while (start != -1) {
				html = html.substring(start + 5);
				start = html.indexOf("src=");
				html = html.substring(start + 5);

				String url = html.substring(0, html.indexOf('"'));

				if (url.indexOf("http://") < 0) // relative link
				{
					if (url.charAt(0) != '/')
						url = "/" + url;

					url = baseURL + url;
				}

				String ext = url.substring(url.length() - 3);
				if (ext.equals("png") || ext.equals("jpg") || ext.equals("gif")
						|| ext.equals("peg"))
					urls.add(url);

				count++;
				start = html.indexOf("<img");
			}

			if (count == 0)
				return getString(R.string.no_image);

			return getString(R.string.image_count) + count;
		}
	}
}
