package com.Ranjith.testapi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class MainActivity extends ActionBarActivity {
	
	Button show;
	ListView lv;
	ProgressDialog pdilog;
	ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();
	
    private static final String TAG_NAME = "name";
    private static final String TAG_ROLE = "role";
    private static final String TAG_ID = "id";
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        show = (Button) findViewById(R.id.show);              
       
        pdilog = new ProgressDialog(MainActivity.this);
        pdilog.setMessage("Getting Data......");
        newItemlist = new ArrayList<HashMap<String, String>>();
        
        show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new JSONParse().execute(); 
				
			}
		});
        
                
    }
    
    private class JSONParse extends AsyncTask<String, Void, Void>{
    	protected void onPreExecute() {
            super.onPreExecute();
           pdilog.show();
        }
    	protected Void doInBackground(String... args) {
    		try {
    			InputStream is =null;
    			String Jsonstring ="";
    			
    			Log.i("................", "Hello.............");
    			DefaultHttpClient httpClient = new DefaultHttpClient();
    			HttpGet httpPost = new HttpGet("http://spritle-rhomobile-2.herokuapp.com/developers.json");
    			HttpResponse httpResponse = httpClient.execute(httpPost);
    			HttpEntity httpEntity = httpResponse.getEntity();
    			is = httpEntity.getContent();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                }
                is.close();
    			Jsonstring = sb.toString();
    			Log.i("..............", Jsonstring);
    			JSONArray json = new JSONArray(Jsonstring);
    			for(int i=0;i<json.length();i++)
    			{
    				JSONObject c = json.getJSONObject(i);
    				String name = c.getString(TAG_NAME);
    				String role = c.getString(TAG_ROLE);
    				String id = c.getString(TAG_ID);
    				Log.i("........", name);
    				HashMap<String, String> map = new HashMap<String, String>();
    				map.put(TAG_NAME,name);
    				map.put(TAG_ROLE, role);
    				map.put(TAG_ID, id);
    				newItemlist.add(map);
    			}
    			
				
			} catch (Exception e) {
				
			}
    	
    	return null;
    	}
    	
    	 protected void onPostExecute(Void result) {
    		 super.onPostExecute(result);
             pdilog.dismiss();
             lv = (ListView) findViewById(R.id.show_items);
             ListAdapter adapter = new SimpleAdapter(MainActivity.this, newItemlist, R.layout.row, new String[]{TAG_NAME,TAG_ROLE,TAG_ID}, 
            		 new int[]{R.id.name,R.id.role,R.id.id});
             lv.setAdapter(adapter);
    	 }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
