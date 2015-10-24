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

public class LoginActivity extends Activity {
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
		session.setLogin(false);

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

				} else {
					// Prompt user to enter credentials
					Utils.ShowMensagem("Favor informar a senha!",
							LoginActivity.this);

				}
			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				if (!db.getUserDetails().isEmpty()) {
					Utils.ShowMensagem("Já existe senha gravada!",
							LoginActivity.this);
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

		if (db.getUserDetails().isEmpty()) {
			Utils.ShowMensagem("Não existe senha gravada!", this);
			return;
		}

		if (!db.getUserDetails().containsValue(password)) {
			Utils.ShowMensagem("Senha invalida!", this);

			return;
		}

		session.setLogin(true);

		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
