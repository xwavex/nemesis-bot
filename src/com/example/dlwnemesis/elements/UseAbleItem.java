package com.example.dlwnemesis.elements;

public abstract class UseAbleItem extends Item {
	protected int level;
	protected String type;
	protected String id;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString() {
		return "[Level: " + level + ", Type: " + type + ", ID: " + id + "]";
	}
}
