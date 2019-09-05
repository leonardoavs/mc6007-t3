/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mc6007.t1.model;

import com.sleepycat.je.rep.monitor.GroupChangeEvent;
import com.sleepycat.je.rep.monitor.JoinGroupEvent;
import com.sleepycat.je.rep.monitor.LeaveGroupEvent;
import com.sleepycat.je.rep.monitor.Monitor;
import com.sleepycat.je.rep.monitor.MonitorChangeListener;
import com.sleepycat.je.rep.monitor.MonitorConfig;
import com.sleepycat.je.rep.monitor.NewMasterEvent;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author casa
 */
public class DefaultMonitorModel extends DefaultTableModel {

    public DefaultMonitorModel() {
        addColumn("Action");
        addColumn("Node Name");
        addColumn("Master Name");
        addColumn("Ip");
        addColumn("Join Time");
        addColumn("Leave Reason");
        addColumn("Leave Time");
        addColumn("Change Type");
        addColumn("Replication Group");
        try {
            MonitorConfig monConfig = new MonitorConfig();
            monConfig.setNodeName("node6020");
            monConfig.setGroupName("Mc6007T1Group");
            monConfig.setNodeHostPort("localhost:6020");
            monConfig.setHelperHosts("localhost:6001,localhost:6002,localhost:6003,localhost:6004,localhost:6005");

            final Monitor monitor = new Monitor(monConfig);

            monitor.startListener(new RouterChangeListener(this));
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}

class RouterChangeListener implements MonitorChangeListener {

    DefaultMonitorModel model = null;

    public RouterChangeListener(DefaultMonitorModel defaultMonitorModel) {
        this.model = defaultMonitorModel;
    }

    public void notify(NewMasterEvent newMasterEvent) {
        model.addRow(getData("New Master", newMasterEvent.getNodeName(), newMasterEvent.getNodeName(), getNullString(newMasterEvent.getSocketAddress()), "", "", "", "", ""));
        model.fireTableDataChanged();
    }

    public void notify(GroupChangeEvent groupChangeEvent) {
        model.addRow(getData("Group Change", groupChangeEvent.getNodeName(), "", "", "", "", "", getNullString(groupChangeEvent.getChangeType()), getNullString(groupChangeEvent.getRepGroup())));
        model.fireTableDataChanged();
    }

    public void notify(JoinGroupEvent joinGroupEvent) {
        model.addRow(getData("Joining Group", joinGroupEvent.getNodeName(), joinGroupEvent.getNodeName(), "", getNullString(joinGroupEvent.getJoinTime()), "", "", "", ""));
        model.fireTableDataChanged();
    }

    public void notify(LeaveGroupEvent leaveGroupEvent) {
        model.addRow(getData("Leaving Group", leaveGroupEvent.getNodeName(), leaveGroupEvent.getNodeName(), "", getNullString(leaveGroupEvent.getJoinTime()), getNullString(leaveGroupEvent.getLeaveReason()), getNullString(leaveGroupEvent.getLeaveTime()), "", ""));
        model.fireTableDataChanged();
    }

    private String getNullString(Object object) {
        return object == null ? "" : object.toString();
    }

    private String[] getData(String action, String nodeName, String masterName, String ip, String joinTime, String leaveReason, String leaveTime, String changeType, String replicationGroup) {
        String[] data = new String[]{action, nodeName, masterName, ip, joinTime, leaveReason, leaveTime, changeType, replicationGroup};
        System.out.println("Data: " + Arrays.asList(data));
        return data;
    }
}
