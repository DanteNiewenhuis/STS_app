package e.dante.sts.Keyword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import e.dante.sts.R;
/*
    This is the adapter that creates an expendablelist for the Keywords
 */
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
    // create the group view of the Keyword.
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Keyword keyword = (Keyword) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.keyword_group, parent, false);
        }


        TextView group = convertView.findViewById(R.id.keyword_name_view);
        group.setText(keyword.getName());

        if (isExpanded) {
            ((ImageView) convertView.findViewById(R.id.keyword_open_indicator)).setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            group.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        } else {
            ((ImageView) convertView.findViewById(R.id.keyword_open_indicator)).setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            group.setTextColor(context.getResources().getColor(R.color.TextOnSecondary));
        }


        return convertView;
    }

    @Override
    // make the view that is showed when the item is expanded
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
