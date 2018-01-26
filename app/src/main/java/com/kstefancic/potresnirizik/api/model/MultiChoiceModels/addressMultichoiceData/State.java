package com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData;

/**
 * Created by user on 26.11.2017..
 */

public class State {

    int id;
    String stateName;

    public State(int id, String stateName) {
        this.id = id;
        this.stateName = stateName;
    }

    public State() {
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", stateName='" + stateName + '\'' +
                '}';
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
