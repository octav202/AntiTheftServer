package com.ndroid.at;

import java.util.ArrayList;
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
import com.ndroid.at.models.DeviceStatus;
import com.ndroid.at.models.Location;

@RestController
public class Controller {

	private static HashMap<Integer, Device> devices = new HashMap<Integer, Device>();
	private static List<Location> locations = new ArrayList<Location>();
	private static HashMap<Integer, DeviceStatus> deviceStatuses = new HashMap<Integer, DeviceStatus>();
	static {

//		locations.add(new Location(1, 10.0, 10.0, "10:01"));
//		locations.add(new Location(1, 20.0, 20.0, "10:02"));
//		locations.add(new Location(1, 30.0, 30.0, "10:03"));
//		locations.add(new Location(1, 40.0, 40.0, "10:04"));
//		locations.add(new Location(1, 50.0, 50.0, "10:05"));
//		locations.add(new Location(1, 60.0, 60.0, "10:06"));

//		deviceStatuses.put(1, new DeviceStatus(1, 1, 1, 1, 1, 1, 0));
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
		deviceStatuses.put(device.getDeviceId(), new DeviceStatus(device.getDeviceId(), 0, 0, 0, 0, 0, 0, 0));

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
		
		if (lastLocation != null) {
			simulateLocation(lastLocation);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} else {
			System.out.print("\\n Simulate Location Failed - no previous location");
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		
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
		
		System.out.print("\n getDeviceStatus: " + deviceStatus);
		return new ResponseEntity<DeviceStatus>(deviceStatus, HttpStatus.OK);
	}
	
	/**
	 * Simulate Location
	 */
	private void simulateLocation(Location location) {
		System.out.print("\n simulating location.. ");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 1; i<= 20 ; i++) {
					Location nextLocation = new Location();
					nextLocation.setDeviceId(location.getDeviceId());
					nextLocation.setLat(location.getLat() + i * 3);
					nextLocation.setLon(location.getLon() + i * 3);
					nextLocation.setTimeStamp(location.getTimeStamp());
					
					System.out.println("\n Adding location : " + nextLocation);
					locations.add(nextLocation);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	

}