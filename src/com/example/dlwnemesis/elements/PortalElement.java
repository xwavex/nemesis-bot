package com.example.dlwnemesis.elements;

import java.util.ArrayList;
import java.util.Date;

import com.example.dlwnemesis.behavior.LocationData;

import android.util.Log;


public class PortalElement {
	private String title;
	private String id;
	private String faction;
	private Date timeReadyAgain;
	private LocationData location;
	
	private int hacksPerTime;
	private int countCollectedItems;
	private int countHits;
	
	public ArrayList<HackProc> collectList;
	
	/**
	 * TOO_SOON_BIG - Unsafe to acquire items. Estimated time to
	 * cooldown: 300 seconds TOO_SOON_ - Parse Seconds. Range(9,3) (e.g.
	 * TOO_SOON_240_SECS) TOO_OFTEN - It may take significant time for
	 * the Portal to reset OUT_OF_RANGE - clear! NEED_MORE_ENERGY - Not
	 * enough XM.
	 */
	public void setPortalCountDown(String s) {
		Log.d("DLWCC", "Set Portal cooldown");
		Date now=null;
		if (s.equalsIgnoreCase("TOO_SOON_BIG")) {
			now = new Date();
			now.setTime(now.getTime()+300*1000);
		} else if (s.contains("TOO_SOON_")) {
			String tmp = s.replace("TOO_SOON_", "");
			tmp = tmp.substring(0, tmp.length()-5);
			
			now = new Date();
			now.setTime(now.getTime()+Integer.parseInt(tmp)*1000);
		} else if (s.equalsIgnoreCase("TOO_OFTEN")) {
			now = new Date();
			now.setTime(now.getTime()+14400*1000);
		} else if (s.equalsIgnoreCase("OUT_OF_RANGE")) {
			//TODO logger
		} else if (s.equalsIgnoreCase("NEED_MORE_ENERGY")) {
			//TODO logger
		}else if (s.equalsIgnoreCase("OK")) {
			now = new Date();
			now.setTime(0);
		}
		if (now != null) {
			setTimeReadyAgain(now);
		}
		Log.d("DLWCC", "cooldown gesetzt");
	}
	
	public PortalElement() {
		Date tmp = new Date();
		tmp.setTime(0);
		setTimeReadyAgain(tmp);
	}
	public PortalElement(String i, String t, String f, Date time) {
		setTitle(t);
		setId(i);
		setFaction(f);
		setTimeReadyAgain(time);
	}
	public LocationData getLocation() {
		return location;
	}
	public void setLocation(LocationData location) {
		this.location = location;
	}
	public int getCountHits() {
		return countHits;
	}
	public void setCountHits(int countHits) {
		this.countHits = countHits;
	}
	public int getCountCollectedItems() {
		return countCollectedItems;
	}
	public void setCountCollectedItems(int countCollectedItems) {
		this.countCollectedItems = countCollectedItems;
	}
	public int getHacksPerTime() {
		return hacksPerTime;
	}
	public void setHacksPerTime(int hacksPerTime) {
		this.hacksPerTime = hacksPerTime;
	}
	public String getFaction() {
		return faction;
	}
	public void setFaction(String faction) {
		this.faction = faction;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getTimeReadyAgain() {
		return timeReadyAgain;
	}
	public void setTimeReadyAgain(Date timeReadyAgain) {
		this.timeReadyAgain = timeReadyAgain;
	}
	
}
