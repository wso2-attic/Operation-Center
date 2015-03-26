package org.wso2.oc.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Node {

	private static final Log log= LogFactory.getLog(Node.class);

    private String nodeId;
    private String ip;
    private String subDomain;
    private String adminServiceUrl;
    private String startTime;
    private String os;
    private double systemCpuUsage;
    private double userCpuUsage;
    private String serverUpTime;
	private double totalMemory;
	private int cpuCount;
	private double cpuSpeed;
	private String timestamp;
	private List<String> patches;
	private double freeMemory;
	private double idleCpuUsage;
    private int threadCount;
    private double systemLoadAverage;
    private String status;
    private List<Command> commands;
    private boolean synchronizationReceived;
    private boolean registrationReceived;
	private boolean isLogEnabled;
	private String carbonLog;
    public Node(){
        commands=new ArrayList<Command>();
        registrationReceived=false;
        synchronizationReceived=false;
    }
    public String getCarbonLog() {
        return carbonLog;
    }
    public void setCarbonLog(String carbonLog) {
        this.carbonLog = carbonLog;
    }

    public void setLogEnabled(boolean isLogEnabled) {
		this.isLogEnabled = isLogEnabled;
	}

	public boolean isLogEnabled() {
		return isLogEnabled;
	}

	public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getAdminServiceUrl() {
        return adminServiceUrl;
    }

    public void setAdminServiceUrl(String adminServiceUrl) {
        this.adminServiceUrl = adminServiceUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public int getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(int cpuCount) {
        this.cpuCount = cpuCount;
    }

    public double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getPatches() {
        return patches;
    }

    public void setPatches(List<String> patches) {
        this.patches = patches;
    }

    public double getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(double freeMemory) {
        this.freeMemory = freeMemory;
    }

    public double getIdleCpuUsage() {
        return idleCpuUsage;
    }

    public void setIdleCpuUsage(double idleCpuUsage) {
        this.idleCpuUsage = idleCpuUsage;
    }

    public double getSystemCpuUsage() {
        return systemCpuUsage;
    }

    public void setSystemCpuUsage(double systemCpuUsage) {
        this.systemCpuUsage = systemCpuUsage;
    }

    public double getUserCpuUsage() {
        return userCpuUsage;
    }

    public void setUserCpuUsage(double userCpuUsage) {
        this.userCpuUsage = userCpuUsage;
    }

    public String getServerUpTime() {
        return serverUpTime;
    }

    public void setServerUpTime(String serverUpTime) {
        this.serverUpTime = serverUpTime;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Command> getCommands() {
        return commands;
    }

	public void addCommand(String commandId){
		if(commands == null) {
			commands = new ArrayList<Command>();
		}
		commands.add(new Command(commandId));
	}

    public void updateNodeStatus(){
        Date currentTime = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");

        Date lastServerUpTime = null;

        try {
            lastServerUpTime = dateFormat.parse(this.getTimestamp());
        } catch (ParseException e) {
            log.error("Error in parsing timestamp", e);
        }

        long diff = currentTime.getTime() - lastServerUpTime.getTime();

        if(diff>ServerConstants.NODE_DOWN_TIME_INTERVAL){
            this.setStatus(ServerConstants.NODE_DOWN);
        }else if(diff>ServerConstants.NODE_NOT_REPORTING_TIME_INTERVAL){
            this.setStatus(ServerConstants.NODE_NOT_REPORTING);
        }else{
            this.setStatus(ServerConstants.NODE_RUNNING);
        }
    }

    public boolean isSynchronizationReceived() {
        return synchronizationReceived;
    }

    public void setSynchronizationReceived(boolean synchronizationReceived) {
        this.synchronizationReceived = synchronizationReceived;
    }

    public boolean isRegistrationReceived() {
        return registrationReceived;
    }

    public void setRegistrationReceived(boolean registrationReceived) {
        this.registrationReceived = registrationReceived;
    }
}
