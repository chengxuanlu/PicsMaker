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
	
	   
	
	    static String localTempImgDir; // 保存照片的文件夹名称
	   
	    static File dirPicSys; // 本APP的系统相册
	 
	    public static void CreateGallery(Context context)//传入上下文
	    {
	        // 在系统相册中生成一个和应用名称一样的文件夹，专门保存本APP拍照获得的照片
	        localTempImgDir = context.getResources().getString(R.string.app_name);
	        createFileInAlbum(); // 创建本APP的系统相册
	       
	    }
	    /**
	     * 
	     * @throws IOException
	     * 
	     * @see 在系统相册中创建一个名为localTempImgDir的相册文件夹, 用来保存本APP调用拍照功能获得的照片
	     * 
	     */
	    private static void createFileInAlbum()
	    {           
	    	dirPicSys = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + localTempImgDir); // 获得相册文件夹路径
	        //dirPicSys = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir); // 获得相册文件夹路径
	        if (!dirPicSys.exists())
	        { // 在系统相册中创建一个名为应用名称的相册文件夹
	            dirPicSys.mkdirs();
	        }
	    }
	    
	
	    /**
	     * 生成一张图片
	     */


	    public static void buildImageFromView(Activity activity) {    
	    	
	    	// View是你需要截图的View

			View view = activity.getWindow().getDecorView().findViewById(R.id.mylayout);

			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			Bitmap b1 = view.getDrawingCache();
			
			// 获取状态栏高度		
	    	Rect frame = new Rect();
	    	activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	    	//view.getWindowVisibleDisplayFrame(frame);		
	    	int statusBarHeight = frame.top; 
	    	
	    	// 获取屏幕长和高
	    	int w = view.getWidth();    
	    	int h = view.getHeight();  
	
	    	// 去掉标题栏		
	    	//Bitmap bmp = Bitmap.createBitmap(b1, 0, statusBarHeight, w, h - statusBarHeight);
	    	Bitmap bmp = Bitmap.createBitmap(b1, 0, 0, w, h);
	    	view.destroyDrawingCache();

	    	Canvas c = new Canvas(bmp);    
	    	//Canvas c = new Canvas(b1);
	    	c.drawColor(Color.WHITE);    
	    	/** 如果不设置canvas画布为白色，则生成透明 */    
	    	view.draw(c);    
     
	    	try {
	    		//bitmap转png
				saveBitmap(bmp);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	    	
	    } 

	    
	    //bitmap转png
	    private static void saveBitmap(Bitmap bitmap) throws IOException
	    {
	    	//按时间给图片命名
	    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
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
