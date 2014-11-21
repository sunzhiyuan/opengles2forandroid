package com.example.firstopengglproject;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FirstOpenGLProjectActivity extends ActionBarActivity {
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);
//		setContentView(R.layout.activity_first_open_glproject);
		final ActivityManager activityManager = 
				(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo =
				activityManager.getDeviceConfigurationInfo();
		final boolean supportEs2 = configurationInfo.reqGlEsVersion>=0x20000;
		if(supportEs2){
			glSurfaceView.setEGLContextClientVersion(2);
			
			glSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
			rendererSet = true;
		} else {
			Toast.makeText(this, "This devices does not support OpenGl ES 2.0", Toast.LENGTH_LONG).show();
			return;
		}
		setContentView(glSurfaceView);
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_open_glproject, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(rendererSet)
			glSurfaceView.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(rendererSet)
			glSurfaceView.onResume();
	}
}
