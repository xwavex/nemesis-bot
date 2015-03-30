package com.example.dlwnemesis.elements;

import java.util.ArrayList;

public class HackProc {
	private String status;
	private ArrayList<UseAbleItem> items;
	private boolean read = false;

	/**
	 * TOO_SOON_BIG - Unsafe to acquire items. Estimated time to cooldown: 300
	 * seconds TOO_SOON_ - Parse Seconds. Range(9,3) (e.g. TOO_SOON_240_SECS)
	 * TOO_OFTEN - It may take significant time for the Portal to reset
	 * OUT_OF_RANGE - clear! NEED_MORE_ENERGY - Not enough XM.
	 */
	public HackProc(String s) {
		setStatus(s);
	}

	public void setCollectedItems(ArrayList<UseAbleItem> i) {
		items = i;
	}

	public ArrayList<UseAbleItem> getCollectedItems() {
		return items;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		String itemString = "";
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				itemString = itemString + items.get(i).toString();
			}
		}
		return "Status: " + status + ", " + itemString;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
