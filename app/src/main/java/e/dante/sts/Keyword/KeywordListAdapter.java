package e.dante.sts.Keyword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import e.dante.sts.R;

public class KeywordListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> dataHeaders;
    private HashMap<String, String> dataChild;

    public KeywordListAdapter(Context context, List<String> dataHeaders, HashMap<String, String> dataChild) {
        this.context = context;
        this.dataHeaders = dataHeaders;
        this.dataChild = dataChild;
    }

    @Override
    public int getGroupCount() {
        return this.dataHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.dataHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.dataChild.get(this.dataHeaders.get(groupPosition));
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
        String title = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.keyword_group, parent, false);
        }

        TextView group = convertView.findViewById(R.id.group_view);
        group.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String text = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.keyword_child, parent, false);
        }

        TextView group = convertView.findViewById(R.id.child_view);
        group.setText(text);

        return convertView;
    }
}
