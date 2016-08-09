/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicmdpcontroller;

import burlap.behavior.singleagent.Episode;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.StateUtilities;
import dynamicmdpcontroller.actions.GMEAction;
import dynamicmdpcontroller.controllers.FinalStateException;
import dynamicmdpcontroller.controllers.GlobalController;
import dynamicmdpcontroller.controllers.LocalController;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stefano
 */
public class DecisionSupportConnection implements DecisionSupportInterface {

    private LocalController localControllers[] = null;
    private GlobalController globalController = null;

    private Episode[] localEpisode = null;
    private Episode globalEpisode = null;

    public DecisionSupportConnection() {
        localControllers = DynamicMDPController.getInstance().getLocalControllers();
        globalController = DynamicMDPController.getInstance().getGlobalController();
        localEpisode = new Episode[localControllers.length];
    }

    private void planFromLocalState(DynamicMDPState s, int index) throws FinalStateException {
        localControllers[index].planFromState(s);
    }

    private void planFromGlobalState(DynamicMDPState s) throws FinalStateException {
        globalController.planFromState(s);
    }

    @Override
    public List<GMEAction> getAllLocalDefinedActions(int index) {
        return localControllers[index].getDomainGen().getActions();
    }

    @Override
    public List<GMEAction> getAllGlobalDefinedActions() {
        return globalController.getDomainGen().getActions();
    }

    @Override
    public String printState(DynamicMDPState s) {
        return StateUtilities.stateToString(s);
    }

    @Override
    public List<String> getAllStateAttributes(DynamicMDPState s) {
        List<Object> keys = s.variableKeys();
        List<String> ret = new ArrayList<>(keys.size());
        for (Object o : keys) {
            ret.add((String) o);
        }
        return ret;
    }

    @Override
    public List<GMEAction> getLocalOptimalPathActions(int index, DynamicMDPState d) throws FinalStateException {
        if (localEpisode[index] == null) {
            planFromLocalState(d, index);
            localEpisode[index] = localControllers[index].getEpisode();
        }
        List<Action> actions = localEpisode[index].actionSequence;
        List<GMEAction> ret = new ArrayList<>(actions.size());
        for (Action a : actions) {
            GMEAction ga = (GMEAction) a;
            ret.add(ga);
        }
        return ret;
    }

    @Override
    public List<GMEAction> getGlobalOptimalPathActions(DynamicMDPState s) throws FinalStateException {
        if (globalEpisode == null) {
            planFromGlobalState(s);
            globalEpisode = globalController.getEpisode();
        }
        List<Action> actions = globalEpisode.actionSequence;
        List<GMEAction> ret = new ArrayList<>(actions.size());
        for (Action a : actions) {
            ret.add((GMEAction) a);
        }
        return ret;
    }

    @Override
    public List<DynamicMDPState> getGlobalOptimalPath(DynamicMDPState s) throws FinalStateException {
        if (globalEpisode == null) {
            planFromGlobalState(s);
            globalEpisode = globalController.getEpisode();
        }
        List<State> states = globalEpisode.stateSequence;
        List<DynamicMDPState> ret = new ArrayList<>(states.size());
        for (State st : states) {
            ret.add((DynamicMDPState) st);
        }
        return ret;
    }

    @Override
    public List<DynamicMDPState> getLocalOptimalPath(int index, DynamicMDPState s) throws FinalStateException {
        if (localEpisode[index] == null) {
            localControllers[index].planFromState(s);
            localEpisode[index] = localControllers[index].getEpisode();
        }
        List<State> states = localEpisode[index].stateSequence;
        List<DynamicMDPState> ret = new ArrayList<>(states.size());
        for (State st : states) {
            ret.add((DynamicMDPState) st);
        }
        return ret;
    }

    @Override
    public double getGlobalPathReward(DynamicMDPState s) throws FinalStateException {
        if (globalEpisode == null) {
            planFromGlobalState(s);
            globalEpisode = globalController.getEpisode();
        }
        List<Double> rewards = globalEpisode.rewardSequence;
        double ret = 0;
        for (Double d : rewards) {
            ret += d;
        }
        return ret;
    }

    @Override
    public double getLocalPathReward(int index, DynamicMDPState s) throws FinalStateException {
        if (localEpisode[index] == null) {
            localControllers[index].planFromState(s);
            localEpisode[index] = localControllers[index].getEpisode();
        }
        List<Double> rewards = localEpisode[index].rewardSequence;
        double ret = 0;
        for (Double d:rewards) {
            ret+=d;
        }
        return ret;
    }

    @Override
    public double getLocalStateValue(int index, DynamicMDPState s) throws FinalStateException {
         if (localEpisode[index] == null) {
            localControllers[index].planFromState(s);
            localEpisode[index] = localControllers[index].getEpisode();
        }
        return localControllers[index].getPlanner().value(s);
    }

    @Override
    public double getGlobalStateValue(DynamicMDPState s) throws FinalStateException {
        if (globalEpisode == null) {
            planFromGlobalState(s);
            globalEpisode = globalController.getEpisode();
        }
        return globalController.getPlanner().value(s);
    }
}