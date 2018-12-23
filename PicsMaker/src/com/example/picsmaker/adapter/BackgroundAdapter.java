package com.example.picsmaker.adapter;

import java.io.InputStream;
import java.util.List;

import com.example.picsmaker.R;
import com.example.picsmaker.R.drawable;
import com.example.picsmaker.entity.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BackgroundAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<background> Datas;
	private int itemViewId;
	private Context mContext;
	
	public BackgroundAdapter(Context context, int itemViewID, List<background> Datas) {
		this.mInflater = LayoutInflater.from(context);
		this.Datas = Datas;
		this.itemViewId = itemViewID;
		mContext = context;
	}
	
	public void addItems(int position, List<background> tempList) {

		Datas.addAll(0, tempList);
	}
	
	public void addItems(List<background> tempList) {

		Datas.addAll(tempList);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (arg1 == null) {
			arg1 = mInflater.inflate(this.itemViewId, arg2, false);
			holder = new ViewHolder();

			holder.info = (TextView) arg1.findViewById(R.id.txt_name);
			holder.pic = (ImageView) arg1.findViewById(R.id.img_icon);
			//recycleImageView(holder.pic);
			arg1.setTag(holder);
		} else {

			holder = (ViewHolder) arg1.getTag();
		}

		
		holder.info.setText((String) Datas.get(arg0).getInfo());
		holder.pic.setImageBitmap(readBitMap(mContext,Datas.get(arg0).getId()));
		//holder.pic.setImageResource(Datas.get(arg0).getId());
		return arg1;
		
	}
	
	public final class ViewHolder {
		public TextView info;
		public ImageView pic;
	}
	
	public static void recycleImageView(View view){
        if(view==null) return;
        if(view instanceof ImageView){
            Drawable drawable=((ImageView) view).getDrawable();
            if(drawable instanceof BitmapDrawable){
                Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()){
                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                    //log("have recycled ImageView Bitmap");
                    bmp=null;
                }
            }
        }
	}
	
	public static Bitmap readBitMap(Context context,int image_id) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(image_id);
		return BitmapFactory.decodeStream(is,null,opt);
	}
	
		

}
