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
		devices.put(1, new Device(1, "Device1", "Pass1"));
		devices.put(2, new Device(2, "Device2", "Pass2"));
		devices.put(3, new Device(3, "Device3", "Pass3"));

		locations.add(new Location(1, 10.0, 10.0, "10:01"));
		locations.add(new Location(1, 20.0, 20.0, "10:02"));
		locations.add(new Location(1, 30.0, 30.0, "10:03"));
		locations.add(new Location(1, 40.0, 40.0, "10:04"));
		locations.add(new Location(1, 50.0, 50.0, "10:05"));
		locations.add(new Location(1, 60.0, 60.0, "10:06"));

		deviceStatuses.put(1, new DeviceStatus(1, 1, 1, 1));
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

	@RequestMapping(value = "/addDevice", method = RequestMethod.POST)
	public ResponseEntity<Device> update(@RequestBody Device device) {
		System.out.print("\n addDevice() : " + device);
		if (device != null) {
			devices.put(device.getDeviceId(), device);
		}
		return new ResponseEntity<Device>(device, HttpStatus.OK);
	}

	// Location

	@RequestMapping(value = "/sendLocation", method = RequestMethod.POST)
	public ResponseEntity<Location> send(@RequestBody Location location) {
		System.out.print("\n sendLocation() : " + location);

		if (location != null) {
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

	// Device Status
	@RequestMapping(value = "/sendDeviceStatus", method = RequestMethod.POST)
	public ResponseEntity<DeviceStatus> sendDeviceStatus(@RequestBody DeviceStatus deviceStatus) {
		System.out.print("\n sendDeviceStatus() : " + deviceStatus);

		if (deviceStatuses != null) {
			deviceStatuses.put(deviceStatus.getDeviceId(), deviceStatus);
		}
		return new ResponseEntity<DeviceStatus>(deviceStatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getDeviceStatus")
	public ResponseEntity<DeviceStatus> getDeviceStatus(@RequestParam(value = "id", defaultValue = "0") String id) {
		System.out.print("\n getDeviceStatus() : " + id);
		DeviceStatus deviceStatus = deviceStatuses.get(Integer.parseInt(id));
		return new ResponseEntity<DeviceStatus>(deviceStatus, HttpStatus.OK);
	}

}