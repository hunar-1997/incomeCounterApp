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
				break;
			}
		}
		
		if(today==-1){
			data.add(new ArrayList<Integer>());
			int i=data.size()-1;
			data.get(i).add(getDayId());
			today=i;
		}
		
		for(int i=1 ; i<data.get(today).size() ; i++){
			contents.add(""+data.get(today).get(i));
		}
		
		listView.setAdapter(adapter);
		update(data.get(today));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				String s = (String) l.getItemAtPosition(position);
				CustomDialogClass cdd = new CustomDialogClass(thisActivity, s, position);
				cdd.show();
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
							data.get(today).add(j);
							update(data.get(today));
							
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
				InfoDialog cdd = new InfoDialog(thisActivity);
				cdd.show();
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
		day.setText("تۆماری " + fn.parseDate( data.get(today).get(0)) );
	}
	
	
	class CustomDialogClass extends Dialog implements
	android.view.View.OnClickListener {

		public Activity act;
		public Dialog d;
		public Button change, delete, cancel;
		public String value;
		public int position;
		public TextView selectedPrise;

		public CustomDialogClass(Activity a, String val, int pos) {
			super(a);
			this.act = a;
			this.value = val;
			this.position=pos;
		}

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.change);
			change = (Button) findViewById(R.id.change);
			delete = (Button) findViewById(R.id.delete);
			cancel = (Button) findViewById(R.id.back);
			selectedPrise = (TextView) findViewById(R.id.selectedPrise);
			change.setOnClickListener(this);
			delete.setOnClickListener(this);
			cancel.setOnClickListener(this);
			selectedPrise.setText(this.value);
		}

		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.change:
					fn.print(MainActivity.this, "slaw");
					break;
				case R.id.delete:
					AlertDialog sure = new AlertDialog.Builder(MainActivity.this).create();
					sure.setTitle("ئاگاداری");
					sure.setMessage("دڵنیایت لە سڕینەوەی "+this.value+"؟");
					sure.setCancelable(true);
					sure.setButton(AlertDialog.BUTTON_POSITIVE, "بەڵێ",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								contents.remove(position);
								data.get(today).remove(position+1);
								update(data.get(today));
								fn.writeToFile(thisActivity, db, data.toString());
								fn.print(thisActivity, "سڕیمەوە");
								dialog.dismiss();
								dismiss();
							}
						});
					sure.setButton(AlertDialog.BUTTON_NEGATIVE, "نەخێر",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								fn.print(MainActivity.this, "نەم سڕیەوە");
								dialog.dismiss();
							}
						});
					sure.show();
					break;
				case R.id.back:
					dismiss();
					break;
			}
		}
	}
	
	class InfoDialog extends Dialog implements
	android.view.View.OnClickListener {

		public Activity act;
		public Dialog d;
		public Button thanks;

		public InfoDialog(Activity a) {
			super(a);
			this.act = a;
		}
		
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.about);
			thanks = (Button) findViewById(R.id.thanks);
			thanks.setOnClickListener(this);
		}

		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.thanks:
					dismiss();
					break;
			}
		}
	}
	
}
