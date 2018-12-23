 
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
    //定义相关变量,依次是妹子显示位置的X,Y坐标  
    public float bitmapX;  
    public float bitmapY;
    private int drawable_id;
    /*
     * 单只手指按下的时候是可以进行平移操作的，多点触控的时候不能进行平移操作。所以维护一个boolean变量mCanTranslate，在ACTION_DOWN的时候置位true，ACTION_POINTER_DOWN置为false
     */
    private boolean mCanTranslate = false;
    private boolean mCanRotate = false;
    private boolean mCanScale = false;
    private PointF mLastSinglePoint = new PointF(),mLastMidPoint = new PointF();
    private float mLastDist;
    //图片的边界矩形
    private RectF mBoundRectF = new RectF();
    //用于平移、缩放、旋转变换图片的矩阵
    private Matrix mCurrentMatrix = new Matrix();
    //记录onLayout之后的初始化缩放因子
    private float mInitialScaleFactor = 1.0f;
    //记录图片总的缩放因子
    private float mTotalScaleFactor = 1.0f;
    //动画开始时的矩阵值
    private float[] mBeginMatrixValues = new float[9];
    //动画结束时的矩阵值
    private float[] mEndMatrixValues = new float[9];
    //动画过程中的矩阵值
    private float[] mTransformMatrixValues = new float[9];
    //属性动画
    private ValueAnimator mAnimator = ValueAnimator.ofFloat(0f, 1f);
    //记录上一次两只手指构成的一个向量
    private PointF mLastVector = new PointF();
    //最大缩放比例因子
    private float mMaxScaleFactor = 3.0f;
    //最小缩放比例因子
    private float mMinScaleFactor = 0.8f;
    	
  
    public Material(Context context,int _id) {  
        super(context);  
        //设置妹子的起始坐标  
        //bitmapX = 0;  
        //bitmapY = 200;  
        this.drawable_id = _id;
        this.setImageResource(_id);
        //设置刻度类型
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
 
    //重写View类的onDraw()方法  
    public int getDrawableId() {
    	return this.drawable_id;
    }
    
    /**
     * 初始化放置图片
     * 将图片缩放和控件大小一致并移动图片中心和控件的中心重合
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
     * 初始化图片的大小与位置
     */
    private void initImagePositionAndSize() {
        mCurrentMatrix.reset();
        upDateBoundRectF();
        float scaleFactor = Math.min(getWidth() / mBoundRectF.width(), getHeight() / mBoundRectF.height());
        mInitialScaleFactor = scaleFactor;
        mTotalScaleFactor *= scaleFactor;
        //以图片的中心点进行缩放，缩放图片大小和控件大小适应
        mCurrentMatrix.postScale(scaleFactor, scaleFactor, mBoundRectF.centerX(), mBoundRectF.centerY());
        //将图片中心点平移到和控件中心点重合
        mCurrentMatrix.postTranslate(getPivotX() - mBoundRectF.centerX(), getPivotY() - mBoundRectF.centerY());
        //对图片进行变换，并更新图片的边界矩形
        transform();
    }

    
    /**
     * 当单点触控的时候可以进行平移操作
     * 当多点触控的时候：可以进行图片的缩放、旋转
     * ACTION_DOWN：标记能平移、不能旋转、不能缩放
     * ACTION_POINTER_DOWN：如果手指个数为2,标记不能平移、能旋转、能缩放
     * 记录平移开始时两手指的中点、两只手指形成的向量、两只手指间的距离
     * ACTION_MOVE：进行平移、旋转、缩放的操作。
     * ACTION_POINTER_UP：有一只手指抬起的时候，设置图片不能旋转、不能缩放，可以平移
     *
     * @param event 点击事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            //单点触控，设置图片可以平移、不能旋转和缩放
            case MotionEvent.ACTION_DOWN:
                mCanTranslate = true;
                mCanRotate = false;
                mCanScale = false;
                //记录单点触控的上一个单点的坐标
                mLastSinglePoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            	mAnimator.cancel();
                //多点触控，设置图片不能平移
                mCanTranslate = false;
                //当手指个数为两个的时候，设置图片能够旋转和缩放
                if (event.getPointerCount() == 2) {
                    mCanRotate = true;
                    mCanScale = true;
                    //记录两手指的中点
                    PointF pointF = midPoint(event);
                    //记录开始滑动前两手指中点的坐标
                    mLastMidPoint.set(pointF.x, pointF.y);
                    //记录开始滑动前两个手指之间的距离
                    mLastDist = distance(event);
                    //设置向量，以便于计算角度
                    mLastVector.set(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //判断能否平移操作
                if (mCanTranslate) {
                    float dx = event.getX() - mLastSinglePoint.x;
                    float dy = event.getY() - mLastSinglePoint.y;
                    //平移操作
                    translation(dx, dy);
                    //重置上一个单点的坐标
                    mLastSinglePoint.set(event.getX(), event.getY());
                }
                //判断能否缩放操作
                if (mCanScale) {
                    float scaleFactor = distance(event) / mLastDist;
                    scale(scaleFactor);
                    //重置mLastDist，让下次缩放在此基础上进行
                    mLastDist = distance(event);
                }
                //判断能否旋转操作
                if (mCanRotate) {
                    //当前两只手指构成的向量
                    PointF vector = new PointF(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
                    //计算本次向量和上一次向量之间的夹角
                    float degree = calculateDeltaDegree(mLastVector, vector);
                    rotation(degree);
                    //更新mLastVector,以便下次旋转计算旋转过的角度
                    mLastVector.set(vector.x, vector.y);
                }
                //图像变换
                transform();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //当两只手指有一只抬起的时候，设置图片不能缩放和选择，能够进行平移
                if (event.getPointerCount() == 2) {
                    mCanScale = false;
                    mCanRotate = false;
                    mCanTranslate = true;
                    //重置旋转和缩放使用到的中点坐标
                    mLastMidPoint.set(0f, 0f);
                    //重置两只手指的距离
                    mLastDist = 0f;
                    //重置两只手指形成的向量
                    mLastVector.set(0f, 0f);
                }
                //获得开始动画之前的矩阵
                mCurrentMatrix.getValues(mBeginMatrixValues);
                //缩放回弹
                backScale();
                upDateBoundRectF();
                //旋转回弹
                backRotation();
                upDateBoundRectF();
                //获得动画结束之后的矩阵
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
        //检查图片边界的平移是否超过控件的边界
    	int w = getWidth();
    	int h = getHeight();
        if (mBoundRectF.left + dx > getWidth() - 20 || mBoundRectF.right + dx < 20
                || mBoundRectF.top + dy > getHeight() - 20 || mBoundRectF.bottom + dy < 20) {
            return;
        }
        mCurrentMatrix.postTranslate(dx, dy);
    }
    

    /**
     * 计算两个手指头之间的中心点的位置
     * x = (x1+x2)/2;
     * y = (y1+y2)/2;
     *
     * @param event 触摸事件
     * @return 返回中心点的坐标
     */
    private PointF midPoint(MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        return new PointF(x, y);
    }


    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//两点间距离公式
    }
    
    
    /**
     * 图像缩放操作
     *
     * @param scaleFactor 缩放比例因子
     */
    protected void scale(float scaleFactor) {
        //累乘得到总的的缩放因子
        mTotalScaleFactor *= scaleFactor;
        mCurrentMatrix.postScale(scaleFactor, scaleFactor, mBoundRectF.centerX(), mBoundRectF.centerY());
    }
    
    
    /**
     * 图像变换并更新边界矩阵
     */
    protected void transform() {
        setImageMatrix(mCurrentMatrix);
    	//setImageBitmap(mCurrentMatrix);
        upDateBoundRectF();
    }
    
    
    /**
     * 更新矩形边界
     */
    private void upDateBoundRectF() {
        if (this.getDrawable() != null) {
            mBoundRectF.set(getDrawable().getBounds());
            mCurrentMatrix.mapRect(mBoundRectF);
        }
    }
    
    /**
     * 计算两个向量之间的夹角
     *
     * @param lastVector 上一次两只手指形成的向量
     * @param vector     本次两只手指形成的向量
     * @return 返回手指旋转过的角度
     */
    private float calculateDeltaDegree(PointF lastVector, PointF vector) {
        float lastDegree = (float) Math.atan2(lastVector.y, lastVector.x);
        float degree = (float) Math.atan2(vector.y, vector.x);
        float deltaDegree = degree - lastDegree;
        return (float) Math.toDegrees(deltaDegree);
    }
    
    /**
     * 旋转操作
     *
     * @param degree 旋转角度
     */
    protected void rotation(float degree) {
        //旋转变换
        mCurrentMatrix.postRotate(degree, mBoundRectF.centerX(), mBoundRectF.centerY());

    }
    
    /**
     * 旋转回弹
     */
    protected void backRotation() {
        //x轴方向的单位向量，在极坐标中，角度为0
        float[] x_vector = new float[]{1.0f, 0.0f};
        //映射向量
        mCurrentMatrix.mapVectors(x_vector);
        //计算x轴方向的单位向量转过的角度
        float totalDegree = (float) Math.toDegrees((float) Math.atan2(x_vector[1], x_vector[0]));
        float degree = totalDegree;
        degree = Math.abs(degree);
        //如果旋转角度的绝对值在45-135度之间，让其旋转角度为90度
        if (degree > 45 && degree <= 135) {
            degree = 90;
        } //如果旋转角度的绝对值在135-225之间，让其旋转角度为180度
        else if (degree > 135 && degree <= 225) {
            degree = 180;
        } //如果旋转角度的绝对值在225-315之间，让其旋转角度为270度
        else if (degree > 225 && degree <= 315) {
            degree = 270;
        }//如果旋转角度的绝对值在315-360之间，让其旋转角度为0度
        else {
            degree = 0;
        }
        degree = totalDegree < 0 ? -degree : degree;
        //degree-totalDegree计算达到90的倍数角，所需的差值
        mCurrentMatrix.postRotate(degree - totalDegree, mBoundRectF.centerX(), mBoundRectF.centerY());
    }

    /**
     * 缩放回弹
     */
    protected void backScale() {
        float scaleFactor = 1.0f;
        //如果总的缩放比例因子比初始化的缩放因子还小，进行回弹
        if (mTotalScaleFactor / mInitialScaleFactor < mMinScaleFactor) {
            //1除以总的缩放因子再乘初始化的缩放因子，求得回弹的缩放因子
            scaleFactor = mInitialScaleFactor / mTotalScaleFactor * mMinScaleFactor;
            //更新总的缩放因子，以便下次在此缩放比例的基础上进行缩放
            mTotalScaleFactor = mInitialScaleFactor * mMinScaleFactor;
        }
        //如果总的缩放比例因子大于最大值，让图片放大到最大倍数
        else if (mTotalScaleFactor / mInitialScaleFactor > mMaxScaleFactor) {
            //求放大到最大倍数，需要的比例因子
            scaleFactor = mInitialScaleFactor / mTotalScaleFactor * mMaxScaleFactor;
            //更新总的缩放因子，以便下次在此缩放比例的基础上进行缩放
            mTotalScaleFactor = mInitialScaleFactor * mMaxScaleFactor;
        }
        mCurrentMatrix.postScale(scaleFactor, scaleFactor, mBoundRectF.centerX(), mBoundRectF.centerY());
    }

    /**
     * 平移回弹
     * 平移之后不能出现有白边的情况
     */
    protected void backTranslation() {
        float dx = 0;
        float dy = 0;
        //判断图片的宽度是否大于控件的宽度，若是要进行边界的判断
        if (mBoundRectF.width() >= getWidth()) {
            //左边界在控件范围内，或者图片左边界超出控件范围
            if ((mBoundRectF.left > getLeft() && mBoundRectF.left <= getRight()) || mBoundRectF.left > getRight()) {
                dx = getLeft() - mBoundRectF.left;
            } //图片右边界在控件范围内,或者图片右边界超出控件范围
            else if ((mBoundRectF.right >= getLeft() && mBoundRectF.right < getRight()) || mBoundRectF.right < getLeft()) {
                dx = getRight() - mBoundRectF.right;
            }
        } //如果图片宽度小于控件宽度，移动图片中心x坐标和控件中心x坐标重合
        else {
            dx = getPivotX() - mBoundRectF.centerX();
        }
        //判断图片的高度是否大于控件的高度，若是要进行边界的判断
        if (mBoundRectF.height() >= getHeight()) {
            //图片上边界在控件范围内，或者图片上边界超出控件范围
            if ((mBoundRectF.top > getTop() && mBoundRectF.top <= getBottom()) || mBoundRectF.top > getBottom()) {
                dy = getTop() - mBoundRectF.top;
            } //图片下边界在控件范围内,或者图片下边界超出控件范围
            else if ((mBoundRectF.bottom < getBottom() && mBoundRectF.bottom >= getTop()) || mBoundRectF.bottom < getTop()) {
                dy = getBottom() - mBoundRectF.bottom;
            }
        } //如果图片高度小于控件高度，移动图片中心y坐标和控件中心y坐标重合
        else {
            dy = getPivotY() - mBoundRectF.centerY();
        }
        mCurrentMatrix.postTranslate(dx, dy);
    }



} 
 

