package com.afrsafe.app;

import java.util.Arrays;
import java.util.List;

import android.app.Application;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();
	// Number of columns of Grid View
	public static final int NUM_OF_COLUMNS = 2;

	// Gridview image padding
	public static final int GRID_PADDING = 8; // in dp

	// SD card image directory
	public static final String PHOTO_ALBUM = "/AFRSafe";

	public static final String EXT_ARQUIVO = ".AFRSafe";

	// supported file formats
	public static final List<String> FILE_EXTN = Arrays.asList("afrsafe");

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

}