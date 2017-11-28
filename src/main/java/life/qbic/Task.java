package life.qbic;

public class Task{
    
    /**
     * The task id
     */
    private int taskId;

    /**
     * The native job id
     */
    private String nativeId;

    /**
     * The process name
     */
    private String process;

    /**
     * The process status
     */
    private String status;

    /**
     * Process exit
     */
    private int exit;

    /**
     * Process module
     */
    private String module;

    /**
     * Number of requested CPUs
     */
    private int cpusRequested;

    /**
     * Process duration
     */
    private String time;

    /**
     * Disk usage
     */
    private String disk;

    /**
     * Requested memory
     */
    private String memory;

    /**
     * Realtime duration
     */
    private String realtime;

    /**
     * CPU used (%)
     */
    private double cpuUsed;

    /**
     * Memory used (%)
     */
    private double memUsed;

    public Task(){
        this.taskId = -1;
        this.nativeId = "";
        this.process = "";
        this.status = "";
        this.exit = -1;
        this.cpusRequested = -1;
        this.time = "";
        this.disk = "";
        this.memory = "";
        this.realtime = "";
        this.cpuUsed = -1;
        this.memUsed = -1;
    }

	/**
	 * @return the taskId
	 */
	public int getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the nativeId
	 */
	public String getNativeId() {
		return nativeId;
	}

	/**
	 * @param nativeId the nativeId to set
	 */
	public void setNativeId(String nativeId) {
		this.nativeId = nativeId;
	}

	/**
	 * @return the process
	 */
	public String getProcess() {
		return process;
	}

	/**
	 * @param process the process to set
	 */
	public void setProcess(String process) {
		this.process = process;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the exit
	 */
	public int getExit() {
		return exit;
	}

	/**
	 * @param exit the exit to set
	 */
	public void setExit(int exit) {
		this.exit = exit;
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return the cpusRequested
	 */
	public int getCpusRequested() {
		return cpusRequested;
	}

	/**
	 * @param cpusRequested the cpusRequested to set
	 */
	public void setCpusRequested(int cpusRequested) {
		this.cpusRequested = cpusRequested;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the disk
	 */
	public String getDisk() {
		return disk;
	}

	/**
	 * @param disk the disk to set
	 */
	public void setDisk(String disk) {
		this.disk = disk;
	}

	/**
	 * @return the memory
	 */
	public String getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(String memory) {
		this.memory = memory;
	}

	/**
	 * @return the realtime
	 */
	public String getRealtime() {
		return realtime;
	}

	/**
	 * @param realtime the realtime to set
	 */
	public void setRealtime(String realtime) {
		this.realtime = realtime;
	}

	/**
	 * @return the cpuUsed
	 */
	public double getCpuUsed() {
		return cpuUsed;
	}

	/**
	 * @param cpuUsed the cpuUsed to set
	 */
	public void setCpuUsed(double cpuUsed) {
		this.cpuUsed = cpuUsed;
	}

	/**
	 * @return the memUsed
	 */
	public double getMemUsed() {
		return memUsed;
	}

	/**
	 * @param memUsed the memUsed to set
	 */
	public void setMemUsed(double memUsed) {
		this.memUsed = memUsed;
	}

    




}