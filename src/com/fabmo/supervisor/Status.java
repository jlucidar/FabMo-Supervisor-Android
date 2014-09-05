package com.fabmo.supervisor;

public class Status {
	public String state = null ;
	public double posx = 0 ;
	public double posy = 0 ;
	public double posz = 0 ;
	/* not implemented yet
	public double posa = (Float) null ;
	public double posb = (Float) null ;
	public double posc = (Float) null ;
	*/
    public String current_file = null ;
    public int nb_lines = 0;
    public int line = 0;
    
    
	public Status(String state, double posx, double posy, double posz) {
		this.state = state;
		this.posx = posx;
		this.posy = posy;
		this.posz = posz;
	}
    
    
	public Status(String state, double posx, double posy, double posz,
			String current_file, int nb_lines, int line) {
		this.state = state;
		this.posx = posx;
		this.posy = posy;
		this.posz = posz;
		this.current_file = current_file;
		this.nb_lines = nb_lines;
		this.line = line;
	}


	@Override
	public String toString() {
		return "{state=" + state + ", posx=" + posx + ", posy=" + posy
				+ ", posz=" + posz + ", current_file=" + current_file
				+ ", nb_lines=" + nb_lines + ", line=" + line + "}";
	}
	
	
	
}
