public class FindMD5 {

	public static void main(String[] args) {
		
		String str = "SKY-PTNM-", tmpStr;
		String target = "ee854646eff20e7e40fb09bd52bb6576";
		
		//Build all 10,000 Combinations
		for(int i = 1; i < 10000; i++){
			tmpStr = str + String.format("%04d", i);
			//System.out.println(tmpStr);
			if(target.equals(MD5(tmpStr))){
				System.out.println(tmpStr);
			}
		}
	}

	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
}
