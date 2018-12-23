package com.example.picsmaker.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.picsmaker.R;
import com.example.picsmaker.R.layout;
import com.example.picsmaker.dao.Picturedao;
import com.example.picsmaker.domain.Picture;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.*;
import android.util.DisplayMetrics;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 
public class MainActivity extends Activity implements View.OnClickListener{
	Button btnnewATag = null;
	Button btnautoATag = null;
	Button btnTag1 = null;
	Button btnTag2 = null;
	Button btnMe = null;
	Button btnEdit = null;
	Button btnAdd = null;
	ImageView btnaddframe = null;
	private LinearLayout contentLlayout;
    private Button addImg;
    ArrayList<String> strs;
    static String currentTag = null;
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
	}
	private void initialize() {
		
		contentLlayout = (LinearLayout) findViewById(R.id.test_layout);
        addImg = (Button) findViewById(R.id.tag1);
        addImg.setOnClickListener(this);
		btnnewATag = (Button) findViewById(R.id.newATag);
		btnnewATag.setOnClickListener(this);
		btnautoATag = (Button) findViewById(R.id.autoATag);
		btnautoATag.setOnClickListener(this);	
		btnTag1 = (Button) findViewById(R.id.autoATag);
		btnTag1.setOnClickListener(this);
		btnTag2 = (Button) findViewById(R.id.autoATag);
		btnTag2.setOnClickListener(this);
		btnMe = (Button) findViewById(R.id.me);
		btnMe.setOnClickListener(this);
		btnEdit = (Button) findViewById(R.id.autoATag);
		btnEdit.setOnClickListener(this);
		btnAdd = (Button) findViewById(R.id.add);
		btnAdd.setOnClickListener(this);
		btnaddframe = (ImageView) findViewById(R.id.addframe);
		btnaddframe.setOnClickListener(this);
		 

	}
	 
    public void showEditDialog() {
        createNewTag createNewTag = new createNewTag(this,R.layout.create_newtag);
        createNewTag.show();
    }

	 private void addImg(String filepath){
		 	//先清空布局
		 	contentLlayout.removeAllViews();
	        ImageView newImg = new ImageView(this);
	        //设置想要的图片，相当于android:src="@drawable/image"
	        //newImg.setImageResource(R.drawable.icon);
	        File file = new File(filepath);
	        if(file.exists()) {
	        	Bitmap bm=BitmapFactory.decodeFile(filepath);
	        	newImg.setImageBitmap(bm);
	        }
	        
	        //设置子控件在父容器中的位置布局，wrap_content,match_parent
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	                40,30);
	        // 也可以自己想要的宽度，参数（int width, int height）均表示px
	        // 如dp单位，首先获取屏幕的分辨率在求出密度，根据屏幕ppi=160时，1px=1dp
	        //则公式为 dp * ppi / 160 = px ——> dp * dendity = px
	        //如设置为48dp：1、获取屏幕的分辨率 2、求出density 3、设置
	        DisplayMetrics displayMetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	        float density = displayMetrics.density;
	        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
	                (int)(48 * density),
	                (int)(48 * density));
	 
	        //相当于android:layout_marginLeft="8dp"
	        params1.leftMargin = 8;
	 
	        //addView(View child)，默认往已有的view后面添加，后插入，不设置布局params,默认wrap_content
	        contentLlayout.addView(newImg);
	 
	        //addView(View child, LayoutParams params)，往已有的view后面添加，后插入,并设置布局
	        //contentLlayout.addView(newImg,params1);
	 
	        //addView(View view,int index, LayoutParams params),在某个index处插入
	        //contentLlayout.addView(newImg, 0, params1);
	    }
	

	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode==200){			
				strs =(ArrayList<String>) data.getExtras().get("data");
				//将选中的图片；路径传入数据库
				
				
				Toast.makeText(getApplicationContext(), "��ѡ����"+strs.size()+"��", 0).show();;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.newATag:
			int count=1;//计数器，计新增加按钮个数
			//在后面加一个BUTTON
			Button newBtn =new Button(MainActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					60, 60);
			LinearLayout layout =(LinearLayout) findViewById(R.id.firstlayout);
			layout.addView(newBtn);
			//弹出弹窗，用户输入新标签名称
			showEditDialog();
			count++;
			//给新建按钮添加标签名字
			String a = createNewTag.newtag;
			newBtn.setText(a);
			//给新增按钮添加id
			if(count==1)	newBtn.setId(R.id.newtag1);
			else if(count==2)	newBtn.setId(R.id.newtag2);
			else if(count==3)	newBtn.setId(R.id.newtag3);
			else if(count==4)	newBtn.setId(R.id.newtag4);
			else if(count==5)	newBtn.setId(R.id.newtag5);
			else newBtn.setId(R.id.newtag6);
			
			//将新建的标签加入数据库（调用后端给的方法
//			Picturedao.addTag(a);
//			currentTag = a;
			break;
		case R.id.autoATag:
			
			//新建线程，跳到相册MainAcitivity2去选择若干图片
			Intent intent = new Intent(this, MainActivity2.class);
			startActivity(intent);
			//将图片路径们传给服务器

			break;
		case R.id.tag1:			
			
			//下面展示出标签为本相册制作出的图片（初始时为空	 
			
			 // 向后端提供tag，得到String类型的列表p1
			  List<String> p1 = new ArrayList<String>(); 
			  p1=Picturedao.getPicsByTag("MadeByThisApp");
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p1.size();i++){
			  		addImg(p1.get(i));
			  }
			  currentTag = "MadeByThisApp";
			break;
		case R.id.tag2:			
			//下面展示出自动识别为人物的照片	（初始时为空	
			// 向后端提供tag，得到String类型的列表p2
			  List<String> p2 = new ArrayList<String>(); 
			  p2=Picturedao.getPicsByTag("people");
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p2.size();i++){
			  		addImg(p2.get(i));
			  }
			  currentTag = "people";
			break;
		case R.id.newtag1:			
			 	
			// 向后端提供tag，得到String类型的列表
			Button b =(Button) findViewById(R.id.newtag1);
			  List<String> p3 = new ArrayList<String>(); 
			  CharSequence text=b.getText();
			  String str = text.toString();
			  p3=Picturedao.getPicsByTag(str);//str为转换成String类型的标签
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p3.size();i++){
			  		addImg(p3.get(i));
			  }
			  currentTag = str;
			break;
		case R.id.newtag2:			
			 	
			// 向后端提供tag，得到String类型的列表
			Button b2 =(Button) findViewById(R.id.newtag1);
			  List<String> p4 = new ArrayList<String>(); 
			  CharSequence text2=b2.getText();
			  String str2 = text2.toString();
			  p3=Picturedao.getPicsByTag(str2);//str为转换成String类型的标签
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p3.size();i++){
			  		addImg(p3.get(i));
			  }
			  currentTag = str2;
			break;
		case R.id.newtag3:			
			//下面展示出自动识别为人物的照片	（初始时为空	
			// 向后端提供tag，得到String类型的列表p2
			Button b3 =(Button) findViewById(R.id.newtag1);
			  List<String> p5 = new ArrayList<String>(); 
			  CharSequence text3=b3.getText();
			  String str3 = text3.toString();
			  p3=Picturedao.getPicsByTag(str3);//str为转换成String类型的标签
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p3.size();i++){
			  		addImg(p3.get(i));
			  }
			  currentTag = str3;
			break;
		case R.id.newtag4:			
			 	
			// 向后端提供tag，得到String类型的列表
			Button b4 =(Button) findViewById(R.id.newtag1);
			  List<String> p6 = new ArrayList<String>(); 
			  CharSequence text4=b4.getText();
			  String str4 = text4.toString();
			  p3=Picturedao.getPicsByTag(str4);//str为转换成String类型的标签
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p3.size();i++){
			  		addImg(p3.get(i));
			  }
			  currentTag = str4;
			break;
		case R.id.newtag5:			
			 	
			// 向后端提供tag，得到String类型的列表
			Button b5 =(Button) findViewById(R.id.newtag1);
			  List<String> p7 = new ArrayList<String>(); 
			  CharSequence text5=b5.getText();
			  String str5 = text5.toString();
			  p3=Picturedao.getPicsByTag(str5);//str为转换成String类型的标签
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p3.size();i++){
			  		addImg(p3.get(i));
			  }
			  currentTag = str5;
			break;
		case R.id.newtag6:			
		 	
			// 向后端提供tag，得到String类型的列表p2
			Button b6 =(Button) findViewById(R.id.newtag1);
			  List<String> p8 = new ArrayList<String>(); 
			  CharSequence text6=b6.getText();
			  String str6 = text6.toString();
			  p3=Picturedao.getPicsByTag(str6);//str为转换成String类型的标签
			
			  //把列表元素的地址转化成图片放在xml里
			  for(int i=0;i<p3.size();i++){
			  		addImg(p3.get(i));
			  }
			  currentTag = str6;
			break;
		case R.id.addframe:
			
			//新建线程，跳到相册MainAcitivity2去选择若干图片			
			Intent intent2 = new Intent(this, MainActivity2.class);
			startActivity(intent2);
			//将选好的照片添加到数据库
			int flag =0;
			for(int i=0;i<strs.size();i++) {  
				String p = strs.get(i);	//路径
				String tag = currentTag;			//标签
				Picture pic = new Picture(p,tag);   //创建picture对象
				Picturedao.addPicByTag( pic);		//将picture对象添加到数据库
			}
			
			break;
		case R.id.me:
			//新建线程，跳到个人主页			
			Intent intent3 = new Intent(this,Me.class);
			startActivity(intent3);
			break;		
		case R.id.add:
			//新建线程，跳到 EditActivity		
			Intent intent4 = new Intent(this,EditActivity.class);
			startActivity(intent4);
			break;
		}		
	}
}
