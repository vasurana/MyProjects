package net.muneemji.main;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.muneemji.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import login_and_registration.AppConfig;
import login_and_registration.AppController;
import login_and_registration.LoginActivity;
import login_and_registration.MainActivity;
import login_and_registration.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShopCategoriesList extends ListActivity {
	private static final String TAG = ShopCategoriesList.class.getSimpleName();
	String tag_string_req = "req_shopCategories";
	/** Items entered by the user is stored in this ArrayList variable */
	ArrayList list = new ArrayList();
	private String jsonResponse;
	/** Declaring an ArrayAdapter to set items to ListView */
	ArrayAdapter adapter;

	private ProgressDialog pDialog;
	private SessionManager session;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Setting a custom layout for the list activity */
		setContentView(R.layout.activity_shopcategories);

		/** Reference to the buttons of the layout main.xml */
		Button button_add = (Button) findViewById(R.id.btnAdd);
		Button button_done = (Button) findViewById(R.id.btnDone);
		Button btnDel = (Button) findViewById(R.id.btnDel);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(ShopCategoriesList.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}

		/** Defining the ArrayAdapter to set items to ListView */
		adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_multiple_choice, list);
		
		getCategoriesItems();

		/** Defining a click event listener for the button "Add" */
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edit = (EditText) findViewById(R.id.txtItem);
				list.add(edit.getText().toString());
				edit.setText("");
				adapter.notifyDataSetChanged();
			}
		};

		/** Defining a click event listener for the button "Delete" */
		OnClickListener listenerDel = new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** Getting the checked items from the listview */
				SparseBooleanArray checkedItemPositions = getListView()
						.getCheckedItemPositions();
				int itemCount = getListView().getCount();
				for (int i = itemCount - 1; i >= 0; i--) {
					if (checkedItemPositions.get(i)) {
						adapter.remove(list.get(i));
					}

				}

				checkedItemPositions.clear();
				adapter.notifyDataSetChanged();
			}
		};

		/** Setting the event listener for the add button */
		button_add.setOnClickListener(listener);

		/** Setting the event listener for the delete button */
		btnDel.setOnClickListener(listenerDel);

		/** Setting the event listener for the delete button */
		button_done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showDialog();

				JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
						"http://www.gtwebsolutions.net/kent/getoutlet.php",
						new Response.Listener<JSONArray>() {
							public void onResponse(JSONArray jsonArray) {
								hideDialog();
								
								// Successfully download json
								// So parse it and populate the listview
								for (int i = 0; i < jsonArray.length(); i++) {
									try {
										JSONObject categories = (JSONObject) jsonArray
												.get(i);
										JSONObject jsonObject = jsonArray
												.getJSONObject(i);
										list.add(jsonObject
												.getString("owner_name"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								adapter.notifyDataSetChanged();
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError volleyError) {
								Log.e("Error", "Unable to parse json array");
							}
						});
				
				
				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(jsonArrayRequest,
						tag_string_req);
			}
		});

		/** Setting the adapter to the ListView */
		setListAdapter(adapter);
	}

	private void getCategoriesItems() {
		// TODO Auto-generated method stub
		
		
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				"", null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());

						try {
							// Parsing json object response
							// response will be a json object
							
							JSONObject phone = response.getJSONObject("phone");
							
							String home = phone.getString("home");
							String mobile = phone.getString("mobile");
							list.add(home);
							list.add(mobile);

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
						hideDialog();
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hideDialog();
					}
				});
		
		
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_string_req);
		
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
