
package com.example.picsmaker.domain;
import com.example.picsmaker.R;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;  
  
@SuppressLint("ClickableViewAccessibility")
public class Material extends ImageView {  
    //������ر���,������������ʾλ�õ�X,Y����  
    public float bitmapX;  
    public float bitmapY;
    private int drawable_id;
    /*
     * ��ֻ��ָ���µ�ʱ���ǿ��Խ���ƽ�Ʋ����ģ���㴥�ص�ʱ���ܽ���ƽ�Ʋ���������ά��һ��boolean����mCanTranslate����ACTION_DOWN��ʱ����λtrue��ACTION_POINTER_DOWN��Ϊfalse
     */
    private boolean mCanTranslate = false;
    private boolean mCanRotate = false;
    private boolean mCanScale = false;
    private PointF mLastSinglePoint = new PointF(),mLastMidPoint = new PointF();
    private float mLastDist;
    //ͼƬ�ı߽����
    private RectF mBoundRectF = new RectF();
    //����ƽ�ơ����š���ת�任ͼƬ�ľ���
    private Matrix mCurrentMatrix = new Matrix();
    //��¼onLayout֮��ĳ�ʼ����������
    private float mInitialScaleFactor = 1.0f;
    //��¼ͼƬ�ܵ���������
    private float mTotalScaleFactor = 1.0f;
    //������ʼʱ�ľ���ֵ
    private float[] mBeginMatrixValues = new float[9];
    //��������ʱ�ľ���ֵ
    private float[] mEndMatrixValues = new float[9];
    //���������еľ���ֵ
    private float[] mTransformMatrixValues = new float[9];
    //���Զ���
    private ValueAnimator mAnimator = ValueAnimator.ofFloat(0f, 1f);
    //��¼��һ����ֻ��ָ���ɵ�һ������
    private PointF mLastVector = new PointF();
    //������ű�������
    private float mMaxScaleFactor = 3.0f;
    //��С���ű�������
    private float mMinScaleFactor = 0.8f;
    	
  
    public Material(Context context,int _id) {  
        super(context);  
        //�������ӵ���ʼ����  
        //bitmapX = 0;  
        //bitmapY = 200;  
        this.drawable_id = _id;
        this.setImageResource(_id);
        //���ÿ̶�����
        this.setScaleType(ScaleType.MATRIX);
        //this.setImageDrawable(getResources().getDrawable(drawable_id));
    }  
    
    public Material(Context context) {
        this(context, null);
    }

    public Material(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Material(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context, attrs);
    }
 
    //��дView���onDraw()����  
    public int getDrawableId() {
    	return this.drawable_id;
    }
    
