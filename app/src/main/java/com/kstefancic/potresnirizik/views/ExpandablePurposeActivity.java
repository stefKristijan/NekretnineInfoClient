package com.kstefancic.potresnirizik.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Sector;
import com.kstefancic.potresnirizik.helper.DBHelper;
import com.kstefancic.potresnirizik.helper.PurposeExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExpandablePurposeActivity extends Activity {

    public static final String PURPOSE = "purpose";
    Button btnCancel;
    private ExpandableListView expandableListViewPurpose;
    private List<Sector> sectors;
    private List<Purpose> purposes;
    private PurposeExpandableListAdapter purposeExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_purpose);
        this.setFinishOnTouchOutside(true);

        this.sectors = DBHelper.getInstance(this).getAllSectors();
        this.purposes = DBHelper.getInstance(this).getAllPurposes();

        setUpUI();
    }

    private void setUpUI() {
        setUpExpandableListView();
        this.btnCancel = findViewById(R.id.dialogPurpose_btnCancel);
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpExpandableListView() {
        this.expandableListViewPurpose = findViewById(R.id.dialogPurpose_elvPurpose);
        setUpPurposeELV();
        this.expandableListViewPurpose.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Sector sector = sectors.get(groupPosition);
                List<Purpose> purposeGroup = new ArrayList<>();

                for(Purpose purpose : purposes){
                    if(sector.getSectorName().equals(purpose.getSector().getSectorName())){
                        purposeGroup.add(purpose);
                    }
                }
                Intent purposeIntent = new Intent();
                purposeIntent.putExtra(PURPOSE,  purposeGroup.get(childPosition));
                setResult(RESULT_OK, purposeIntent);
                finish();
                return false;
            }
        });

    }

    private void setUpPurposeELV() {
        this.purposeExpandableListAdapter= new PurposeExpandableListAdapter(this, sectors, purposes);
        this.expandableListViewPurpose.setAdapter(purposeExpandableListAdapter);
    }
}
