import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SSH_File_Input {
	
	public static void main(String[] args) throws Exception // Throws exception, for when setting the Scanner to the possibly missing file
	{ 
		// pass the path to the file as a parameter 
		File file = new File("SSH_Log_Analysis/auth.log"); 
		Scanner scan = new Scanner(file); 
		
		HashMap<String, Integer> userMap = new HashMap<>();
		HashMap<String, Integer> IPMap = new HashMap<>();

		int validUsernames = 0, count = 0;
		String currentLine, username;
		while (scan.hasNextLine()) {
			currentLine = scan.nextLine(); //Entire Line
			currentLine = currentLine.substring(currentLine.indexOf("]: ")+3); //Right after the SSHD call and till the end of the line
			
			// Check for Failed attempt of a User-name.
			// "Failed none" means a successful login.
			if(currentLine.substring(0,8).equals("Failed p")) {
				
				currentLine = currentLine.substring(20); //Start after "Failed Password for "
				if(currentLine.substring(0,7).equals("invalid"))
					currentLine = currentLine.substring(13);
				else
					validUsernames++;
				
				username = currentLine.substring(0, currentLine.indexOf(" ")); //Go till the first space
				
				if(!userMap.containsKey(username))
					userMap.put(username, 1);
				else
					userMap.replace(username, userMap.get(username) + 1);
				
				//System.out.println(username);
				
				currentLine = currentLine.substring(username.length() + 6); //6 = size of " from "
				currentLine = currentLine.substring(0, currentLine.indexOf(" ")); //Go till the first space
				
				//System.out.println(currentLine);
				
				if(!IPMap.containsKey(currentLine))
					IPMap.put(currentLine, 1);
				else
					IPMap.replace(currentLine, IPMap.get(currentLine) + 1);
				
				//System.out.println(currentLine);
				count++;
			}
		}
		
		
		System.out.println("userMap Size: " + userMap.size() + " (AKA: number of Usernames attempted)");
		System.out.println("IPMap Size: " + IPMap.size()  + " (AKA: number of IP Addresses) *Work in Progress*");
		System.out.println("Valid username attempts: " + validUsernames + " *Work in Progress*");
		System.out.println("Total failed attempts: " + count + " *Work in Progress*\n");
		
		userMap = sortByValue(userMap);
		IPMap = sortByValue(IPMap);
		
		// Print IP Addresses
		for (String name: IPMap.keySet()){
            String key = name.toString();
            String value = IPMap.get(name).toString();  
            //if(IPMap.get(name) > 2)
            System.out.println(value + "  -  " + key);  
		}
		
		System.out.println();
		
		// Print User-name attempts
		/*for (String name: userMap.keySet()){
            String key = name.toString();
            String value = userMap.get(name).toString();  
            //if(userMap.get(name) > 2)
            System.out.println(value + "  -  " + key);  
		}*/
		
		
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

/*
 * How many different IP addresses attempted to gain unauthorized access to the SSH server?
 * 	Wrong: 30, 32
 * 
 * How many different VALID user-names were attempted?
 * 	Wrong: 2033, 2031
 * 
 * What is the total number of failed attempts (ssh attempt failures)?
 * 	Wrong: 2553
 */
