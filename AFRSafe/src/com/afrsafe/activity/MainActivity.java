package com.afrsafe.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.afrsafe.R;
import com.afrsafe.app.AppController;
import com.afrsafe.db.SQLiteHandler;
import com.afrsafe.db.SessionManager;
import com.afrsafe.gallery.helper.Utils;

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
				Intent intent = new Intent(MainActivity.this,
						GridViewActivity.class);
				startActivity(intent);
				finish();

			}
		});

		btnPuxar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

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

		if ((data != null) && (data.getClipData() != null)) {
			ClipData cdata = data.getClipData();
			for (int i = 0; i <= cdata.getItemCount() - 1; i++) {

				moveFile(getRealPathFromURI(cdata.getItemAt(i).getUri()));

			}
		}
		if (data != null) {
			Uri selectedImageUri = data.getData();
			moveFile(getRealPathFromURI(selectedImageUri));
		} else {
			Utils.ShowMensagem(
					"Nenhuma imagem copiada, favor escolher uma foto do dispositivo!",
					MainActivity.this);
		}
		Utils.reload();
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Audio.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void moveFile(String inputFile) {

		InputStream in = null;
		OutputStream out = null;

		try {

			// create output directory if it doesn't exist
			File dir = new File(Environment.getExternalStorageDirectory()
					+ AppController.PHOTO_ALBUM + "/");
			if (!dir.exists()) {
				dir.mkdirs();
			}

			in = new FileInputStream(inputFile);
			File file = new File(inputFile);
			String newname = file.getName().split("\\.")[0];

			out = new FileOutputStream(
					Environment.getExternalStorageDirectory()
							+ AppController.PHOTO_ALBUM + "/" + newname
							+ AppController.EXT_ARQUIVO);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file
			out.flush();
			out.close();
			out = null;

			// delete the original file
			File filedel = new File(inputFile);
			filedel.delete();

			MediaScannerConnection.scanFile(this, new String[] { inputFile },
					null, new MediaScannerConnection.OnScanCompletedListener() {

						@Override
						public void onScanCompleted(String path, Uri uri) {
							Log.i("ExternalStorage", "Scanned " + path + ":");
							Log.i("ExternalStorage", "-> uri=" + uri);
						}
					});

		}

		catch (FileNotFoundException fnfe1) {
			Log.e("tag", fnfe1.getMessage());
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}
}
