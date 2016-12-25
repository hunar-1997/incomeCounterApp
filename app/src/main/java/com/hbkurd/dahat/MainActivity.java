package com.hbkurd.dahat;

import com.hbkurd.dahat.functions;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.widget.ListView;
import android.view.*;
import java.io.File;
import java.io.OutputStreamWriter;
import android.content.Context;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import java.util.*;
import android.widget.AdapterView.*;
import android.content.*;
import android.widget.GridLayout.*;
import android.text.*;
public class MainActivity extends Activity 
{
	functions fn = new functions();
	Activity thisActivity = this;
	public final String db="database";
	
	List<String> contents;
	ArrayAdapter adapter;
	List<List<Integer>> data;
	
	TextView prise;
	TextView day;
	TextView ko;
	Button save;
	ListView listView;
	
	
	int sum=0;
	int today=-1;
	int pointer=-1;
	
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		String rawData = fn.readFromFile(MainActivity.this, db);
		
		if( rawData!="error" )
			data = fn.decode(rawData);
		else{
			data = fn.decode("[["+getDayId()+"]]");
			rawData=data.toString();
			fn.writeToFile(thisActivity, db,"[["+getDayId()+"]]");
		}
		
		contents = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,R.layout.list,R.id.holder,contents);
        data = new ArrayList<List<Integer>>();
		
		listView = (ListView) findViewById(R.id.lists);
		ko = (TextView)findViewById(R.id.ko);
		save = (Button) findViewById(R.id.save);
		prise = (TextView) findViewById(R.id.prise);
		
		prise.requestFocus();
		
		data = fn.decode(rawData);
		
		for(int i=0 ; i<data.size() ; i++){
			if(data.get(i).get(0)==getDayId()){
				today=i;
				pointer=today;
				break;
			}
		}
		
		if(today==-1){
			data.add(new ArrayList<Integer>());
			int i=data.size()-1;
			data.get(i).add(getDayId());
			today=i;
			pointer=today;
		}
		
		for(int i=1 ; i<data.get(pointer).size() ; i++){
			contents.add(""+data.get(pointer).get(i));
		}
		
		listView.setAdapter(adapter);
		update(data.get(pointer));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				String s = (String) l.getItemAtPosition(position);
				Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.change);

				dialog.setCancelable(true);
				dialog.setTitle("کردار: "+s);
				dialog.show();
			}
		});
		
		save.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				String p=prise.getText().toString();
				if(!p.isEmpty()){
					try{
						int j = Integer.parseInt(p);
						if(j%250==0){
							contents.add(""+j);
							data.get(pointer).add(j);
							update(data.get(pointer));
							
							fn.writeToFile(thisActivity, db, data.toString());
						}else{
							fn.print(thisActivity, "تکایە نرخێکی ڕاست بنووسە");
						}
					}catch(Throwable e){
						fn.print(thisActivity, "تکایە تەنها ژمارە بنووسە");
					}
				}else{
					fn.print(thisActivity, "نابێت نرخ بەتاڵ بێت");
				}
				prise.setText("");
			}
		});
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu , menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.mainMenuAbout:
				AlertDialog about = new AlertDialog.Builder(MainActivity.this).create();
				about.setTitle("دەربارەی ئەم بەرنامەیە");
				about.setCancelable(false);
				about.setMessage("ئەم بەرنامەیە دروستکراوە لەلایەن هونەر عومەر\nhbkurd@gmail.com\nhbkurd.weebly.com");
				about.setButton(AlertDialog.BUTTON_NEUTRAL, "سوپاس",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				about.show();
				return true;
			case R.id.mainMenuSend:
				Intent send = new Intent(Intent.ACTION_SEND);
				send.setType("text/plain");
				send.putExtra(Intent.EXTRA_STREAM, data.toString());
				startActivity(send);
				return true;
			case R.id.mainMenuReceive:
				Intent rec = new Intent(Intent.ACTION_GET_CONTENT);
				rec.setType("file/*");
				startActivityForResult(rec, 48);
				return true;
			case R.id.mainMenuExit:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void history(View view)
	{
		try{
		Intent historyPage = new Intent(thisActivity, History.class);
		historyPage.putExtra("data", data.toString());
		startActivity(historyPage);
		}catch(Throwable e){
			fn.print(thisActivity,e.toString());
		}
	}
	
	public int getDayId(){
		Calendar c = Calendar.getInstance();
		return Integer.parseInt(c.get(Calendar.YEAR)+""+(c.get(Calendar.MONTH)+1)+""+c.get(Calendar.DAY_OF_MONTH) );
	}
	
	public void update(List<Integer> l){
		sum=0;
		for(int i=1;i<l.size();i++) sum+=l.get(i);
		
		ko.setText("تێکڕا: "+sum+" دینار");
		adapter.notifyDataSetChanged();
		
		day=(TextView)findViewById(R.id.day);
		day.setText("تۆماری " + fn.parseDate( data.get(pointer).get(0)) );
	}
	
}
