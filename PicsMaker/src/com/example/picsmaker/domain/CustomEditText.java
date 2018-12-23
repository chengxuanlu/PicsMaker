package com.example.picsmaker.domain;
//textview，显示文字
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class CustomEditText extends TextView{

    private Typeface font;
    public float bitmapX;  
    public float bitmapY;  
    private textClick mytextClick;
    private float startx;// down事件发生时，手指相对于view左上角x轴的距离
    private float starty;// down事件发生时，手指相对于view左上角y轴的距离
    private float endx; // move事件发生时，手指相对于view左上角x轴的距离
    private float endy; // move事件发生时，手指相对于view左上角y轴的距离
    private float left; // DragTV左边缘相对于父控件的距离
    private float top; // DragTV上边缘相对于父控件的距离
    private int right; // DragTV右边缘相对于父控件的距离
    private int bottom; // DragTV底边缘相对于父控件的距离
    private float hor; // 触摸情况下，手指在x轴方向移动的距离
    private float ver; // 触摸情况下，手指在y轴方向移动的距离
    private float mfX, mfY, msX, msY;
    private boolean isMove = true;
    private float mAngle;
    private int ptrID1 = INVALID_POINTER_ID, ptrID2 = INVALID_POINTER_ID;
    private float oldDist = 0;
    private float textSize = 0;
    private float scale;
    private static final int INVALID_POINTER_ID = -1;
    private MotionEvent mEvent;
    private boolean onefinger;
    
	public CustomEditText(Context context,Typeface _font) {
		super(context);
		// TODO Auto-generated constructor stub
		 //设置刻度类型
		this.setTypeface(_font);
		this.font = _font;
		this.bitmapX = 0;
		this.bitmapY = 200;
		this.setHint("Picture Maker");
		this.setClickable(true);
	}
	
	public Typeface getFontFamily() {
		return this.font;
	}
	
	@Override
	public boolean performClick() {
	  return super.performClick();
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    // ▼ 注意这里使用的是 getAction()，先埋一个小尾巴。
	    switch (event.getAction()){
	        case MotionEvent.ACTION_DOWN:
	        	// 手指按下
	        	onefinger = true;
                //手指落点相对于TextView的坐标
                startx = event.getX();
                starty = event.getY();
                Log.d("HHHH", "onTouch: ACTION_DOWM " + startx);
                ptrID1 = event.getPointerId(event.getActionIndex());
	            break;
	            
	        case MotionEvent.ACTION_POINTER_DOWN:
                isMove = false;
                onefinger = false;

                ptrID2 = event.getPointerId(event.getActionIndex());
                //手指落点相对于RelativeLayout的坐标
                msX = mEvent.getX(event.findPointerIndex(ptrID2));
                msY = mEvent.getY(event.findPointerIndex(ptrID2));
                mfX = mEvent.getX(event.findPointerIndex(ptrID1));
                mfY = mEvent.getY(event.findPointerIndex(ptrID1));
                oldDist = spacing(event);
                break;

	        case MotionEvent.ACTION_MOVE:
	            // 手指移动
	        	Log.d("HHHH", "onTouch: ACTION_MOVE ");
                left = this.getX();
                top = this.getY();
                if (isMove) {
                    endx = event.getX();
                    endy = event.getY();
                    hor = (endx - startx);
                    ver = (endy - starty);
                    this.setX(left + hor);
                    this.setY(top + ver);

                }
                else  {
                    //处理旋转模块
                    float nfX, nfY, nsX, nsY, test;
                    test = event.getX(event.findPointerIndex(ptrID1));

                    nsX = mEvent.getX(event.findPointerIndex(ptrID2));
                    nsY = mEvent.getY(event.findPointerIndex(ptrID2));
                    nfX = mEvent.getX(event.findPointerIndex(ptrID1));
                    nfY = mEvent.getY(event.findPointerIndex(ptrID1));
                    mAngle = angleBetweenLines(mfX, mfY, msX, msY, nfX, nfY, nsX, nsY);
                    this.setRotation(mAngle);
                    //缩放
                    float newDist = spacing(event);
                    if (newDist > oldDist + 1) {
                        zoom(newDist / oldDist);
                        oldDist = newDist;
                    }
                    if (newDist < oldDist - 1) {
                        zoom(newDist / oldDist);
                        oldDist = newDist;
                    }

                }

	            break;
	        case MotionEvent.ACTION_UP:
	            // 手指抬起
	        	performClick();
	            break;
	        case MotionEvent.ACTION_CANCEL:
	            // 事件被拦截 
	            break;
	        case MotionEvent.ACTION_OUTSIDE:
	            // 超出区域 
	            break;
	        case MotionEvent.ACTION_POINTER_UP:
                Log.d("HHHH", "onTouch: ACTION_POINTER_UP");
                isMove = true;
                break;
	    }
	    return super.onTouchEvent(event);
	}
	
	
	
	private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    private void zoom(float f) {
        this.setTextSize(textSize *= f);
    }

    /**
     * 计算刚开始触摸的两个点构成的直线和滑动过程中两个点构成直线的角度
     *
     * @param fX  初始点一号x坐标
     * @param fY  初始点一号y坐标
     * @param sX  初始点二号x坐标
     * @param sY  初始点二号y坐标
     * @param nfX 终点一号x坐标
     * @param nfY 终点一号y坐标
     * @param nsX 终点二号x坐标
     * @param nsY 终点二号y坐标
     * @return 构成的角度值
     */
    private float angleBetweenLines(float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float angle1 = (float) Math.atan2((fY - sY), (fX - sX));
        float angle2 = (float) Math.atan2((nfY - nsY), (nfX - nsX));

        float angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return -angle;
    }
    
    public interface textClick {
        void onTextClick();
    }
    
    public textClick getTextClick() {
        return mytextClick;
    }
    public void setTextClick(textClick mytextClick) {
        this.mytextClick = mytextClick;
    }



	

}
