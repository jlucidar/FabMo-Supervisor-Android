package com.fabmo.supervisor;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;



	public class StatusReportAdapter extends ArrayAdapter<Device> {
		  private final Context context;
		  private final Device[] devs;

		  public StatusReportAdapter(Context context, Device[] devs) {
		    super(context, R.layout.status_report_view, devs);
		    this.context = context;
		    this.devs = devs;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
			View deviceView = inflater.inflate(R.layout.status_report_view,parent,false);
			TextView hostname_text_view = (TextView) deviceView.findViewById(R.id.device_hostname);
			TextView network_text_view = (TextView) deviceView.findViewById(R.id.network);
			
			TextView state_text_view = (TextView) deviceView.findViewById(R.id.state);
			TextView posx_text_view = (TextView) deviceView.findViewById(R.id.posx);
			TextView posy_text_view = (TextView) deviceView.findViewById(R.id.posy);
			TextView posz_text_view = (TextView) deviceView.findViewById(R.id.posz);
			
			hostname_text_view.setText(devs[position].hostname);;
			network_text_view.setText(devs[position].network.toString());
			
			if(devs[position].status == null)
				devs[position].status = new Status("undefined", 0, 0, 0);
			state_text_view.setText(devs[position].status.state);
			posx_text_view.setText(new DecimalFormat("##0.000;-##0.000").format(devs[position].status.posx));
			posy_text_view.setText(new DecimalFormat("##0.000;-##0.000").format(devs[position].status.posy));
			posz_text_view.setText(new DecimalFormat("##0.000;-##0.000").format(devs[position].status.posz));
			
			if(devs[position].status.current_file != null)
			{
				// TODO display the file status view
				View fileView = inflater.inflate(R.layout.file_report_view,(ViewGroup)deviceView,false);
				TextView current_file_text_view = (TextView) fileView.findViewById(R.id.current_file);
				ProgressBar file_progress_bar_view = (ProgressBar) fileView.findViewById(R.id.file_progressBar);
				// fill in any details dynamically here
				current_file_text_view.setText(devs[position].status.current_file);
				file_progress_bar_view.setMax(devs[position].status.nb_lines);
				file_progress_bar_view.setProgress(devs[position].status.line);

				// insert into main view
				((ViewGroup) deviceView).addView(fileView, 2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			}
				
		    return deviceView;
		  }
	}
