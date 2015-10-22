package afrsafe.activity;

import info.androidhive.loginandregistration.R;
import afrsafe.db.SQLiteHandler;
import afrsafe.db.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

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

			@Override
			public void onClick(View view) {
				String password = inputPassword.getText().toString().trim();

				// Check for empty data in the form
				if (!password.isEmpty()) {
					// login user
					checkLogin(password);
					hideDialog();
				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Favor informar a senha!", Toast.LENGTH_LONG)
							.show();
					hideDialog();
				}
			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				if (!db.getUserDetails().isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Já existe senha gravada!", Toast.LENGTH_LONG)
							.show();
					hideDialog();
					return;
				}

				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin(final String password) {

		pDialog.setMessage("Entrando ...");
		showDialog();

		if (db.getUserDetails().isEmpty()) {
			Toast.makeText(getApplicationContext(),
					"Não existe senha gravada!", Toast.LENGTH_LONG).show();
			hideDialog();
			return;
		}

		if (!db.getUserDetails().containsValue(password)) {
			Toast.makeText(getApplicationContext(), "Senha invalida!",
					Toast.LENGTH_LONG).show();
			hideDialog();
			return;
		}

		session.setLogin(true);

		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
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
