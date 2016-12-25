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
	
	int state=0;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		String h = getIntent().getStringExtra("data");
		historyList = (ListView) findViewById(R.id.historyList);
		data = fn.decode(h);
		
		content = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, R.layout.list, R.id.holder, content);
		historyList.setAdapter(adapter);
		
		dayText=(TextView)findViewById(R.id.dayId);
		
		showDays();
		
		showDayButton = (Button) findViewById(R.id.showDays);
		showDayButton.setVisibility(Button.INVISIBLE);
		showDayButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				showDays();
				showDayButton.setVisibility(Button.INVISIBLE);
			}
		});
		
		historyList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					if(data.get(position).size()>0){
						if(state==0){
							showSells(position);			
							showDayButton.setVisibility(Button.VISIBLE);
						}
					}
				}
			});
	}
	
	public void showDays(){
		state=0;
		content.clear();
		adapter.notifyDataSetChanged();
		
		for(int i=0;i<data.size();i++){
			int sum=0;
			for(int j=1;j<data.get(i).size();j++)
				sum += data.get(i).get(j);
			content.add(fn.parseDate(data.get(i).get(0))+"  "+sum+" دینار");
		}
			
		adapter.notifyDataSetChanged();
		dayText.setText("ڕۆژێک هەڵبژێرە");
	}
	
	public void showSells(int position){
		state=1;
		content.clear();
		adapter.notifyDataSetChanged();
		for(int i=1;i<data.get(position).size();i++)
			content.add(data.get(position).get(i).toString()+" دینار");
		adapter.notifyDataSetChanged();
		dayText.setText("تۆماری "+fn.parseDate(data.get(position).get(0)));
	}
	
	public void back(View view){
		finish();
	}
}
