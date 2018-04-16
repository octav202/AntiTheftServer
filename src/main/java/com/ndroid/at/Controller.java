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
import com.ndroid.at.models.Location;

@RestController
public class Controller {

	private static HashMap<Integer, Device> devices = new HashMap<Integer, Device>();
	private static List<Location> locations = new ArrayList<Location>();
	static {
		devices.put(1, new Device(1, "Device1", "Pass1"));
		devices.put(2, new Device(2, "Device2", "Pass2"));
		devices.put(3, new Device(3, "Device3", "Pass3"));

		locations.add(new Location(1, 10.0, 10.0, "10:01"));
		locations.add(new Location(1, 20.0, 20.0, "10:02"));
		locations.add(new Location(1, 30.0, 30.0, "10:03"));
		locations.add(new Location(1, 40.0, 40.0, "10:04"));
		locations.add(new Location(1, 50.0, 50.0, "10:05"));
		locations.add(new Location(1, 60.0, 60.0, "10:06"));
	}

	// Device
	@RequestMapping(value = "/getDevice")
	public ResponseEntity<Device> getDevice(@RequestParam(value = "id", defaultValue = "0") String id) {
		Device device = devices.get(Integer.parseInt(id));
		return new ResponseEntity<Device>(device, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDeviceId")
	public ResponseEntity<Integer> getDeviceId(@RequestParam Map<String,String> requestParams) {
		String name = requestParams.get("name");
		String pass = requestParams.get("pass");
		Iterator<Entry<Integer, Device>> it = devices.entrySet().iterator();
		int id = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Device> entry= (Entry<Integer, Device>) it.next();
			Device device = entry.getValue();
			if (device.getDeviceName().equals(name) && device.getDevicePass().equals(pass)) {
				id = device.getDeviceId();
			}
		}
		
		return new ResponseEntity<Integer>(id, HttpStatus.OK);
	}

	@RequestMapping(value = "/addDevice", method = RequestMethod.POST)
	public ResponseEntity<Device> update(@RequestBody Device device) {
		if (device != null) {
			devices.put(device.getDeviceId(), device);
		}
		return new ResponseEntity<Device>(device, HttpStatus.OK);
	}

	// Location

	@RequestMapping(value = "/sendLocation", method = RequestMethod.POST)
	public ResponseEntity<Location> send(@RequestBody Location location) {
		if (location != null) {
			locations.add(location);
		}
		return new ResponseEntity<Location>(location, HttpStatus.OK);
	}

	@RequestMapping(value = "/getLocation")
	public ResponseEntity<Void> getLocation(@RequestParam(value = "id", defaultValue = "0") String id) {
		List<Location> deviceLocs = new ArrayList<Location>();
		Integer deviceId = Integer.parseInt(id);

		for (Location loc : locations) {
			if (loc.getDeviceId() == deviceId) {
				deviceLocs.add(loc);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}