package com.afrsafe.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.afrsafe.R;
import com.afrsafe.gallery.adpter.FullScreenImageAdapter;
import com.afrsafe.gallery.helper.Utils;

public class FullScreenViewActivity extends Activity {

	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		viewPager = (ViewPager) findViewById(R.id.pager);

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);

		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				Utils.getFilePaths());

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate our menu from the resources by using the menu inflater.
		getMenuInflater().inflate(R.menu.view, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = new Intent(FullScreenViewActivity.this,
				GridViewActivity.class);

		switch (item.getItemId()) {
		case R.id.menu_voltar:
			startActivity(intent);
			finish();
			return true;

		case R.id.menu_excluir:

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						File file = new File(adapter.getPathsPosition());
						file.delete();
						Utils.reload();
						Utils.getFilePaths();
						Intent intent = new Intent(FullScreenViewActivity.this,
								GridViewActivity.class);
						startActivity(intent);
						finish();
						break;

					case DialogInterface.BUTTON_NEGATIVE:

						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Deseja realmente excluir?")
					.setPositiveButton("Sim", dialogClickListener)
					.setNegativeButton("Não", dialogClickListener).show();

			return true;

		case R.id.menu_restaurar:

			startActivity(intent);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
