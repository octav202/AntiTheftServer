package com.ndroid.at.models;

public class DeviceStatus {

	private int deviceId;
	private Integer lock;
	private Integer wipeData;
	private Integer encryptStorage;
	
	public DeviceStatus() {
		this.deviceId = 0;
		this.lock = 0;
		this.wipeData = 0;
		this.encryptStorage = 0;
	}
	
	public DeviceStatus(int deviceId, Integer lock, Integer wipeData, Integer encryptStorage) {
		this.deviceId = deviceId;
		this.lock = lock;
		this.wipeData = wipeData;
		this.encryptStorage = encryptStorage;
	}
	
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getLock() {
		return lock;
	}
	public void setLock(Integer lock) {
		this.lock = lock;
	}
	public Integer getWipeData() {
		return wipeData;
	}
	public void setWipeData(Integer wipeData) {
		this.wipeData = wipeData;
	}
	public Integer getEncryptStorage() {
		return encryptStorage;
	}
	public void setEncryptStorage(Integer encryptStorage) {
		this.encryptStorage = encryptStorage;
	}
	@Override
	public String toString() {
		return "DeviceStatus [deviceId=" + deviceId + ", lock=" + lock + ", wipeData=" + wipeData + ", encryptStorage="
				+ encryptStorage + "]";
	}
	

}
