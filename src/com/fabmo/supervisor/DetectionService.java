package com.fabmo.supervisor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

public class DetectionService implements Runnable{
	 Context mContext;
	 DatagramSocket socket;
	 int port = 24862;
	 public static String REQ = "R U A SBT ?\0";
	 Thread server;
	 static boolean run_server = false;
	 static ArrayList<JSONObject> devices;
	 
	 
	 public DetectionService(Context mContext){
	       this.mContext = mContext;

	       DetectionService.devices = new ArrayList<JSONObject>();
	       this.server = new Thread(this);
	       this.server.start();

	  }
	 
	InetAddress getBroadcastAddress() throws IOException {
	    WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	    DhcpInfo dhcp = wifi.getDhcpInfo();
	    // handle null somehow

	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++)
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    return InetAddress.getByAddress(quads);
	}
	
	void SendBroadcast(String data, int port){
		try {

			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),getBroadcastAddress(), port);
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void run(){    
	   try {

	    	
			socket = new DatagramSocket(port);
			socket.setBroadcast(true);
			socket.setReuseAddress(true);
			//socket.bind(new InetSocketAddress(port));
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	   this.SendBroadcast(REQ, this.port);
	   
		//byte[] buf = new byte[1024];
		//DatagramPacket packet = new DatagramPacket(buf, buf.length);
		run_server=true;
		UDPResponder.current_dialog = new ArrayList<InetAddress>();
		try {
			while(run_server)
			{
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				new Thread(new UDPResponder(socket, packet, this)).start();
				//buf = new byte[1024];
				//packet.setData(buf);
			}
			socket.close();
			return;
		} catch (IOException e) {
			socket.close();
			e.printStackTrace();
		}
		
	}

	public void add_device(JSONObject device) {
		DetectionService.devices.add(device);
	}
	
}
