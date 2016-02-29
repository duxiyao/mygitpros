package com.kjstudy.bean;

public enum MsgLocation {
	LOCATIONLEFT(1),
	LOCATIONRIGHT(2),
	LOCATIONCENTER(3);
	private final int msgLocation;
	private MsgLocation(int msgLocation){
		this.msgLocation=msgLocation;
	}
	
	public int getValue(){
		return msgLocation;
	}
}
