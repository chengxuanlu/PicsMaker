
package com.example.picsmaker.domain;

import java.util.ArrayList;
import java.util.List;

public class Picture {
	private String path;
	private List<String> tags;

	Picture(String p, String tag){
		this.path = p;
		
		tags = new ArrayList<String>();
		tags.add(tag);
	}
	
	
	public String getPath() {
		return path;
	}
	
	public List<String> getTags(){
		return tags;
	}
}

