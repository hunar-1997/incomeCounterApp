package com.hbkurd.dahat;
import android.app.*;
import java.io.*;
import android.widget.*;
import android.content.*;
import android.text.*;
import java.util.*;

public class functions
{
	public List<Integer> getList(List<List<Integer>> list, int index){
		List<Integer> r = new ArrayList<Integer>();
		for(int i=1;i<list.get(index).size();i++){
			r.add(list.get(index).get(i));
		}
		return r;
	}
	
	public String readFromFile(Activity act, String fileName) {

		String ret = "";

		try {
			InputStream inputStream = act.openFileInput(fileName);

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
			//print(t+"",1000);
			return "error";
		}

		return ret;
	}
	
	public void writeToFile(Activity act, String fileName, String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(act.openFileOutput(fileName, Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		}catch (Throwable t){
			print(act, "تۆمار بەتاڵە");
		}
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
	
	public void print(Context con, String s){
		Toast.makeText(con, s, Toast.LENGTH_LONG).show();
	}
	
	public String parseDate(int date){
		int a = date%100;
		date /= 100;
		int b=date%100;
		date /= 100;
		return date+"/"+b+"/"+a;
	}
}
