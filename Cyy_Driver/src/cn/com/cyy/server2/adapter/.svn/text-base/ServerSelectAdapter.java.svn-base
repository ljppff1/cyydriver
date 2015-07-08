package cn.com.cyy.server2.adapter;

import java.util.List;
import java.util.Map;

import cn.com.cyy.server2.model.ServerModel;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * 服务类别的选择
 * @author hurenji
 */

public class ServerSelectAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<List<ServerModel>> listP ;
	private Map<String,List<ServerModel>> mapC;
	
	
	public ServerSelectAdapter(Context context , List<List<ServerModel>> listP , Map<String,List<ServerModel>> mapC){
		this.context = context;
		this.listP = listP;
		this.mapC = mapC;
		
	}
	
	
	@Override
	public int getGroupCount() {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
