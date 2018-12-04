package com.example.picsmaker.domain;
import com.example.picsmaker.R;

import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.view.View;  
  
public class Material extends View {  
    //������ر���,������������ʾλ�õ�X,Y����  
    public float bitmapX;  
    public float bitmapY;  
    public Material(Context context) {  
        super(context);  
        //�������ӵ���ʼ����  
        bitmapX = 0;  
        bitmapY = 200;  
    }  
 
    //��дView���onDraw()����  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        //����,����ʵ����Paint�Ķ���  
        Paint paint = new Paint();  
        //����ͼƬ����λͼ����  
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);  
        //����������  
        canvas.drawBitmap(bitmap, bitmapX, bitmapY,paint);  
        //�ж�ͼƬ�Ƿ����,ľ�л��յĻ�ǿ���ջ�ͼƬ  
        if(bitmap.isRecycled())  
        {  
            bitmap.recycle();  
        }  
    } 
} 
