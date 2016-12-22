package com.hbkurd.dahat;

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
import android.widget.AdapterView.*;public class MainActivity extends Activity 
{
    
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
		
		String rawData=readFromFile(db);
	
		if( rawData!="error" )
			data = decode(rawData);
		else{
			data = decode("[["+getDayId()+"]]");
			rawData=data.toString();
			writeToFile(db,"[["+getDayId()+"]]");
		}
		
		contents = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,R.layout.list,R.id.holder,contents);
        listView = (ListView) findViewById(R.id.lists);
		data = new ArrayList<List<Integer>>();
		
		data = decode(rawData);
		
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
		
		ko=(TextView)findViewById(R.id.ko);
		
		listView.setAdapter(adapter);
		update(data.get(pointer));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				String s = (String) l.getItemAtPosition(position);
				print(s, 1000);
			}
		});
		
		save = (Button) findViewById(R.id.save);
		prise = (TextView) findViewById(R.id.prise);
		
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
							
							writeToFile(db, data.toString());
						}else{
							print("تکایە نرخێکی ڕاست بنووسە",1000);
						}
					}catch(Throwable e){
						print("تکایە تەنها ژمارە بنووسە",1000);
					}
				}else{
					print("نابێت نرخ بەتاڵ بێت",1000);
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
				Toast.makeText(this, "This is my app!!!", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.mainMenuExit:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void readData(View view)
	{
		String data = readFromFile(db);
		print("The data: "+data, 1000);
	}
	
	private void writeToFile(String fileName, String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(fileName, Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		}catch (Throwable t){
			print("تۆمار بەتاڵە", 1000);
		}
	}
	
	private String readFromFile(String fileName) {

		String ret = "";

		try {
			InputStream inputStream = this.openFileInput(fileName);

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		}catch (Throwable t){
			return "error";
		}

		return ret;
	}
	
	public int getDayId(){
		Calendar c = Calendar.getInstance();
		return Integer.parseInt(c.get(Calendar.YEAR)+""+(c.get(Calendar.MONTH)+1)+""+c.get(Calendar.DAY_OF_MONTH) );
	}
	
	public String parseDate(int date){
		int a = date%100;
		date /= 100;
		int b=date%100;
		date /= 100;
		return date+"/"+b+"/"+a;
	}
	
	public void print(String s, int dur){
		Toast.makeText(this,s,dur).show();
	}
	
	public void update(List<Integer> l){
		sum=0;
		for(int i=1;i<l.size();i++) sum+=l.get(i);
		
		ko.setText("تێکڕا: "+sum);
		adapter.notifyDataSetChanged();
		
		day=(TextView)findViewById(R.id.day);
		day.setText("تۆماری " + parseDate( data.get(pointer).get(0)) );
	}
	
	List<List<Integer>> decode(String s){
		List<List<Integer>> Days = new ArrayList<List<Integer>>();
		int state = 0;
		String token="";
		for(char c:s.toCharArray()){
			if(c!=',' && c!='[' && c!=']' && c!=' ') {
				token += c;
			}else{
				if(c=='['){
					if(state==0)
						state++;
					else if(state==1){
						Days.add(new ArrayList<Integer>());
						state++;
					}
				}else if(c==']'){
					if(state==2){
						if(!token.isEmpty())
							Days.get(Days.size()-1).add(Integer.parseInt(token));
						token="";
						state--;
					}else if(state==1){
						break;
					}
				}else if(c==','){
					if(state==2 && !token.isEmpty()){
						Days.get(Days.size()-1).add(Integer.parseInt(token));
						token="";
					}
				}
			}
		}
		return Days;
	}
}
