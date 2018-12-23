package com.example.picsmaker.entity;

import com.example.picsmaker.R.drawable;

import android.graphics.drawable.Drawable;

public class background {
	
	private Drawable icon;
	private String info;
	private int id;
	
	public background() {
		
	}
	
    public background(Drawable hIcon, int _id,String hName) {
        this.icon = hIcon;
        this.info = hName;
        this.id = _id;
    }
    
    public void setId(int _id) {
    	this.id = _id;
    }
    
    public int getId() {
    	return id;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getInfo() {
        return info;
    }

    public void setIcon(Drawable hIcon) {
        this.icon = hIcon;
    }

    public void setInfo(String hName) {
        this.info = hName;
    }
}
