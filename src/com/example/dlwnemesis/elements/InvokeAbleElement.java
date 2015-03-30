package com.example.dlwnemesis.elements;

public abstract class InvokeAbleElement {
	protected String uri = "";
	protected String payload = "";
	
	public String getUri() {
		return uri;
	}
	
	public String getPayLoad() {
		return payload;
	}
	
	public abstract void parseContent(String content);
}
