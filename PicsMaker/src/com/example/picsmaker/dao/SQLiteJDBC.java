package com.example.picsmaker.dao;

import java.sql.*;
import java.util.*;

class SQLiteJDBC {
	 public static Connection c = null;
	 public static Statement stmt = null;
	 public static String table_name = "ID_tag";
	//�������ݿ�
	 public static Connection connectSQLiteDB()
	  {
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:"+table_name+".db");
	      //System.out.println("Opened database successfully");
	      return c;
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
		return null;
	  }
	 
	 //�����ݿ��д�����
	  public static void createTable()
	  {
		    try {
		    	c = connectSQLiteDB();
		       stmt = c.createStatement();
		     //�����˻���
		      String sql = "CREATE TABLE ACCOUNT " +
		                   "(Acc_num   TEXT PRIMARY KEY CHECK(LENGTH(Acc_num)=4), " +	//���ţ�4λ�����ظ�
		                   " Acc_id   TEXT CHECK(LENGTH(Acc_id)>=12), " + //id��������Ϊ12λ��Ψһ
		                   " Acc_name         TEXT    NOT NULL, " + 
		                   " Acc_passwd       TEXT    NOT NULL CHECK(LENGTH(Acc_passwd)>=6), " +  //���볤�ȴ���6
		                   " Acc_deposit      REAL DEFAULT 0.00, " + 
		                   " Acc_cale         TEXT ) "; 
		      stmt.executeUpdate(sql);
		      //������ͨ����Ա��
		      sql = "CREATE TABLE MANAGER " +
	                "(Gene_num   TEXT PRIMARY KEY CHECK(LENGTH(Gene_num)=6), " +	//���ţ�6λ�����ظ�
	                " Gene_name  TEXT, " + 
	                " Gene_passwd    TEXT NOT NULL CHECK(LENGTH(Gene_passwd)>=6) ) ";//���볤�ȴ���6
	                        
		      stmt.executeUpdate(sql);      
		      stmt.close();
		      c.close();
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      //System.exit(0);
		    }
		   // System.out.println("Table created successfully");
	  }
	 
	  //�������ݣ��½��˻�/����Ա��
	  public static boolean insertDB(String sql)
	  {
	    try {
	      c = connectSQLiteDB();
	      c.setAutoCommit(false);
	    //  System.out.println("Opened database successfully");
	      stmt = c.createStatement();
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.commit();
	      c.close();
	      return true;
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    //  System.exit(0);
	      return false;
	    }
	  //  System.out.println("Records created successfully");
	  }
	  
	  //�������ݿ⣨��ȡ��--�޸����޸����룩
	  public static boolean updateDB(String sql)
	  {
	    try {
	      c = connectSQLiteDB();
	      c.setAutoCommit(false);
	    //  System.out.println("Opened database successfully");
	      stmt = c.createStatement();
	      stmt.executeUpdate(sql);
	      c.commit();
	      stmt.close();
	      c.close();
	      return true;
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	     // System.exit(0);
	      return false;
	    }
	   // System.out.println("Operation done successfully");
	  }
	  ///�������ResultSetת����list
	  public static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
	        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	        ResultSetMetaData md = rs.getMetaData();
	        int columnCount = md.getColumnCount();
	        while (rs.next()) {
	            Map<String, Object> rowData = new HashMap<String, Object>();
	            for (int i = 1; i <= columnCount; i++) {
	                rowData.put(md.getColumnName(i), rs.getObject(i));
	            }
	            list.add(rowData);
	        }
	        return list;
	}
	  
	  //��ѯ������������¼��������ʱ���������˻���ת�ˣ�
	  public static List<Map<String,Object>> select(String sql)
	  {
	    try {
	      c = connectSQLiteDB();
	      c.setAutoCommit(false);
	     // System.out.println("Opened database successfully");
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery(sql);
	      List<Map<String, Object>> resultlist = convertList(rs);
	      rs.close();
	      stmt.close();
	      c.close();
	      return resultlist;
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    //System.out.println("Operation done successfully");
		return null;
	  }

	 
}
