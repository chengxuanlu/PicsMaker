package com.example.picsmaker.ui;

import com.example.picsmaker.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class createNewTag extends Dialog implements View.OnClickListener{
	public createNewTag(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

//	public class createNewTag extends Dialog {  
		  
	    /** 
	     * �����Ķ��� * 
	     */  
	    Activity context;  
	  
	    private Button btn_save;  
	  
	    public EditText newTagName;  
	   
	    public static String newtag;
	  
	   // private View.OnClickListener mClickListener;  
	  
	    public createNewTag(Activity context) {  
	        super(context);  
	        this.context = context;  
	    }  
	  
	/*    public createNewTag(Activity context, int theme, View.OnClickListener clickListener) {  
	        super(context, theme);  
	        this.context = context;  
	        this.mClickListener = clickListener;  
	    }  */
	    public createNewTag(Activity context, int theme ) {  
	        super(context, theme);  
	        this.context = context;  
	      //  this.mClickListener = clickListener;  
	    }  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        // ָ������  
	        this.setContentView(R.layout.create_newtag);  
	  
	        newTagName = (EditText) findViewById(R.id.newTagName);  
	        btn_save = (Button) findViewById(R.id.btn_save_pop);
	        btn_save.setOnClickListener(this);
	  
	        /* 
	         * ��ȡʥ����Ĵ��ڶ��󼰲����������޸ĶԻ���Ĳ�������, ����ֱ�ӵ���getWindow(),��ʾ������Activity��Window 
	         * ����,�����������ͬ���ķ�ʽ�ı����Activity������. 
	         */  
	        Window dialogWindow = this.getWindow();  
	  
	        WindowManager m = context.getWindowManager();  
	        Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������  
	        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ  
	        // p.height = (int) (d.getHeight() * 0.6); // �߶�����Ϊ��Ļ��0.6  
	        p.width = (int) (d.getWidth() * 0.8); // �������Ϊ��Ļ��0.8  
	        dialogWindow.setAttributes(p);  
	  
	        // ����id�ڲ������ҵ��ؼ�����  
	        btn_save = (Button) findViewById(R.id.btn_save_pop);  
	  
	        // Ϊ��ť�󶨵���¼�������  
	      //  btn_save.setOnClickListener(mClickListener);  
	        
	        this.setCancelable(true); 
	    }
	    public void onClick(View v) {
    		// TODO Auto-generated method stub

    		switch (v.getId()) {
    		case R.id.btn_save_pop:
    			//保存新标签为newtag
    			  newtag = newTagName.getText().toString();
    		}
        }
//	}  
}
