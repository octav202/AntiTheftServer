package com.ndroid.at;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndroid.at.models.Device;
import com.ndroid.at.models.DeviceAlert;
import com.ndroid.at.models.DeviceStatus;
import com.ndroid.at.models.Location;

@RestController
public class Controller {

	private static HashMap<Integer, Device> devices = new HashMap<Integer, Device>();
	private static List<Location> locations = new ArrayList<Location>();
	private static HashMap<Integer, DeviceStatus> deviceStatuses = new HashMap<Integer, DeviceStatus>();
	private static HashMap<Integer, DeviceAlert> deviceAlerts = new HashMap<Integer, DeviceAlert>();
	
	static {

		// Test Device
		devices.put(1, new Device(1, "device", "pass"));
		deviceStatuses.put(1, new DeviceStatus(1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		deviceAlerts.put(1, new DeviceAlert(1, "", "", ""));
		
//		locations.add(new Location(1, 10.0, 10.0, "10:01"));
//		locations.add(new Location(1, 20.0, 20.0, "10:02"));
//		locations.add(new Location(1, 30.0, 30.0, "10:03"));
//		locations.add(new Location(1, 40.0, 40.0, "10:04"));
//		locations.add(new Location(1, 50.0, 50.0, "10:05"));
//		locations.add(new Location(1, 60.0, 60.0, "10:06"));
		
	}

	// Device
	@RequestMapping(value = "/getDevice")
	public ResponseEntity<Device> getDevice(@RequestParam(value = "id", defaultValue = "0") String id) {
		System.out.print("\n getDevice() : " + id);
		Device device = devices.get(Integer.parseInt(id));
		return new ResponseEntity<Device>(device, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDeviceId")
	public ResponseEntity<Integer> getDeviceId(@RequestParam Map<String, String> requestParams) {
		String name = requestParams.get("name");
		String pass = requestParams.get("pass");
		System.out.print("\n getDeviceId() : " + name + ", " + pass);
		Iterator<Entry<Integer, Device>> it = devices.entrySet().iterator();
		int id = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Device> entry = (Entry<Integer, Device>) it.next();
			Device device = entry.getValue();
			if (device.getDeviceName().equals(name) && device.getDevicePass().equals(pass)) {
				id = device.getDeviceId();
			}
		}

		return new ResponseEntity<Integer>(id, HttpStatus.OK);
	}

	@RequestMapping(value = "/addDevice")
	public ResponseEntity<Integer> addDevice(@RequestParam Map<String, String> requestParams) {
		String name = requestParams.get("name");
		String pass = requestParams.get("pass");
		System.out.print("\n addDevice : " + name + ", " + pass);
		Iterator<Entry<Integer, Device>> it = devices.entrySet().iterator();
		int max = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Device> entry = (Entry<Integer, Device>) it.next();
			Device device = entry.getValue();
			if (device.getDeviceId() > max) {
				max = device.getDeviceId();
			}
		}

		int nextId = max +1;
		Device device = new Device(nextId, name, pass);
		System.out.print("\n Device added : " + device);
		devices.put(device.getDeviceId(), device);

		// Add Initial Device Status
		deviceStatuses.put(device.getDeviceId(), new DeviceStatus(device.getDeviceId(), 0, 0, 0, 0, 0, 0, 0, 0, 0));

		// Add Initial Alert
		deviceAlerts.put(device.getDeviceId(), new DeviceAlert(device.getDeviceId(), "", "", ""));
		
		return new ResponseEntity<Integer>(nextId, HttpStatus.OK);
	}

	// Location

	@RequestMapping(value = "/sendLocation", method = RequestMethod.POST)
	public ResponseEntity<Location> send(@RequestBody Location location) {
		System.out.print("\n sendLocation() : " + location);

		if (location != null) {
			for (Location loc:locations) {
				if (location.isEqualTo(loc)) {
					System.out.println("\n Location already stored.");
					return new ResponseEntity<Location>(location, HttpStatus.OK);
				}
			}
			
			locations.add(location);
		}
		return new ResponseEntity<Location>(location, HttpStatus.OK);
	}

	@RequestMapping(value = "/getLocation", method = RequestMethod.GET)
	public ResponseEntity<List<Location>> getLocation(@RequestParam(value = "id", defaultValue = "0") String id) {
		System.out.print("\n getLocation() : " + id);

		List<Location> deviceLocs = new ArrayList<Location>();
		Integer deviceId = Integer.parseInt(id);

		for (Location loc : locations) {
			if (loc.getDeviceId() == deviceId) {
				deviceLocs.add(loc);
			}
		}
		return new ResponseEntity<List<Location>>(deviceLocs, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/simulateLocation", method = RequestMethod.GET)
	public ResponseEntity<Boolean> simulateLocation(@RequestParam(value = "id", defaultValue = "0") String id) {
		System.out.print("\n simulateLocation() : " + id);

		Integer deviceId = Integer.parseInt(id);

		Location lastLocation = null;
		for (Location loc : locations) {
			if (loc.getDeviceId() == deviceId) {
				lastLocation = loc;
			}
		}
		
		if (lastLocation == null) {
			lastLocation = new Location();
			lastLocation.setDeviceId(deviceId);
			lastLocation.setLat(45.4459525);
			lastLocation.setLon(28.051898);
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			lastLocation.setTimeStamp(timeStamp);
		} 
		
		simulateLocation(lastLocation);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	// Device Status
	@RequestMapping(value = "/sendDeviceStatus", method = RequestMethod.POST)
	public ResponseEntity<DeviceStatus> sendDeviceStatus(@RequestBody DeviceStatus deviceStatus) {
		System.out.print("\n sendDeviceStatus() : " + deviceStatus);

		if (deviceStatuses != null) {
			deviceStatuses.put(deviceStatus.getDeviceId(), deviceStatus);
		}
		
		System.out.print("\n sendDeviceStatus: " + deviceStatus);
		return new ResponseEntity<DeviceStatus>(deviceStatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDeviceStatus")
	public ResponseEntity<DeviceStatus> getDeviceStatus(@RequestParam(value = "id", defaultValue = "0") String id) {
		System.out.print("\n getDeviceStatus() : " + id);
		DeviceStatus deviceStatus = deviceStatuses.get(Integer.parseInt(id));
		
		if (deviceStatus == null) {
			// Add Initial Device Status
			deviceStatus = new DeviceStatus(Integer.parseInt(id), 0, 0, 0, 0, 0, 0, 0, 0, 0);
			deviceStatuses.put(Integer.parseInt(id), deviceStatus);
			System.out.print("\n Device Status initialized: " + deviceStatus);
		}
		
		System.out.print("\n getDeviceStatus: " + deviceStatus);
		return new ResponseEntity<DeviceStatus>(deviceStatus, HttpStatus.OK);
	}
	
	// Device Alerts
	@RequestMapping(value = "/sendDeviceAlert", method = RequestMethod.POST)
	public ResponseEntity<DeviceAlert> sendDeviceAlert(@RequestBody DeviceAlert deviceAlert) {
		System.out.print("\n sendDeviceAlert() : " + deviceAlert);

		if (deviceAlerts != null) {
			deviceAlerts.put(deviceAlert.getDeviceId(), deviceAlert);
		}

		System.out.print("\n sendDeviceAlert: " + deviceAlert);
		return new ResponseEntity<DeviceAlert>(deviceAlert, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getDeviceAlert")
	public ResponseEntity<DeviceAlert> getDeviceAlert(@RequestParam(value = "id", defaultValue = "0") String id) {
		System.out.print("\n getDeviceAlert() : " + id);
		DeviceAlert deviceAlert = deviceAlerts.get(Integer.parseInt(id));
		
		if (deviceAlert == null) {
			System.out.print("\n No Device Alert Set.");
		}
		
		System.out.print("\n getDeviceAlert: " + deviceAlert);
		return new ResponseEntity<DeviceAlert>(deviceAlert, HttpStatus.OK);
	}
	
	/**
	 * Simulate Location
	 */
	private void simulateLocation(Location location) {
		System.out.print("\n simulating location.. ");
		
		DeviceStatus status = deviceStatuses.get(location.getDeviceId());
		
		locations.add(location);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 1; i<= 20 ; i++) {
					Location nextLocation = new Location();
					nextLocation.setDeviceId(location.getDeviceId());
					nextLocation.setLat(location.getLat() - i * 0.0005f);
					nextLocation.setLon(location.getLon() - i * 0.0005f);
					
					String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
					nextLocation.setTimeStamp(timeStamp);
					
					System.out.println("\n Adding location : " + nextLocation);
					locations.add(nextLocation);
					try {
						Thread.sleep(1000 * status.getLocationFrequency());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	

}