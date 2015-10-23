package com.afrsafe.activity;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.afrsafe.R;
import com.afrsafe.app.AppController;
import com.afrsafe.db.SQLiteHandler;
import com.afrsafe.db.SessionManager;

public class MainActivity extends Activity {

	private ImageButton btnSair;
	private ImageButton btnReset;
	private ImageButton btnPuxar;
	private ImageButton btnGalery;

	private SQLiteHandler db;
	private SessionManager session;
	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnSair = (ImageButton) findViewById(R.id.BtnSair);
		btnReset = (ImageButton) findViewById(R.id.BtnReset);
		btnGalery = (ImageButton) findViewById(R.id.BtnGalery);
		btnPuxar = (ImageButton) findViewById(R.id.BtnPuxar);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		} else {
			File directory = new File(Environment.getExternalStorageDirectory()
					+ AppController.PHOTO_ALBUM);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();

		// Logout button click event
		btnSair.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});

		btnReset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				db.deleteUsers();
				logoutUser();
			}
		});

		btnGalery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		btnPuxar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// db.deleteUsers();
				// logoutUser();

				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
				startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

			}
		});
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data.getClipData() != null) {

			ClipData cdata = data.getClipData();

			for (int i = 0; i < cdata.getItemCount() - 1; i++) {
				Log.d(MainActivity.class.getSimpleName(), cdata.getItemAt(i)
						.toString());
			}

		}
	}
}
