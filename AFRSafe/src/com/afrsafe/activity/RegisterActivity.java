package com.afrsafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afrsafe.R;
import com.afrsafe.db.SQLiteHandler;
import com.afrsafe.db.SessionManager;
import com.afrsafe.gallery.helper.Utils;

public class RegisterActivity extends Activity {
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
					Utils.ShowMensagem("Ja existe uma senha cadastrada",
							RegisterActivity.this);
					return;
				}

				String password = inputPassword.getText().toString().trim();

				if (!password.isEmpty()) {
					registerUser(password);
				} else {
					Utils.ShowMensagem("Favor informar uma senha!",
							RegisterActivity.this);
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
		db.addUser(password);

		Utils.ShowMensagem("Senha gravada com sucesso!", this);
		Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(i);
		finish();

	}

}
