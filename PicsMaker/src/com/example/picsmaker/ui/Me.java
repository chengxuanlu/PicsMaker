package com.example.picsmaker.ui;

import com.example.picsmaker.R;
import com.example.picsmaker.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Me extends Activity implements View.OnClickListener{

	Button btnexit = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me);
		initialize();
	}
	private void initialize() {

		btnexit = (Button) findViewById(R.id.exit);
		btnexit.setOnClickListener(this);	 

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.exit:
			
			//���ӵ�΢���˳���¼
			//�ȷ��ɸ�����һ���������˳�
			Toast.makeText(getApplicationContext(), "您已退出账号", Toast.LENGTH_LONG).show();
			break;
	/*	case R.id.add:
			//��ת��add����
			startActivity(EditActivity);//�������
			break;
		case R.id.picture:
			//��ת��picture������
			startActivity(MainActivity);//�������
			break;*/
		}		
	}
}
