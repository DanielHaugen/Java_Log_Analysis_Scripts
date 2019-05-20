import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Text_File_Input {

	public static void main(String[] args) throws Exception // Throws exception, for when setting the Scanner to the possibly missing file
	{ 
		// pass the path to the file as a parameter 
		File file = new File("Netflow_Log_Analysis/netflow.txt"); 
		Scanner scan = new Scanner(file); 

		HashMap<String, Integer> map = new HashMap<>();

		String currentLine;
		while (scan.hasNextLine()) {
			currentLine = scan.nextLine(); //Entire Line
			currentLine = currentLine.substring(currentLine.indexOf("TCP")+3); //Right after TCP and till the end of the line
			currentLine = currentLine.substring(0, currentLine.indexOf(":")); //Right after TCP and before the ':' of the sub-net
			currentLine = currentLine.trim(); //Just the IP Address
			
			//System.out.println(currentLine);
			if(!map.containsKey(currentLine))
				map.put(currentLine, 1);
			else
				map.replace(currentLine, map.get(currentLine) + 1);
				
		}
		
		System.out.println("Map Size: " + map.size());  
		map = sortByValue(map);
		
		for (String name: map.keySet()){
            String key = name.toString();
            String value = map.get(name).toString();  
            if(map.get(name) > 20)
            System.out.println(value + "  -  " + key);  
		}
		
		
		scan.close();
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
