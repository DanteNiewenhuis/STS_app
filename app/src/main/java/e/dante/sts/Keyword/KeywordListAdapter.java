package e.dante.sts.Keyword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import e.dante.sts.R;

public class KeywordListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Keyword> dataChild;

    public KeywordListAdapter(Context context, ArrayList<Keyword> dataChild) {
        this.context = context;
        this.dataChild = dataChild;
    }

    @Override
    public int getGroupCount() {
        return this.dataChild.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.dataChild.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.dataChild.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Keyword keyword = (Keyword) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.keyword_group, parent, false);
        }

        TextView group = convertView.findViewById(R.id.group_view);
        group.setText(keyword.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Keyword keyword = (Keyword) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.keyword_child, parent, false);
        }

        TextView group = convertView.findViewById(R.id.child_view);
        group.setText(keyword.getDescription());

        return convertView;
    }
}
