package e.dante.sts;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends DialogFragment {
    private String name;
    private String type;
    private String des;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
        type = args.getString("type");
        des = args.getString("des");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_info, container, false);
        TextView infoView = myView.findViewById(R.id.info_name_view);
        TextView typeView = myView.findViewById(R.id.info_type_view);
        TextView desView = myView.findViewById(R.id.info_des_view);


        typeView.setText(type);
        infoView.setText(name);
        desView.setText(des);
        return myView;
    }
}
