import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AppTransferLog {
	
	public static void main(String[] args) throws Exception // Throws exception, for when setting the Scanner to the possibly missing file
	{ 
		// pass the path to the file as a parameter 
		File file = new File("App_Transfer_Log_Analysis/app_transfers.log"); 
		Scanner scan = new Scanner(file); 

		HashMap<String, Integer> map = new HashMap<>();
		HashMap<String, Integer> IPMap = new HashMap<>();
		
		ArrayList<Transfer> list = new ArrayList<>();
		String[] strArray = new String[8];
		double transferSize = 0.0, totalGB = 0.0;
		int countEntries = 0;
		
		
		String curln;
		while (scan.hasNextLine()) {
			curln = scan.nextLine(); //Entire Line
			if(curln.charAt(0) == '{') {
				countEntries++;
				
				// IP Address Line
				curln = scan.nextLine();
				curln = curln.substring(curln.indexOf(": \"") + 3, curln.length()-2);
				strArray[0] = curln;
				//System.out.println(curln);
				
				// Location Line + {
				curln = scan.nextLine(); 
				
				// Accuracy Line
				curln = scan.nextLine(); 
				curln = curln.substring(curln.indexOf(":") + 2, curln.length()-1);
				strArray[1] = curln;
				//System.out.println(curln);
				
				// Latitude Line
				curln = scan.nextLine();
				curln = curln.substring(curln.indexOf(":") + 2, curln.length()-1);
				strArray[2] = curln;
				//System.out.println(curln);
				
				// Longitude Line
				curln = scan.nextLine();
				curln = curln.substring(curln.indexOf(":") + 2, curln.length()-1);
				strArray[3] = curln;
				//System.out.println(curln);
				
				curln = scan.nextLine(); // }, Line
				
				// User-name Line
				curln = scan.nextLine(); 
				curln = curln.substring(curln.indexOf(":") + 3, curln.length()-2);
				strArray[4] = curln;
				//System.out.println(curln);
				
				// Transfer Size Line
				curln = scan.nextLine();
				curln = curln.substring(curln.indexOf(":") + 3, curln.length()-2);
				strArray[5] = curln;
				//System.out.println(curln);
				
				//	Date/Time Line
				curln = scan.nextLine();
				//	Date
				strArray[6] = curln.substring(curln.indexOf(":") + 3, curln.indexOf("T"));
				//System.out.println(strArray[6]);
				//	Time
				curln = curln.substring(curln.indexOf("T") + 1, curln.length()-1);
				strArray[7] = curln;
				//System.out.println(curln);
				
				curln = scan.nextLine(); // } Closing Line
			}
			
			try {
				transferSize = Double.parseDouble(strArray[5].substring(0, strArray[5].indexOf(" ")));
			} catch(NumberFormatException e) {
				transferSize = (double) Integer.parseInt(strArray[5].substring(0, strArray[5].indexOf(" ")));
			}
			
			//System.out.println(transferSize);
			if(strArray[5].substring(strArray[5].length()-2).equals("kB")) {
				transferSize /= Math.pow(10, 6);
			} else if(strArray[5].substring(strArray[5].length()-2).equals("MB")) {
				transferSize /= Math.pow(10, 3);
			} else { // A few cases are just a couple bytes
				transferSize /= Math.pow(10, 9);
			}
			//System.out.println("Now: " + transferSize);
			totalGB += transferSize;
			
			//System.out.println(transferSize);
			list.add(new Transfer(strArray[0], strArray[1], strArray[2], strArray[3], strArray[4], transferSize, strArray[6], strArray[7]));
			
			// Map of Dates
			if(!map.containsKey(strArray[6]))
				map.put(strArray[6], 1);
			else
				map.replace(strArray[6], map.get(strArray[6]) + 1);

			// Map of IP Addresses
			if(!IPMap.containsKey(strArray[0]))
				IPMap.put(strArray[0], 1);
			else
				IPMap.replace(strArray[0], IPMap.get(strArray[0]) + 1);
		}

		System.out.println("Number of Entries: " + countEntries);
		System.out.println("Total Gigabytes: " + totalGB);
		
		map = sortByValue(map);
		System.out.println("Busiest Date: " + map.keySet().iterator().next()); // Returns the first value of the sorted set
		
		System.out.println("\nMap of Dates:");
		for (String name: map.keySet()){
			String key = name.toString();
			String value = map.get(name).toString();  
			//if(map.get(name) > 20)
				System.out.println(value + "  -  " + key);  
		}
		
		/*System.out.println("\nMap of IP Addresses:");
		for (String name: IPMap.keySet()){
			String key = name.toString();
			String value = IPMap.get(name).toString();  
			if(IPMap.get(name) >= 1)
				System.out.println(value + "  -  " + key);  
		}*/


		scan.close();
	} 

	static class Transfer{
		String ipAddress, accuracy, latitude, longitude, username, date, time;
		double transferSize; // In Gigabytes.
		
		Transfer(String ip, String ac, String lat, String lon, String user, double tran, String date, String time){
			this.ipAddress = ip;
			this.accuracy = ac;
			this.latitude = lat;
			this.longitude = lon;
			this.username = user;
			this.transferSize = tran;
			this.date = date;
			this.time = time;
			
		}
		
	}

	/*
	 *	 This section of code from GeeksForGeeks
	 * 
	 *	 https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
	 */
	// function to sort hashmap by values 
	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
	{ 
		// Create a list from elements of HashMap 
		List<Map.Entry<String, Integer> > list = 
				new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 

		// Sort the list 
		Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
			public int compare(Map.Entry<String, Integer> o1,  
					Map.Entry<String, Integer> o2) 
			{ 
				return (o2.getValue()).compareTo(o1.getValue()); // Swap o2 and o1 to get ascending order
			} 
		}); 

		// put data from sorted list to hashmap  
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
		for (Map.Entry<String, Integer> aa : list) { 
			temp.put(aa.getKey(), aa.getValue()); 
		} 
		return temp; 
	} 
	
}
