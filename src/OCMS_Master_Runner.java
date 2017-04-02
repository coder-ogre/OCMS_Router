
public class OCMS_Master_Runner {
	
	private static OCMS_Master_Runner master = new OCMS_Master_Runner();
	private static String[] ipList = new String[4];
	
	private OCMS_Master_Runner() { }
	
	public static OCMS_Master_Runner getInstance()
	{
		return master;
	}
	
	public static void addIp(int index, String ip)
	{
		ipList[index] = ip;
	}
	
	public static String getIp(int index)
	{
		return ipList[index];
	}
	
	public static String[] getIpList()
	{
		return ipList;
	}
	
	public static void main() {
		
		
		OCMS_Router r1 = new OCMS_Router(1, ipList);
		r1.start();
		OCMS_Router r2 = new OCMS_Router(2, ipList);
		r2.start();
	}

}