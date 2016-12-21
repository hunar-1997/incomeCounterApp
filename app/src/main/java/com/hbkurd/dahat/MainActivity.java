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


import java.util.*;
import android.widget.AdapterView.*;public class MainActivity extends Activity 
{
    
	public final String days="days";
	
	ArrayList<String> contents;
	ArrayAdapter adapter;
	TextView prise;
	TextView day;
	TextView ko;
	Button b;
	String prises="S,";
	int sum=0;
	
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		day=(TextView)findViewById(R.id.day);
		day.setText("تۆماری "+getDayId());
		
		String r=readFromFile(days);
		if(r!="error"){
			prises=r;
		}
		
		contents = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,R.layout.list,R.id.holder,contents);
        ListView listView = (ListView) findViewById(R.id.lists);
		
		String parsed="";
		for(int i=0;i<prises.length()-1;i++){
			parsed+=prises.charAt(i);
			if(prises.charAt(i+1)==','){
				if(prises.charAt(i)=='S'){
					i++;
					parsed="";
				}else{
					contents.add(parsed);
					i++;
					parsed="";
				}
			}
		}
		
		ko=(TextView)findViewById(R.id.ko);
		
		listView.setAdapter(adapter);
		update(contents);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				String s = (String) l.getItemAtPosition(position);
				print(s, 1000);
			}
		});
		
		b = (Button) findViewById(R.id.save);
		prise = (TextView) findViewById(R.id.prise);
		
		b.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				String p=prise.getText().toString();
				if(!p.isEmpty()){
					try{
						int j = Integer.parseInt(p);
						if(j%250==0){
							contents.add(""+j);
							update(contents);
							
							prises+=p+",";
							writeToFile(days, prises);
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
		
		Button del = (Button) findViewById(R.id.srinawa);
		del.setOnClickListener(new View.OnClickListener(){
				public void onClick(View view){
					writeToFile(days, "S,");
					prises="S,";
					contents.clear();
					update(contents);
				}
			});
		
    }
	
	public void readData(View view)
	{
		String data = readFromFile(days);
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
			print("هەڵە ڕویدا: "+t, 1000);
			return "error";
		}

		return ret;
	}
	
	public String getDayId(){
		Calendar c = Calendar.getInstance(); 
		return c.get(Calendar.YEAR)+""+c.get(Calendar.DAY_OF_YEAR);
	}
	
	public void print(String s, int dur){
		Toast.makeText(this,s,dur).show();
	}
	
	public void update(ArrayList l){
		sum=0;
		for(String s:l)
			sum+=Integer.parseInt(s);
		ko.setText("تێکڕا: "+sum);
		adapter.notifyDataSetChanged();
	}
}
