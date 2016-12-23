package com.hbkurd.dahat;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class History extends Activity
{
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
	}
	
	public void back(View view){
		finish();
	}
}
