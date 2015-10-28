package com.example.demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo1.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	ImageView imageView;
	TextView tvpost, tvget;
	Button image, video, send, get;
	BufferedReader in = null;
	StringBuffer sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		image = (Button) findViewById(R.id.b_image);
		video = (Button) findViewById(R.id.b_video);
		send = (Button) findViewById(R.id.b_send);
		get = (Button) findViewById(R.id.b_get);
		tvpost = (TextView) findViewById(R.id.tv_httppost);
		tvget = (TextView) findViewById(R.id.tv_httpget);
		image.setOnClickListener(this);
		video.setOnClickListener(this);
		send.setOnClickListener(this);
		get.setOnClickListener(this);

	}

	class Newpost extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String result = "";
			try {

				HttpPost request = new HttpPost(
						"http://54.251.255.172:9999/signage/videoStatus/save");
				JSONObject obj = new JSONObject();
				HttpClient httpClient = new DefaultHttpClient();
				try {
					obj.putOpt("id", "Cam1");
					obj.putOpt("deviceId", "Cam1");
					obj.putOpt("videoOn", "false");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringEntity stringEntity = new StringEntity(obj.toString());
				stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				request.setEntity(stringEntity);

				HttpResponse response = httpClient.execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				sb = new StringBuffer("");
				String l = "";
				while ((l = in.readLine()) != null) {
					sb.append(l);
				}
				in.close();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			result = sb.toString();
			return result;

		}

		protected void onPostExecute(String string) {

			tvpost.setText(string.toString());

		}

		protected void onPreExecute() {
		}

		protected void onProgressUpdate(Void... values) {
		}

	}

	class Newget extends AsyncTask<Void, Void, String> {

		// String data = null;

		@Override
		protected String doInBackground(Void... params) {
			String result = "";

			try {

				HttpGet request = new HttpGet(
						"http://54.251.255.172:9999/signage/videoStatus/Cam1");

				HttpClient httpClient = new DefaultHttpClient();

				HttpResponse response = httpClient.execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				sb = new StringBuffer("");
				String l = "";
				while ((l = in.readLine()) != null) {
					sb.append(l);
				}
				in.close();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			result = sb.toString();
			return result;
		}

		protected void onPostExecute(String resuslt) {
			tvget.setText(resuslt.toString());

		}

		protected void onPreExecute() {
		}

		protected void onProgressUpdate(Void... values) {
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId())

		{

		case R.id.b_image:
			Intent i = new Intent("com.example.demo1.IMAGE");
			startActivity(i);

			break;

		case R.id.b_video:
			Intent p = new Intent("com.example.demo1.VIDEO");
			startActivity(p);

			break;

		case R.id.b_send:

			new Newpost().execute();

			break;


		case R.id.b_get:

			new Newget().execute();

			break;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case R.id.aboutUs:
			Intent i = new Intent("com.example.demo1.ABOUT");
			startActivity(i);

			break;

		case R.id.exit:
			finish();
			break;
		}
		return false;
	}

}
