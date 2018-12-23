package com.example.picsmaker.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.picsmaker.domain.Picture;



public class Picturedao {
	
	public static String sql;
	
	//从服务器返回图片和标签关系，以对象传入，插入数据库表中
	public static boolean setRelationship(Picture pic){
		//TODO 将对象转换成sql插入语句
		//图片路径
		String path = pic.getPath();
		//标签
		List<String> tags = pic.getTags();
		
		//从服务器返回的结果应有且只有一个标签
		String tag = tags.get(0);
		int indexofpic = getPictureID(path);
		
		if(indexofpic==-1) {
			//插入图片表
			String sql1 = "insert into PICTURE(Picture_path)VALUES('"+path+"')";
			SQLiteJDBC.insertDB(sql1);
			indexofpic = getPictureID(path);
		}
		//从数据库查找该图片和标签对应的Picture_id和Tag_id
		
		int indexoftag = getTagID(tag);
		if(indexoftag==-1) {
			//插入图片表
			String sql1 = "insert into TAG(Tag_name)VALUES('"+tag+"')";
			SQLiteJDBC.insertDB(sql1);
			indexoftag = getTagID(tag);
		}
		int pic_id,tag_id;
		pic_id = indexofpic;
		tag_id = indexoftag;
		
		//插入图片-标签关系
		String sql2 = "insert into RELATIONSHIP(Picture_id, Tag_id)VALUES('"+pic_id+"', '"+tag_id+"')";
	
		try {
			
			SQLiteJDBC.insertDB(sql2);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public static void update(Picture pic){
		
		//TODO 将对象转换成sql更新语句
	}
	
	//点击对应标签，传入标签名，进行删除tag操作，该tag下的所有图片（该标签图片关系）将被删除
	public static boolean removeTag(String name){
		
		sql = "Delete from TAG, RELATIONSHIP where Tag_name = '"+name+"' AND TAG.Tag_id = RELATIONSHIP.Tag_id;";
		if(SQLiteJDBC.updateDB(sql)) {
			return true;
		}else {
			return false;
		}
	}
	
	//点击（某标签下的）某图片，删除图片 及标签关系//只是从数据库删除该图片关系！！
	public static boolean removePics(Picture pic){
		String tag_name = pic.getTags().get(0);
		String pic_path = pic.getPath();
		
		sql = "Delete from PICTURE, RELATIONSHIP where Picture_path = '"+pic_path+"' AND PICTURE.Picture_id = RELATIONSHIP.Picture_id ;";
		
		if(SQLiteJDBC.updateDB(sql)) {
			return true;
		}else {
			return false;
		}
	}
	
	//TODO 根据标签查询符合要求的图片列表
	public static List<String> getPicsByTag(String tag){
	
		int tag_id = getTagID(tag);
		sql = "select Picture_path from TAG, RELATIONSHIP, PICTURE where TAG.Tag_id = RELATIONSHIP.Tag_id AND RELATIONSHIP.Picture_id = PICTURE.Picture_id AND TAG.Tag_id = '"+tag_id+"' ;";
		try {
			//得到path---	
			List<Map<Object, Object>> pathlist = SQLiteJDBC.select(sql);
			List<String> result = new ArrayList<String>();
			//遍历每条结果，从map中取出path
			for(int i=0; i<pathlist.size(); i++) {
				String p = (String) pathlist.get(i).get("Picture_path");
				result.add(p);
			}
			return result;
		}catch(Exception e) {
			e.getMessage();
			return null;
		}
	}
	
	//获取所有标签并展示
	public static List<String> getTags(){
		
		sql = "select Tag_name from TAG";
		
		try {
			//得到所有已有标签名
			List<Map<Object, Object>> taglist = SQLiteJDBC.select(sql);
			List<String> result = new ArrayList<String>();
			//遍历每条结果，从map中取出path
			for(int i=0; i<taglist.size(); i++) {
				String p = (String) taglist.get(i).get("Tag_name");
				result.add(p);
			}
			return result;
		}catch(Exception e) {
			e.getMessage();
			return null;
		}
	}
	
	//获取图片id
	public static int getPictureID(String path) {
		
		sql = "select Picture_id from PICTURE where Picture_path = '"+path+"';";
		List<Map<Object, Object>> idlist = SQLiteJDBC.select(sql);
		if(idlist.size()>0) {
			int id = (Integer) idlist.get(0).get("Picture_id");
			return id;
		}
		return -1;
	}
	
	//获取tag id
	public static int getTagID(String name) {
		
		sql = "select Tag_id from TAG where Tag_name = '"+name+"' ;";
		List<Map<Object, Object>> idlist = SQLiteJDBC.select(sql);
		if(idlist.size()>0) {
			int id = (Integer) idlist.get(0).get("Tag_id");
			return id;
		}
		return -1;
	}
	
	//在某标签下，从相册选择后插入一张图片
	public static boolean addPicByTag(Picture pic) {
		 
		if(setRelationship(pic)) {
			return true;
		}
		return false;
	}
	
	//新建标签
	public static boolean addTag(String tag) {
		sql = "insert into TAG(Tag_name)VALUES('"+tag+"')";
		if(SQLiteJDBC.insertDB(sql)) {
			return true;
		}
		return false;
	}
	
}
