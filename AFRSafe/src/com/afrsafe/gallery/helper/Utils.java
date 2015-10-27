package com.afrsafe.gallery.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.WindowManager;

import com.afrsafe.R;
import com.afrsafe.app.AppController;

public class Utils {

	private final Context _context;
	private static ArrayList<String> paths = new ArrayList<String>();

	// constructor
	public Utils(Context context) {
		this._context = context;
	}

	public static void reload() {
		paths.clear();
		getFilePaths();
	}

	public static ArrayList<String> getFilePaths() {

		if (paths.isEmpty()) {

			File directory = new File(Environment.getExternalStorageDirectory()
					+ AppController.PHOTO_ALBUM);

			// check for directory
			if (directory.isDirectory()) {
				// getting list of file paths
				File[] listFiles = directory.listFiles();

				// Check for count
				if (listFiles.length > 0) {

					// loop through all files
					for (int i = 0; i < listFiles.length; i++) {

						// get file path
						String filePath = listFiles[i].getAbsolutePath();

						// check for supported file extension
						if (IsSupportedFile(filePath)) {
							// Add image path to array list
							paths.add(filePath);
						}
					}
				}
			}
		}
		return paths;
	}

	/*
	 * Check supported file extensions
	 * 
	 * @returns boolean
	 */
	private static boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (AppController.FILE_EXTN.contains(ext.toLowerCase(Locale
				.getDefault())))
			return true;
		else
			return false;

	}

	/*
	 * getting screen width
	 */
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) _context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}

	public static void ShowMensagem(String aMSg, Activity act) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				new ContextThemeWrapper(act, android.R.style.Theme_Dialog));
		dlgAlert.setMessage(aMSg);
		dlgAlert.setTitle(R.string.app_name);
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

}
