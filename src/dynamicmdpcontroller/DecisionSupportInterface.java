/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicmdpcontroller;

import dynamicmdpcontroller.actions.GMEAction;
import dynamicmdpcontroller.controllers.FinalStateException;
import java.util.List;

/**
 *
 * @author stefano
 */
public interface DecisionSupportInterface {
    
    List<GMEAction> getLocalOptimalPathActions(int index, DynamicMDPState d) throws FinalStateException;
    List<GMEAction> getGlobalOptimalPathActions(DynamicMDPState d)throws FinalStateException;
    List<DynamicMDPState> getGlobalOptimalPath(DynamicMDPState s) throws FinalStateException;
    List<DynamicMDPState> getLocalOptimalPath(int index, DynamicMDPState s) throws FinalStateException;
    double getGlobalPathReward(DynamicMDPState s) throws FinalStateException;
    double getLocalPathReward(int index, DynamicMDPState s) throws FinalStateException;
    String printState(DynamicMDPState s);
    List<String> getAllStateAttributes(DynamicMDPState s);
    List<GMEAction> getAllLocalDefinedActions(int index);
    List<GMEAction> getAllGlobalDefinedActions();
    
    double getLocalStateValue(int index, DynamicMDPState s) throws FinalStateException;
    double getGlobalStateValue(DynamicMDPState s) throws FinalStateException;
}
