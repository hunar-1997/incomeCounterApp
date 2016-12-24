package com.hbkurd.dahat;

import com.hbkurd.dahat.functions;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.text.*;

public class History extends Activity
{
	functions fn = new functions();
	
	ListAdapter adapter;
	List<List<Integer>> data;
	
	Button showDayButton;
	Button right,left;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		String h = getIntent().getStringExtra("data");
		ListView list = (ListView) findViewById(R.id.historyList);
		data = fn.decode(h);

		adapter = new ArrayAdapter<Integer>(this, R.layout.list, R.id.holder, fn.getList(data,0));
		list.setAdapter(adapter);
		
		TextView day=(TextView)findViewById(R.id.dayId);
		day.setText("تۆماری " + fn.parseDate( data.get(0).get(0) ) );
		
		showDayButton = (Button) findViewById(R.id.showDays);
		right = (Button) findViewById(R.id.right);
		left = (Button) findViewById(R.id.left);
		
		showDayButton.setVisibility(Button.INVISIBLE);
		right.setVisibility(Button.INVISIBLE);
		left.setVisibility(Button.INVISIBLE);
	}
	
	public void back(View view){
		finish();
	}
}
