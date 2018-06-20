package e.dante.sts.Event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import e.dante.sts.R;

public class EventListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Event> dataChild;

    public EventListAdapter(Context context, ArrayList<Event> dataChild) {
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
        Event event = (Event) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.event_group, parent, false);
        }

        TextView name = convertView.findViewById(R.id.event_name_view);
        TextView act = convertView.findViewById(R.id.event_act_view);
        name.setText(event.getName());
        act.setText("Act " + Integer.toString(event.getAct()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Event event = (Event) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.event_child, parent, false);
        }

        ListView options = convertView.findViewById(R.id.options_list_view);
//        options.setAdapter(new OptionsAdapter(this.context, R.layout.item_option, event.getOptions()));

        return convertView;
    }

    private class OptionsAdapter extends ArrayAdapter<String> {
        private List<String> options;
        private int resource;

        public OptionsAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.options = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            String option = options.get(position);

//            TextView nameView = convertView.findViewById(R.id.option_title_view);
//            TextView desView = convertView.findViewById(R.id.option_description_view);

            int index = option.indexOf("]");

            String name = "";
            String des = "";
            for (int j = 1; j < option.length(); j++) {
                if (j < index) {
                    name += option.charAt(j);
                }
                if (j > index + 1) {
                    des += option.charAt(j);
                }
            }

//            nameView.setText(name + ": ");
//            desView.setText(des);

            return convertView;
        }
    }
}
