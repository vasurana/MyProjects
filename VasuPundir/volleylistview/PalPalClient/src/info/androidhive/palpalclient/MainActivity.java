package info.androidhive.palpalclient;

import info.androidhive.palpalclient.app.AppController;
import info.androidhive.palpalclient.app.Config;
import info.androidhive.palpalclient.helper.Product;
import info.androidhive.palpalclient.helper.ProductListAdapter;
import info.androidhive.palpalclient.helper.ProductListAdapter.ProductListAdapterListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class MainActivity extends Activity implements
		ProductListAdapterListener {
	private static final String TAG = MainActivity.class.getSimpleName();

	private ListView listView;
	private Button btnCheckout;

	// To store all the products
	private List<Product> productsList;

	// To store the products those are added to cart
	private List<PayPalItem> productsInCart = new ArrayList<PayPalItem>();

	private ProductListAdapter adapter;

	// Progress dialog
	private ProgressDialog pDialog;

	private static final int REQUEST_CODE_PAYMENT = 1;

	// PayPal configuration
	private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
			.environment(Config.PAYPAL_ENVIRONMENT).clientId(
					Config.PAYPAL_CLIENT_ID);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		btnCheckout = (Button) findViewById(R.id.btnCheckout);

		productsList = new ArrayList<Product>();
		adapter = new ProductListAdapter(this, productsList, this);

		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Starting PayPal service
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
		startService(intent);

		// Checkout button click listener
		btnCheckout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Check for empty cart
				if (productsInCart.size() > 0) {
					launchPayPalPayment();
				} else {
					Toast.makeText(getApplicationContext(), "Cart is empty! Please add few products to cart.",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// Fetching products from server
		fetchProducts();
	}

	/**
	 * Fetching the products from our server
	 * */
	private void fetchProducts() {
		// Showing progress dialog before making request

		pDialog.setMessage("Fetching products...");

		showpDialog();

		// Making json object request
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				Config.URL_PRODUCTS, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());

						try {
							JSONArray products = response
									.getJSONArray("products");

							// looping through all product nodes and storing
							// them in array list
							for (int i = 0; i < products.length(); i++) {

								JSONObject product = (JSONObject) products
										.get(i);

								String id = product.getString("id");
								String name = product.getString("name");
								String description = product
										.getString("description");
								String image = product.getString("image");
								BigDecimal price = new BigDecimal(product
										.getString("price"));
								String sku = product.getString("sku");

								Product p = new Product(id, name, description,
										image, price, sku);

								productsList.add(p);
							}

							// notifying adapter about data changes, so that the
							// list renders with new data
							adapter.notifyDataSetChanged();

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}

						// hiding the progress dialog
						hidepDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	/**
	 * Verifying the mobile payment on the server to avoid fraudulent payment
	 * */
	private void verifyPaymentOnServer(final String paymentId,
			final String payment_client) {
		// Showing progress dialog before making request
		pDialog.setMessage("Verifying payment...");
		showpDialog();

		StringRequest verifyReq = new StringRequest(Method.POST,
				Config.URL_VERIFY_PAYMENT, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "verify payment: " + response.toString());

						try {
							JSONObject res = new JSONObject(response);
							boolean error = res.getBoolean("error");
							String message = res.getString("message");

							// user error boolean flag to check for errors

							Toast.makeText(getApplicationContext(), message,
									Toast.LENGTH_SHORT).show();

							if (!error) {
								// empty the cart
								productsInCart.clear();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						// hiding the progress dialog
						hidepDialog();

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Verify Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hiding the progress dialog
						hidepDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {

				Map<String, String> params = new HashMap<String, String>();
				params.put("paymentId", paymentId);
				params.put("paymentClientJson", payment_client);

				return params;
			}
		};

		// Setting timeout to volley request as verification request takes sometime
		int socketTimeout = 60000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		verifyReq.setRetryPolicy(policy);

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(verifyReq);
	}

	/**
	 * Preparing final cart amount that needs to be sent to PayPal for payment
	 * */
	private PayPalPayment prepareFinalCart() {

		PayPalItem[] items = new PayPalItem[productsInCart.size()];
		items = productsInCart.toArray(items);

		// Total amount
		BigDecimal subtotal = PayPalItem.getItemTotal(items);

		// If you have shipping cost, add it here
		BigDecimal shipping = new BigDecimal("0.0");

		// If you have tax, add it here
		BigDecimal tax = new BigDecimal("0.0");

		PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(
				shipping, subtotal, tax);

		BigDecimal amount = subtotal.add(shipping).add(tax);

		PayPalPayment payment = new PayPalPayment(
				amount,
				Config.DEFAULT_CURRENCY,
				"Description about transaction. This will be displayed to the user.",
				Config.PAYMENT_INTENT);

		payment.items(items).paymentDetails(paymentDetails);

		// Custom field like invoice_number etc.,
		payment.custom("This is text that will be associated with the payment that the app can use.");

		return payment;
	}

	/**
	 * Launching PalPay payment activity to complete the payment
	 * */
	private void launchPayPalPayment() {

		PayPalPayment thingsToBuy = prepareFinalCart();

		Intent intent = new Intent(MainActivity.this, PaymentActivity.class);

		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);

		startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}

	/**
	 * Receiving the PalPay payment response
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.e(TAG, confirm.toJSONObject().toString(4));
						Log.e(TAG, confirm.getPayment().toJSONObject()
								.toString(4));

						String paymentId = confirm.toJSONObject()
								.getJSONObject("response").getString("id");

						String payment_client = confirm.getPayment()
								.toJSONObject().toString();

						Log.e(TAG, "paymentId: " + paymentId
								+ ", payment_json: " + payment_client);

						// Now verify the payment on the server side
						verifyPaymentOnServer(paymentId, payment_client);

					} catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ",
								e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.e(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.e(TAG,
						"An invalid Payment or PayPalConfiguration was submitted.");
			}
		}
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	@Override
	public void onAddToCartPressed(Product product) {

		PayPalItem item = new PayPalItem(product.getName(), 1,
				product.getPrice(), Config.DEFAULT_CURRENCY, product.getSku());

		productsInCart.add(item);

		Toast.makeText(getApplicationContext(),
				item.getName() + " added to cart!", Toast.LENGTH_SHORT).show();

	}

}
