package com.afrsafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afrsafe.R;
import com.afrsafe.db.SQLiteHandler;
import com.afrsafe.db.SessionManager;

public class RegisterActivity extends Activity {
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnRegister;
	private Button btnLinkToLogin;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		inputPassword = (EditText) findViewById(R.id.password);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

		// Progress dialog
		pDialog = new ProgressDialog(this);

		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// SQLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(RegisterActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (!db.getUserDetails().isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Ja existe uma senha cadastrada", Toast.LENGTH_LONG)
							.show();
					hideDialog();
					return;
				}

				String password = inputPassword.getText().toString().trim();

				if (!password.isEmpty()) {
					registerUser(password);
				} else {
					Toast.makeText(getApplicationContext(),
							"Favor informar uma senha!", Toast.LENGTH_LONG)
							.show();
					hideDialog();
				}
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	/**
	 * Function to store user in MySQL database will post params(tag, name,
	 * email, password) to register url
	 * */
	private void registerUser(final String password) {

		pDialog.setMessage("Gravando Senha!");
		showDialog();
		db.addUser(password);
		hideDialog();
		pDialog.setMessage("Senha gravada com sucesso!");
		hideDialog();
		Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(i);
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
