package solutions.gutta.weatheradvisory.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZipcodeToPhone {
	private final Map<String, ArrayList<Phone>> map = new HashMap<>();
	
	public static ZipcodeToPhone ref = new ZipcodeToPhone();
	
	private ZipcodeToPhone() {
	}
	
	/**
	 * Return all the zipcodes and their respective phone mappings.
	 * 
	 * @return
	 */
	public Map<String, ArrayList<Phone>> get() {
		return Collections.unmodifiableMap(map);
	}
	
	/**
	 * Returns an immutable list of phone numbers for the zip code
	 * 
	 * @param zipcode
	 * @return
	 */
	public List<Phone> get(String zipcode) {
		return Collections.unmodifiableList(getList(zipcode)) ;
	}

	/**
	 * Returns an immutable list of phone numbers for the zip code
	 * 
	 * @param zipcode
	 * @return
	 */
	public Phone get(String zipcode, String phone) {
		int idx = index(zipcode, phone);
		
		if (idx >= 0)
			return getList(zipcode).get(idx);
		else
			return null;
	}
	
	/**
	 * Create a Zipcode to phone mapping
	 * 
	 * @param zipcode
	 * @param phone
	 */
	public void add(String zipcode, Phone phone) {
		synchronized(map) {
			getList(zipcode).add(phone);
		}
	}
		
	/**
	 * Remove a Zipcode to phone mapping.
	 * 
	 * @param zipcode
	 * @param number
	 * @return
	 */
	public Phone remove(String zipcode, String number) {
		final int idx = index(zipcode, number);
		
		if (idx < 0) return null;
		
		final List<Phone> list = getList(zipcode);
		
		synchronized(list) {
			return list.remove(idx);
		}
	}		
	
	/**
	 * Returns number of phones mapped to a Zipcode.
	 * 
	 * @param zipcode
	 * @return
	 */
	public int size(String zipcode) {
		return getList(zipcode).size();
	}
	
	/**
	 * Get an array of Zipcodes available
	 * 
	 * @return
	 */
	public String[] getZipcodes() {
		synchronized(map) {
			return map.keySet().toArray(new String[0]);
		}
	}
	
	public int index(String zipcode, String number) {
		final List<Phone> list = get(zipcode);
		
		synchronized(list) {
			for (int i=0; i<list.size(); i++) {
				if (list.get(i).getNumber().equals(number)) {
					return i;
				}
			}
		}
		
		return -1;
	}

	/**
	 * Returns an immutable list of phone numbers for the zip code
	 * 
	 * @param zipcode
	 * @return
	 */
	List<Phone> getList(String zipcode) {
		synchronized(map) {
			if (!map.containsKey(zipcode)) {
				map.put(zipcode, new ArrayList<Phone>());
			}
			
			return map.get(zipcode) ;
		}
	}

}
