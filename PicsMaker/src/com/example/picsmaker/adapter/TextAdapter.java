package com.example.picsmaker.adapter;

import java.util.List;

import com.example.picsmaker.R;
import com.example.picsmaker.adapter.MaterialAdapter.ViewHolder;
import com.example.picsmaker.domain.CustomEditText;
import com.example.picsmaker.domain.Material;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TextAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<CustomEditText> Datas;
	private int itemViewId;
	private Context mContext;
	
	public TextAdapter(Context context, int itemViewID, List<CustomEditText> Datas) {
		this.mInflater = LayoutInflater.from(context);
		this.Datas = Datas;
		this.itemViewId = itemViewID;
		mContext = context;
	}
	
	public void addItems(int position, List<CustomEditText> tempList) {

		Datas.addAll(0, tempList);
	}
	
	public void addItems(List<CustomEditText> tempList) {

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

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(this.itemViewId, parent, false);
			holder = new ViewHolder();

			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text.setText("Picture Maker");
		holder.text.setTypeface(Datas.get(position).getFontFamily());
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView text;
	}

}
