package com.fabmo.supervisor.HTTP_Request;

import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.fabmo.supervisor.Device;
import com.fabmo.supervisor.Status;
import android.os.AsyncTask;

public class GetStatus extends AsyncTask<URL, Integer, Status>{
	public Device my_device;
	public GetStatus(Device device) {
		my_device=device;
	}

	@Override
	protected com.fabmo.supervisor.Status doInBackground(URL... urls) {
		com.fabmo.supervisor.Status return_status = null;
	    int count = urls.length;

        // This will download stuff from each URL passed in
        for (int i = 0; i < count; i++) {	
			Scanner scanner = null;
			JSONObject jsonStatus =null;
			try{
			URL statusRequest = urls[i];
			scanner = new Scanner(statusRequest.openStream());
			String response = scanner.useDelimiter("\\Z").next();
			jsonStatus = new JSONObject(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			scanner.close();
			
			String state = "disconnected";
			double posx = 0;
			double posy = 0;
			double posz = 0;
		    String current_file = null;
		    int nb_lines = 0;
		    int line = 0;
			try {
				state = jsonStatus.getJSONObject("status").getString("state");
				posx = jsonStatus.getJSONObject("status").getDouble("posx");
				posy = jsonStatus.getJSONObject("status").getDouble("posy");
				posz = jsonStatus.getJSONObject("status").getDouble("posz");
				current_file = jsonStatus.getJSONObject("status").getString("current_file");
				nb_lines = jsonStatus.getJSONObject("status").getInt("nb_lines");
				line = jsonStatus.getJSONObject("status").getInt("line");
			} catch (JSONException e) {
				
			}
			if(current_file != null && current_file.compareTo("null")!=0)
				return_status = new com.fabmo.supervisor.Status(state, posx, posy, posz, current_file, nb_lines, line);
			else
				return_status = new com.fabmo.supervisor.Status(state, posx, posy, posz);
        }
        return return_status;
	}
	
	@Override
	protected void onPostExecute(com.fabmo.supervisor.Status result) {
		my_device.getStatusCallback(result);
	}

}
