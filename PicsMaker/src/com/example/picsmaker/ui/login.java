package com.example.picsmaker.ui;

import com.example.picsmaker.R;
import com.example.picsmaker.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class login extends Activity implements View.OnClickListener{

	 
	ImageButton iconimg = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initialize();
	}
	private void initialize() {

		  
		iconimg=(ImageButton)findViewById(R.id.wxicon);
		iconimg.setOnClickListener(this);	

	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.wxicon:
			
			//���ӵ�΢�ŵ�¼
			//�ȷ��ɸ���һ������
			//ǰ�˺�˹�������
			//�Լ�ѡ��app����ҲҪд��
			//�ҿ���
			Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_LONG).show();
			Intent intent3 = new Intent(this,MainActivity.class);
			startActivity(intent3);
			break;
		}		
	}
	 
	 
	 
}

 