    /**
     * ��ʼ������ͼƬ
     * ��ͼƬ���źͿؼ���Сһ�²��ƶ�ͼƬ���ĺͿؼ��������غ�
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //initImagePositionAndSize();
    }
    
    /**
     * ��ʼ��ͼƬ�Ĵ�С��λ��
     */
    private void initImagePositionAndSize() {
        mCurrentMatrix.reset();
        upDateBoundRectF();
        float scaleFactor = Math.min(getWidth() / mBoundRectF.width(), getHeight() / mBoundRectF.height());
        mInitialScaleFactor = scaleFactor;
        mTotalScaleFactor *= scaleFactor;
        //��ͼƬ�����ĵ�������ţ�����ͼƬ��С�Ϳؼ���С��Ӧ
        mCurrentMatrix.postScale(scaleFactor, scaleFactor, mBoundRectF.centerX(), mBoundRectF.centerY());
        //��ͼƬ���ĵ�ƽ�Ƶ��Ϳؼ����ĵ��غ�
        mCurrentMatrix.postTranslate(getPivotX() - mBoundRectF.centerX(), getPivotY() - mBoundRectF.centerY());
        //��ͼƬ���б任��������ͼƬ�ı߽����
        transform();
    }

    
    /**
     * �����㴥�ص�ʱ����Խ���ƽ�Ʋ���
     * ����㴥�ص�ʱ�򣺿��Խ���ͼƬ�����š���ת
     * ACTION_DOWN�������ƽ�ơ�������ת����������
     * ACTION_POINTER_DOWN�������ָ����Ϊ2,��ǲ���ƽ�ơ�����ת��������
     * ��¼ƽ�ƿ�ʼʱ����ָ���е㡢��ֻ��ָ�γɵ���������ֻ��ָ��ľ���
     * ACTION_MOVE������ƽ�ơ���ת�����ŵĲ�����
     * ACTION_POINTER_UP����һֻ��ָ̧���ʱ������ͼƬ������ת���������ţ�����ƽ��
     *
     * @param event ����¼�
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            //���㴥�أ�����ͼƬ����ƽ�ơ�������ת������
            case MotionEvent.ACTION_DOWN:
                mCanTranslate = true;
                mCanRotate = false;
                mCanScale = false;
                //��¼���㴥�ص���һ�����������
                mLastSinglePoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            	mAnimator.cancel();
                //��㴥�أ�����ͼƬ����ƽ��
                mCanTranslate = false;
                //����ָ����Ϊ������ʱ������ͼƬ�ܹ���ת������
                if (event.getPointerCount() == 2) {
                    mCanRotate = true;
                    mCanScale = true;
                    //��¼����ָ���е�
                    PointF pointF = midPoint(event);
                    //��¼��ʼ����ǰ����ָ�е������
                    mLastMidPoint.set(pointF.x, pointF.y);
                    //��¼��ʼ����ǰ������ָ֮��ľ���
                    mLastDist = distance(event);
                    //�����������Ա��ڼ���Ƕ�
                    mLastVector.set(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //�ж��ܷ�ƽ�Ʋ���
                if (mCanTranslate) {
                    float dx = event.getX() - mLastSinglePoint.x;
                    float dy = event.getY() - mLastSinglePoint.y;
                    //ƽ�Ʋ���
                    translation(dx, dy);
                    //������һ�����������
                    mLastSinglePoint.set(event.getX(), event.getY());
                }
                //�ж��ܷ����Ų���
                if (mCanScale) {
                    float scaleFactor = distance(event) / mLastDist;
                    scale(scaleFactor);
                    //����mLastDist�����´������ڴ˻����Ͻ���
                    mLastDist = distance(event);
                }
                //�ж��ܷ���ת����
                if (mCanRotate) {
                    //��ǰ��ֻ��ָ���ɵ�����
                    PointF vector = new PointF(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
                    //���㱾����������һ������֮��ļн�
                    float degree = calculateDeltaDegree(mLastVector, vector);
                    rotation(degree);
                    //����mLastVector,�Ա��´���ת������ת���ĽǶ�
                    mLastVector.set(vector.x, vector.y);
                }
                //ͼ��任
                transform();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //����ֻ��ָ��һֻ̧���ʱ������ͼƬ�������ź�ѡ���ܹ�����ƽ��
                if (event.getPointerCount() == 2) {
                    mCanScale = false;
                    mCanRotate = false;
                    mCanTranslate = true;
                    //������ת������ʹ�õ����е�����
                    mLastMidPoint.set(0f, 0f);
                    //������ֻ��ָ�ľ���
                    mLastDist = 0f;
                    //������ֻ��ָ�γɵ�����
                    mLastVector.set(0f, 0f);
                }
                //��ÿ�ʼ����֮ǰ�ľ���
                mCurrentMatrix.getValues(mBeginMatrixValues);
                //���Żص�
                backScale();
                upDateBoundRectF();
                //��ת�ص�
                backRotation();
                upDateBoundRectF();
                //��ö�������֮��ľ���
                mCurrentMatrix.getValues(mEndMatrixValues);
                mAnimator.start();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                backTranslation();
                upDateBoundRectF();
                mLastSinglePoint.set(0f, 0f);
                mCanTranslate = false;
                mCanScale = false;
                mCanRotate = false;
                break;
        }
        return true;
    }
    
    protected void translation(float dx, float dy) {
        //���ͼƬ�߽��ƽ���Ƿ񳬹��ؼ��ı߽�
    	int w = getWidth();
    	int h = getHeight();
        if (mBoundRectF.left + dx > getWidth() - 20 || mBoundRectF.right + dx < 20
                || mBoundRectF.top + dy > getHeight() - 20 || mBoundRectF.bottom + dy < 20) {
            return;
        }
        mCurrentMatrix.postTranslate(dx, dy);
    }
    

    /**
     * ����������ָͷ֮������ĵ��λ��
     * x = (x1+x2)/2;
     * y = (y1+y2)/2;
     *
     * @param event �����¼�
     * @return �������ĵ������
     */
    private PointF midPoint(MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        return new PointF(x, y);
    }


