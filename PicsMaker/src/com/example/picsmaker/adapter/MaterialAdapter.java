package com.example.picsmaker.adapter;

import java.io.InputStream;
import java.util.List;

import com.example.picsmaker.R;
import com.example.picsmaker.adapter.BackgroundAdapter.ViewHolder;
import com.example.picsmaker.domain.Material;
import com.example.picsmaker.entity.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MaterialAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Material> Datas;
	private int itemViewId;
	private Context mContext;
	
	public MaterialAdapter(Context context, int itemViewID, List<Material> Datas) {
		this.mInflater = LayoutInflater.from(context);
		this.Datas = Datas;
		this.itemViewId = itemViewID;
		mContext = context;
	}
	
	public void addItems(int position, List<Material> tempList) {

		Datas.addAll(0, tempList);
	}
	
	public void addItems(List<Material> tempList) {

		Datas.addAll(tempList);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(this.itemViewId, parent, false);
			holder = new ViewHolder();

			holder.pic = (ImageView) convertView.findViewById(R.id.img_icon);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		//holder.pic.setImageResource(Datas.get(position).getDrawable());
		holder.pic.setImageBitmap(readBitMap(mContext,Datas.get(position).getDrawableId()));
		return convertView;
	}
	
	public final class ViewHolder {
		public ImageView pic;
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
