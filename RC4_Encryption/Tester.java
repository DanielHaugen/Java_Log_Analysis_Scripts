import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/*
 * 
 * 	Work in Progress...
 * 
 */

public class Tester {

	static String message = "6fce38f8836e82d446c3af46eb3a945a97bb8088256751e47f73a02943883165";
	
	static ArrayList<String> possibleWords = new ArrayList<>();

	static RC4 rc4 = new RC4("    ".getBytes());

	public static void main(String[] args) {

		//Already tried "message", "the"
		possibleWords.add("word");
		possibleWords.add(" a ");
		possibleWords.add("is ");
		possibleWords.add("value");
		
		char arr[] = (" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".toCharArray()); 
		int r = 4; 
		printAllKLength(arr, r); 

		System.out.println("Finished");
		//System.out.println(rc4.encrypt("hello".getBytes()));

	}

	static void printAllKLength(char[] set, int k) { 
		int n = set.length;  
		printAllKLengthRec(set, "", n, k); 
	} 

	// The main recursive method to print all possible strings of length k 
	static void printAllKLengthRec(char[] set, String prefix, int n, int k) { 

		// Base case: k is 0, print prefix 
		if (k == 0) { 
			//System.out.println(prefix);
			/*
				Code for testing the potential password goes here. Prefix is the possible password
			 */
			try {
				if(possibleWords.contains(decrypt(message.getBytes(), prefix).toLowerCase()))
					System.out.println(decrypt(message.getBytes(), prefix) + " - " + prefix + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return; 
		} 

		// One by one add all characters from set and recursively call for k equals to k-1 
		for (int i = 0; i < n; ++i) { 
			// Next character of input added 
			String newPrefix = prefix + set[i];  

			// k is decreased, because we have added a new character 
			printAllKLengthRec(set, newPrefix, n, k - 1);  
		} 
	} 
	
	public static String decrypt(byte[] toDecrypt, String key) throws Exception {
	      // create a binary key from the argument key (seed)
	      SecureRandom sr = new SecureRandom(key.getBytes());
	      KeyGenerator kg = KeyGenerator.getInstance("RC4");
	      kg.init(sr);
	      SecretKey sk = kg.generateKey();
	  
	      // do the decryption with that key
	      Cipher cipher = Cipher.getInstance("RC4");
	      cipher.init(Cipher.DECRYPT_MODE, sk);
	      byte[] decrypted = cipher.doFinal(toDecrypt);
	  
	      return new String(decrypted);
	   }
}
