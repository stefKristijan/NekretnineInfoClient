package com.kstefancic.potresnirizik.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Construction;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.SupportingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 4.2.2018..
 */

public class ConstructionExpandableListAdapter extends BaseExpandableListAdapter{
    private List<String> supportingHeaders;
    private HashMap<String, List<String>> constructionChildren;
    private Context context;


    public ConstructionExpandableListAdapter(Context context, List<SupportingSystem> supportingSystems, List<Construction> constructions) {
        this.context = context;
        this.supportingHeaders = new ArrayList<>();
        this.constructionChildren = new HashMap<>();
        prepareData(supportingSystems,constructions);
    }

    private void prepareData(List<SupportingSystem> supportingSystems, List<Construction> constructions) {
        List<String> tempList = new ArrayList<>();
        String header = "";
        for (int i=0; i<constructions.size();i++) {
            Construction construction = constructions.get(i);
            if (header.isEmpty()) {
                header = construction.getSupportingSystem().getSupportingSystem();
                supportingHeaders.add(header);
                tempList.add(construction.getConstruction());
            } else if (header.equals(construction.getSupportingSystem().getSupportingSystem())) {
                tempList.add(construction.getConstruction());
                if(i==constructions.size()-1){
                    this.constructionChildren.put(header, new ArrayList<>(tempList));
                }
            } else if (!header.equals(construction.getSupportingSystem().getSupportingSystem())) {
                this.constructionChildren.put(header, new ArrayList<>(tempList));
                tempList.clear();
                header = construction.getSupportingSystem().getSupportingSystem();
                supportingHeaders.add(header);
                tempList.add(construction.getConstruction());
            }
        }
    }

    @Override
    public int getGroupCount() {
        return supportingHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.constructionChildren.get(this.supportingHeaders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.supportingHeaders.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.constructionChildren.get(this.supportingHeaders.get(groupPosition)).get(childPosition);
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
