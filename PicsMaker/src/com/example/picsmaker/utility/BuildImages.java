package com.example.picsmaker.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.picsmaker.R;
import com.example.picsmaker.ui.MainActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BuildImages {
	
	   
	
	    static String localTempImgDir; // ������Ƭ���ļ�������
	   
	    static File dirPicSys; // ��APP��ϵͳ���
	 
	    public static void CreateGallery(Context context)//����������
	    {
	        // ��ϵͳ���������һ����Ӧ������һ�����ļ��У�ר�ű��汾APP���ջ�õ���Ƭ
	        localTempImgDir = context.getResources().getString(R.string.app_name);
	        createFileInAlbum(); // ������APP��ϵͳ���
	       
	    }
	    /**
	     * 
	     * @throws IOException
	     * 
	     * @see ��ϵͳ����д���һ����ΪlocalTempImgDir������ļ���, �������汾APP�������չ��ܻ�õ���Ƭ
	     * 
	     */
	    private static void createFileInAlbum()
	    {           
	    	dirPicSys = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + localTempImgDir); // �������ļ���·��
	        //dirPicSys = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir); // �������ļ���·��
	        if (!dirPicSys.exists())
	        { // ��ϵͳ����д���һ����ΪӦ�����Ƶ�����ļ���
	            dirPicSys.mkdirs();
	        }
	    }
	    
	
	    /**
	     * ����һ��ͼƬ
	     */


	    public static void buildImageFromView(Activity activity) {    
	    	
	    	// View������Ҫ��ͼ��View
			View view = activity.getWindow().getDecorView().findViewById(R.id.mylayout);
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			Bitmap b1 = view.getDrawingCache();
			
			// ��ȡ״̬���߶�		
	    	Rect frame = new Rect();
	    	activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	    	//view.getWindowVisibleDisplayFrame(frame);		
	    	int statusBarHeight = frame.top; 
	    	
	    	// ��ȡ��Ļ���͸�
	    	int w = view.getWidth();    
	    	int h = view.getHeight();  
	
	    	// ȥ��������		
	    	//Bitmap bmp = Bitmap.createBitmap(b1, 0, statusBarHeight, w, h - statusBarHeight);
	    	Bitmap bmp = Bitmap.createBitmap(b1, 0, 0, w, h);
	    	view.destroyDrawingCache();

	    	Canvas c = new Canvas(bmp);    
	    	//Canvas c = new Canvas(b1);
	    	c.drawColor(Color.WHITE);    
	    	/** ���������canvas����Ϊ��ɫ��������͸�� */    
	    	view.draw(c);    
     
	    	try {
	    		//bitmapתpng
				saveBitmap(bmp);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	    	
	    } 

	    
	    //bitmapתpng
	    private static void saveBitmap(Bitmap bitmap) throws IOException
	    {
	    	//��ʱ���ͼƬ����
	    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//�������ڸ�ʽ
	    	String bitName = df.format(new Date())+".png";
	    	
	        File file = new File(dirPicSys +"/"+bitName);
	        if(file.exists()){
	            file.delete();
	        }
	        FileOutputStream out;
	        try{
	            out = new FileOutputStream(file);
	            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
	            {
	                out.flush();
	                out.close();
	            }
	        }
	        catch (FileNotFoundException e)
	        {
	            e.printStackTrace();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }
	
}
