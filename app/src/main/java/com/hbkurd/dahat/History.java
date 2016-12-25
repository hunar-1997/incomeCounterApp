package com.hbkurd.dahat;

import com.hbkurd.dahat.functions;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.text.*;
import android.widget.AdapterView.*;

public class History extends Activity
{
	functions fn = new functions();
	
	List<List<Integer>> data;
	List<String> content;
	ArrayAdapter adapter;
	
	Button showDayButton;
	ListView historyList;
	TextView dayText;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		String h = getIntent().getStringExtra("data");
		historyList = (ListView) findViewById(R.id.historyList);
		data = fn.decode(h);
		
		content = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, R.layout.list, R.id.holder, content);
		
		if(data.size()>0)
			for(int i=0;i<data.size();i++)
				content.add(fn.parseDate(data.get(i).get(0)));
		else
			content.add("تۆمار بەتاڵە");
		
		historyList.setAdapter(adapter);
		
		dayText=(TextView)findViewById(R.id.dayId);
		dayText.setText("ڕۆژێک هەڵبژێرە" );
		
		showDayButton = (Button) findViewById(R.id.showDays);
		
		showDayButton.setVisibility(Button.INVISIBLE);
		
		historyList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					if(data.get(position).size()>0){
						content.clear();
						adapter.notifyDataSetChanged();
						for(int i=1;i<data.get(position).size();i++){
							content.add(data.get(position).get(i).toString());
						}
						adapter.notifyDataSetChanged();
						
					}
				}
			});
	}
	
	public void back(View view){
		finish();
	}
}
