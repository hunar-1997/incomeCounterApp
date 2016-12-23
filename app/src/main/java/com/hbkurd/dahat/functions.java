package com.hbkurd.dahat;
import android.app.*;
import java.io.*;
import android.widget.*;
import android.content.*;
import android.text.*;

public class functions
{
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
	
	public void print(Context con, String s){
		Toast.makeText(con, s, Toast.LENGTH_LONG).show();
	}
}
