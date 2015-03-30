package com.example.dlwnemesis.behavior;

public class LocationData {
	private int latE6 = -1;
	private int lonE6 = -1;
	
	public LocationData(int latE6, int lonE6) {
		this.latE6 = latE6;
		this.lonE6 = lonE6;
	}
	
	public int getLatE6() {
		return latE6;
	}
	
	public int getLonE6() {
		return lonE6;
	}
	
	public void setLocation(int latE6, int lonE6) {
		this.latE6 = latE6;
		this.lonE6 = lonE6;
	}
	
	public void setLatE6(int latE6) {
		this.latE6 = latE6;
	}
	
	public void setLonE6(int lonE6) {
		this.lonE6 = lonE6;
	}
	
	public void setLatE6Hex(String hex) {
		this.latE6 = Integer.parseInt(hex,16);
	}
	
	public void setLonE6Hex(String hex) {
		this.lonE6 = Integer.parseInt(hex,16);
	}
	
	public String getLatE6Hex() {
		String lat2 = Integer.toHexString(this.latE6);
		if (lat2.length() < 8) {
			for (int i = 0; i <= (8 - lat2.length()); i++) {
				lat2 = "0" + lat2;
			}
		}
		return lat2;
	}
	
	public String getLonE6Hex() {
		String lon2 = Integer.toHexString(this.lonE6);
		if (lon2.length() < 8) {
			for (int i = 0; i <= (8 - lon2.length()); i++) {
				lon2 = "0" + lon2;
			}
		}
		return lon2;
	}
	
}