    /**
     * ����������ָ��ľ���
     *
     * @param event �����¼�
     * @return �Ż�������ָ֮��ľ���
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//�������빫ʽ
    }
    
    
    /**
     * ͼ�����Ų���
     *
     * @param scaleFactor ���ű�������
     */
    protected void scale(float scaleFactor) {
        //�۳˵õ��ܵĵ���������
        mTotalScaleFactor *= scaleFactor;
        mCurrentMatrix.postScale(scaleFactor, scaleFactor, mBoundRectF.centerX(), mBoundRectF.centerY());
    }
    
    
    /**
     * ͼ��任�����±߽����
     */
    protected void transform() {
        setImageMatrix(mCurrentMatrix);
    	//setImageBitmap(mCurrentMatrix);
        upDateBoundRectF();
    }
    
    
    /**
     * ���¾��α߽�
     */
    private void upDateBoundRectF() {
        if (this.getDrawable() != null) {
            mBoundRectF.set(getDrawable().getBounds());
            mCurrentMatrix.mapRect(mBoundRectF);
        }
    }
    
    /**
     * ������������֮��ļн�
     *
     * @param lastVector ��һ����ֻ��ָ�γɵ�����
     * @param vector     ������ֻ��ָ�γɵ�����
     * @return ������ָ��ת���ĽǶ�
     */
    private float calculateDeltaDegree(PointF lastVector, PointF vector) {
        float lastDegree = (float) Math.atan2(lastVector.y, lastVector.x);
        float degree = (float) Math.atan2(vector.y, vector.x);
        float deltaDegree = degree - lastDegree;
        return (float) Math.toDegrees(deltaDegree);
    }
    
    /**
     * ��ת����
     *
     * @param degree ��ת�Ƕ�
     */
    protected void rotation(float degree) {
        //��ת�任
        mCurrentMatrix.postRotate(degree, mBoundRectF.centerX(), mBoundRectF.centerY());

    }
    
    /**
     * ��ת�ص�
     */
    protected void backRotation() {
        //x�᷽��ĵ�λ�������ڼ������У��Ƕ�Ϊ0
        float[] x_vector = new float[]{1.0f, 0.0f};
        //ӳ������
        mCurrentMatrix.mapVectors(x_vector);
        //����x�᷽��ĵ�λ����ת���ĽǶ�
        float totalDegree = (float) Math.toDegrees((float) Math.atan2(x_vector[1], x_vector[0]));
        float degree = totalDegree;
        degree = Math.abs(degree);
        //�����ת�Ƕȵľ���ֵ��45-135��֮�䣬������ת�Ƕ�Ϊ90��
        if (degree > 45 && degree <= 135) {
            degree = 90;
        } //�����ת�Ƕȵľ���ֵ��135-225֮�䣬������ת�Ƕ�Ϊ180��
        else if (degree > 135 && degree <= 225) {
            degree = 180;
        } //�����ת�Ƕȵľ���ֵ��225-315֮�䣬������ת�Ƕ�Ϊ270��
        else if (degree > 225 && degree <= 315) {
            degree = 270;
        }//�����ת�Ƕȵľ���ֵ��315-360֮�䣬������ת�Ƕ�Ϊ0��
        else {
            degree = 0;
        }
        degree = totalDegree < 0 ? -degree : degree;
        //degree-totalDegree����ﵽ90�ı����ǣ�����Ĳ�ֵ
        mCurrentMatrix.postRotate(degree - totalDegree, mBoundRectF.centerX(), mBoundRectF.centerY());
    }

