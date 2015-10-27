package com.afrsafe.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.afrsafe.R;
import com.afrsafe.app.AppController;
import com.afrsafe.gallery.adpter.GridViewImageAdapter;
import com.afrsafe.gallery.helper.Utils;

public class GridViewActivity extends Activity {

	private Utils utils;
	private ArrayList<String> imagePaths = new ArrayList<String>();
	private GridViewImageAdapter adapter;
	private GridView gridView;
	private int columnWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_view);

		gridView = (GridView) findViewById(R.id.grid_view);

		utils = new Utils(getApplicationContext());

		// Initilizing Grid View
		InitilizeGridLayout();

		// loading all image paths from SD card
		imagePaths = Utils.getFilePaths();

		// Gridview adapter
		adapter = new GridViewImageAdapter(GridViewActivity.this, imagePaths,
				columnWidth);

		// setting grid view adapter
		gridView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate our menu from the resources by using the menu inflater.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_voltar:
			// Here we might start a background refresh task
			Intent intent = new Intent(GridViewActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				AppController.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((utils.getScreenWidth() - ((AppController.NUM_OF_COLUMNS + 1) * padding)) / AppController.NUM_OF_COLUMNS);

		gridView.setNumColumns(AppController.NUM_OF_COLUMNS);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

}
