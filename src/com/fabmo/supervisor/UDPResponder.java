package com.fabmo.supervisor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import org.json.*;

public class UDPResponder implements Runnable, EventListener{
	    DatagramSocket socket = null;
	    DatagramPacket packet = null;
	    
	    static DetectionService detection_service;
	    
	    static String OK = "YES I M !";
		static String ERR = "I DNT UNDRSTND !\0";
		static String HOSTNAME = "U NAME ?\0";
		static ArrayList<InetAddress> current_dialog ;

		public UDPResponder(DatagramSocket socket, DatagramPacket packet, DetectionService dt) {
	        this.socket = socket;
	        this.packet = packet;
	        UDPResponder.detection_service = dt;
	    }

	    public void run() {
	    	byte[] data = this.packet.getData();
	    	data = Arrays.copyOf(data, Arrays.binarySearch(data, (byte) 0)-1);
	    	String message="";
			try {
				message = new String(data, "UTF-8").toString().trim();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
				if(message.contains(OK))
				{
			        DatagramPacket response = new DatagramPacket(HOSTNAME.getBytes(), HOSTNAME.getBytes().length,this.packet.getAddress(), this.packet.getPort());
				        try {
							socket.send(response);
						} catch (IOException e) {
							e.printStackTrace();
						}
					UDPResponder.current_dialog.add(this.packet.getAddress()); // add ip in an array
					//System.out.println(UDPResponder.current_dialog);
				}
				else if(UDPResponder.current_dialog.contains(this.packet.getAddress())) // if the device is a sbt, continue the dialog.
				{
					UDPResponder.current_dialog.remove(this.packet.getAddress()); //end of dialog (remove ip from array )
					//substract \0 character of the string before parsing.
					JSONObject device = null;
					try {
						device = new JSONObject("{\"device\" : " + message.replace("\0", "") + ",\"active_ip\" : \""+ this.packet.getAddress().getHostAddress()+"\"}");
					} catch (JSONException e) {

						e.printStackTrace();
					}

					System.out.println("new device added !");
					UDPResponder.detection_service.add_device(device);

				}
				else
				{
					System.out.println("[UDPResponder.java] received from "+this.packet.getAddress().getHostAddress()+" : unknow message : '"+ message +"'");
				}
	    }
}