    /**
     * ���Żص�
     */
    protected void backScale() {
        float scaleFactor = 1.0f;
        //����ܵ����ű������ӱȳ�ʼ�����������ӻ�С�����лص�
        if (mTotalScaleFactor / mInitialScaleFactor < mMinScaleFactor) {
            //1�����ܵ����������ٳ˳�ʼ�����������ӣ���ûص�����������
            scaleFactor = mInitialScaleFactor / mTotalScaleFactor * mMinScaleFactor;
            //�����ܵ��������ӣ��Ա��´��ڴ����ű����Ļ����Ͻ�������
            mTotalScaleFactor = mInitialScaleFactor * mMinScaleFactor;
        }
        //����ܵ����ű������Ӵ������ֵ����ͼƬ�Ŵ������
        else if (mTotalScaleFactor / mInitialScaleFactor > mMaxScaleFactor) {
            //��Ŵ����������Ҫ�ı�������
            scaleFactor = mInitialScaleFactor / mTotalScaleFactor * mMaxScaleFactor;
            //�����ܵ��������ӣ��Ա��´��ڴ����ű����Ļ����Ͻ�������
            mTotalScaleFactor = mInitialScaleFactor * mMaxScaleFactor;
        }
        mCurrentMatrix.postScale(scaleFactor, scaleFactor, mBoundRectF.centerX(), mBoundRectF.centerY());
    }

    /**
     * ƽ�ƻص�
     * ƽ��֮���ܳ����аױߵ����
     */
    protected void backTranslation() {
        float dx = 0;
        float dy = 0;
        //�ж�ͼƬ�Ŀ���Ƿ���ڿؼ��Ŀ�ȣ�����Ҫ���б߽���ж�
        if (mBoundRectF.width() >= getWidth()) {
            //��߽��ڿؼ���Χ�ڣ�����ͼƬ��߽糬���ؼ���Χ
            if ((mBoundRectF.left > getLeft() && mBoundRectF.left <= getRight()) || mBoundRectF.left > getRight()) {
                dx = getLeft() - mBoundRectF.left;
            } //ͼƬ�ұ߽��ڿؼ���Χ��,����ͼƬ�ұ߽糬���ؼ���Χ
            else if ((mBoundRectF.right >= getLeft() && mBoundRectF.right < getRight()) || mBoundRectF.right < getLeft()) {
                dx = getRight() - mBoundRectF.right;
            }
        } //���ͼƬ���С�ڿؼ���ȣ��ƶ�ͼƬ����x����Ϳؼ�����x�����غ�
        else {
            dx = getPivotX() - mBoundRectF.centerX();
        }
        //�ж�ͼƬ�ĸ߶��Ƿ���ڿؼ��ĸ߶ȣ�����Ҫ���б߽���ж�
        if (mBoundRectF.height() >= getHeight()) {
            //ͼƬ�ϱ߽��ڿؼ���Χ�ڣ�����ͼƬ�ϱ߽糬���ؼ���Χ
            if ((mBoundRectF.top > getTop() && mBoundRectF.top <= getBottom()) || mBoundRectF.top > getBottom()) {
                dy = getTop() - mBoundRectF.top;
            } //ͼƬ�±߽��ڿؼ���Χ��,����ͼƬ�±߽糬���ؼ���Χ
            else if ((mBoundRectF.bottom < getBottom() && mBoundRectF.bottom >= getTop()) || mBoundRectF.bottom < getTop()) {
                dy = getBottom() - mBoundRectF.bottom;
            }
        } //���ͼƬ�߶�С�ڿؼ��߶ȣ��ƶ�ͼƬ����y����Ϳؼ�����y�����غ�
        else {
            dy = getPivotY() - mBoundRectF.centerY();
        }
        mCurrentMatrix.postTranslate(dx, dy);
    }



} 

