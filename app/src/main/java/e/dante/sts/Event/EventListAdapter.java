package e.dante.sts.Event;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import e.dante.sts.GlobalFunctions;
import e.dante.sts.R;

public class EventListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private ArrayList<Event> dataChild;
    private GlobalFunctions gFunctions;

    public EventListAdapter(Context context, FragmentManager fragmentManager, ArrayList<Event> dataChild) {
        this.context = context;
        this.dataChild = dataChild;
        this.gFunctions = new GlobalFunctions(fragmentManager);
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
        if (event.getAct() == 4) {
            act.setText("Any Act");
        }
        else {
            act.setText("Act " + Integer.toString(event.getAct()));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Event event = (Event) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.event_child, parent, false);
        }

        TextView options = convertView.findViewById(R.id.event_options_view);
//        options.setText(gFunctions.makeBold(event.getOptions()
//                .replaceAll(" \\[", "\n["), "[", "]"));

        //TODO make clickable
        String text = gFunctions.parseHTML(event.getOptions());
        options.setText(gFunctions.fromHtml(text));
        options.setMovementMethod(LinkMovementMethod.getInstance());


        return convertView;
    }
}
