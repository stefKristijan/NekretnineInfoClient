package com.kstefancic.potresnirizik.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Construction;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.SupportingSystem;
import com.kstefancic.potresnirizik.helper.ConstructionExpandableListAdapter;
import com.kstefancic.potresnirizik.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ExpandableConstructionActivity extends Activity {

    public static final String CONSTRUCTION = "construction";
    Button btnCancel;
    private ExpandableListView expandableListViewConstruction;
    private List<SupportingSystem> supportingSystems;
    private List<Construction> constructions;
    private ConstructionExpandableListAdapter constructionExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_construction);
        this.setFinishOnTouchOutside(true);

        this.supportingSystems = DBHelper.getInstance(this).getAllSupportingSystems();
        this.constructions = DBHelper.getInstance(this).getAllConstructionSystems();

        setUpUI();
    }

    private void setUpUI() {
        setUpExpandableListView();
        this.btnCancel = findViewById(R.id.dialogConstruction_btnCancel);
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpExpandableListView() {
        this.expandableListViewConstruction = findViewById(R.id.dialogConstruction_elvConstruction);
        setUpConstructionELV();
        this.expandableListViewConstruction.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                SupportingSystem supportingSystem = supportingSystems.get(groupPosition);
                List<Construction> constructionGroup = new ArrayList<>();

                for(Construction construction : constructions){
                    if(supportingSystem.getSupportingSystem().equals(construction.getSupportingSystem().getSupportingSystem())){
                        constructionGroup.add(construction);
                    }
                }
                Intent constructionIntent = new Intent();
                constructionIntent.putExtra(CONSTRUCTION,  constructionGroup.get(childPosition));
                setResult(RESULT_OK, constructionIntent);
                finish();
                return false;
            }
        });

    }

    private void setUpConstructionELV() {
        this.constructionExpandableListAdapter = new ConstructionExpandableListAdapter(this, supportingSystems, constructions);
        this.expandableListViewConstruction.setAdapter(constructionExpandableListAdapter);
    }
}
