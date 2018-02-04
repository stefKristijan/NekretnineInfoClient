package com.kstefancic.potresnirizik.helper;

import android.content.Context;
import android.hardware.Camera;

import com.kstefancic.potresnirizik.api.model.Building;

/**
 * Created by user on 1.2.2018..
 */

public class AreaCalculator {

    private static AreaCalculator mAreaCalculator = null;

    public AreaCalculator(){
    }

    public static synchronized AreaCalculator getInstance (){

        if(mAreaCalculator==null){
            mAreaCalculator = new AreaCalculator();
        }
        return mAreaCalculator;
    }

    public double calculateBrutoArea(Building building){
        return (
                building.getAtticBrutoArea()+building.getBasementBrutoArea()+
                (building.getNumberOfFloors()*calculateFloorArea(building))
        );
    }

    public double calculateFloorArea(Building building){
        switch (building.getPurpose().getPurpose()){
            case "Stambena zgrada":
                return building.getResidentialBrutoArea();

            case "Stambeno-poslovna zgrada":
                return (building.getResidentialBrutoArea()+building.getBusinessBrutoArea());

            default:
                return building.getFloorArea();
        }
    }

    public double calculateNetoArea(Building building){
        if(building.getConstruction().getSupportingSystem().getSupportingSystem().equals("Zidani sustavi")){
            return 0.75*calculateBrutoArea(building);
        }
        return 0.83*calculateBrutoArea(building);
    }

    public double calculateBrutoArea(double floorArea, double basementArea, double atticArea, int numOfFloors){
        return atticArea+basementArea+(floorArea*numOfFloors);
    }

    public double calculateFloorArea(double residentialArea, double businessArea){
        return residentialArea+businessArea;
    }

    public double calculateNetoArea(double brutoArea, String supportingSystem){
        if(supportingSystem.equals("Zidani sustavi")){
            return 0.75*brutoArea;
        }
        return 0.83*brutoArea;
    }

}
