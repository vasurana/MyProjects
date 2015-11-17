/**
 * Author: Vasu Pundir
 * Email: rana.vasu01@gmail.com
 */
package login_and_registration;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.muneemji.R;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private ViewFlipper viewFlipper;
	private float lastX;
	private Button next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputEmail, InputMethodManager.SHOW_IMPLICIT);
        inputPassword = (EditText) findViewById(R.id.password);
       /* InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.showSoftInput(inputPassword, InputMethodManager.SHOW_IMPLICIT);*/
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		//next = (Button) findViewById(R.id.btnNext);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterFlipper.class);
                startActivity(i);
                //finish();
            }
        });
        
       /*
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Next screen comes in from right.
				viewFlipper.setInAnimation(inFromRightAnimation());
				// Current screen goes out from left.
				viewFlipper.setOutAnimation(outToLeftAnimation());
				viewFlipper.showNext();
			}

			private Animation outToLeftAnimation() {
				Animation outtoLeft = new TranslateAnimation(

				Animation.RELATIVE_TO_PARENT, 0.0f,
						Animation.RELATIVE_TO_PARENT, -1.0f,

						Animation.RELATIVE_TO_PARENT, 0.0f,
						Animation.RELATIVE_TO_PARENT, 0.0f

				);

				outtoLeft.setDuration(500);

				outtoLeft.setInterpolator(new AccelerateInterpolator());

				return outtoLeft;
			}

			private Animation inFromRightAnimation() {
				Animation inFromRight = new TranslateAnimation(

				Animation.RELATIVE_TO_PARENT, +1.0f,
						Animation.RELATIVE_TO_PARENT, 0.0f,
						Animation.RELATIVE_TO_PARENT, 0.0f,
						Animation.RELATIVE_TO_PARENT, 0.0f

				);

				inFromRight.setDuration(500);

				inFromRight.setInterpolator(new AccelerateInterpolator());

				return inFromRight;
			}

			private Animation inFromLeftAnimation() {

				Animation inFromLeft = new TranslateAnimation(

				Animation.RELATIVE_TO_PARENT, -1.0f,
						Animation.RELATIVE_TO_PARENT, 0.0f,

						Animation.RELATIVE_TO_PARENT, 0.0f,
						Animation.RELATIVE_TO_PARENT, 0.0f

				);

				inFromLeft.setDuration(500);

				inFromLeft.setInterpolator(new AccelerateInterpolator());

				return inFromLeft;

			}

			private Animation outToRightAnimation() {

				Animation outtoRight = new TranslateAnimation(

				Animation.RELATIVE_TO_PARENT, 0.0f,
						Animation.RELATIVE_TO_PARENT, +1.0f,

						Animation.RELATIVE_TO_PARENT, 0.0f,
						Animation.RELATIVE_TO_PARENT, 0.0f

				);

				outtoRight.setDuration(500);

				outtoRight.setInterpolator(new AccelerateInterpolator());

				return outtoRight;

			}
		});*/

    }
    
 // Using the following method, we will handle all screen swaps.
 	public boolean onTouchEvent(MotionEvent touchevent) {
 		switch (touchevent.getAction()) {

 		case MotionEvent.ACTION_DOWN:
 			lastX = touchevent.getX();
 			break;
 		case MotionEvent.ACTION_UP:
 			float currentX = touchevent.getX();

 			// Handling left to right screen swap.
 			if (lastX < currentX) {

 				// If there aren't any other children, just break.
 				if (viewFlipper.getDisplayedChild() == 0)
 					break;

 				// Next screen comes in from left.
 				viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
 				// Current screen goes out from right.
 				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

 				// Display next screen.
 				viewFlipper.showNext();
 			}

 			// Handling right to left screen swap.
 			if (lastX > currentX) {

 				// If there is a child (to the left), kust break.
 				if (viewFlipper.getDisplayedChild() == 1)
 					break;

 				// Next screen comes in from right.
 				viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
 				// Current screen goes out from left.
 				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

 				// Display previous screen.
 				viewFlipper.showPrevious();
 			}
 			break;
 		}
 		return false;
 	}

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
