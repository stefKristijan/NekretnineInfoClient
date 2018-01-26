package com.kstefancic.potresnirizik.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Sector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 28.11.2017..
 */

public class PurposeExpandableListAdapter extends BaseExpandableListAdapter {

    private List<String> sectorHeaders;
    private HashMap<String, List<String>> purposeChildren;
    private Context context;


    public PurposeExpandableListAdapter(Context context, List<Sector> sectors, List<Purpose> purposes) {
        this.context = context;
        this.sectorHeaders = new ArrayList<>();
        this.purposeChildren = new HashMap<>();
        prepareData(sectors,purposes);
    }

    private void prepareData(List<Sector> sectors, List<Purpose> purposes) {
        List<String> tempList = new ArrayList<>();
        String header = "";
        for (int i=0; i<purposes.size();i++) {
            Purpose purpose = purposes.get(i);
            if (header.isEmpty()) {
                Log.d("IF-ELVA", header + " " + purpose + " " + tempList.size());
                header = purpose.getSector().getSectorName();
                sectorHeaders.add(header);
                tempList.add(purpose.getPurpose());
            } else if (header.equals(purpose.getSector().getSectorName())) {
                Log.d("ELSEIF1-ELVA", header + " " + purpose + " " + tempList.size());
                tempList.add(purpose.getPurpose());
                if(i==purposes.size()-1){
                    this.purposeChildren.put(header, new ArrayList<>(tempList));
                }
            } else if (!header.equals(purpose.getSector().getSectorName())) {
                Log.d("ELSEIF2-ELVA", header + " " + purpose + " " + tempList.size());
                this.purposeChildren.put(header, new ArrayList<>(tempList));
                Log.d("HASHAMAP", purposeChildren.toString());
                tempList.clear();
                header = purpose.getSector().getSectorName();
                sectorHeaders.add(header);
                tempList.add(purpose.getPurpose());
            }
        }
    }

    @Override
    public int getGroupCount() {
        return sectorHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.purposeChildren.get(this.sectorHeaders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.sectorHeaders.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.purposeChildren.get(this.sectorHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
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
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(groupPosition);
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.elv_header_item,null);

        }

        TextView tvHeader = view.findViewById(R.id.tvElvHeaderItem);
        tvHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(groupPosition,childPosition);

        if(view==null){
            LayoutInflater inlater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inlater.inflate(R.layout.elv_child_item,null);
        }

        TextView tvChild = view.findViewById(R.id.tvElvChildItem);

        tvChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
