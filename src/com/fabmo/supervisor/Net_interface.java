package com.fabmo.supervisor;

public class Net_interface {
	public Net_interface( String itr,String ip_address) {
		this.ip_address = ip_address;
		this.itr = itr;
	}
	public String ip_address;
	public String itr;
	@Override
	public String toString() {
		return ip_address+"@"+itr ;
	}
	
	
	
}
