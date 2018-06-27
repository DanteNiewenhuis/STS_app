package e.dante.sts.Combos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import e.dante.sts.R;

public class ComboListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Opinion> dataChild;

    public ComboListAdapter(Context context, ArrayList<Opinion> dataChild) {
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
        Opinion opinion = (Opinion) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.combo_child, parent, false);
        }
        TextView nameView = convertView.findViewById(R.id.combo_child_name_view);
        nameView.setText(opinion.getUserId());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Opinion opinion = (Opinion) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.combo_group, parent, false);
        }
//        TextView noteView = convertView.findViewById(R.id.combo_group_notes_view);
//        noteView.setText(opinion.getNote());

        return convertView;
    }
}
