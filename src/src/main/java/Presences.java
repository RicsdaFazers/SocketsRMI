import java.util.*;

public class Presences {

	private static Hashtable<String, IPInfo> presentIPs = new Hashtable<String, IPInfo>();
	private static int cont = 0;

	public Vector<String> getPresences(String IPAddress) {
		
		long actualTime = new Date().getTime();
		cont = cont+1;
		
		System.out.println("cont = "+ cont);
		
		//Assume-se que o IP e valido!!!!!
		synchronized(this) {
			if (presentIPs.containsKey(IPAddress)) {
				IPInfo newIp = presentIPs.get(IPAddress);
				newIp.setLastSeen(actualTime);
			}
			else {
				IPInfo newIP = new IPInfo(IPAddress, actualTime);
				presentIPs.put(IPAddress,newIP);
			}
		}
		return getIPList();
	}
	
	private Vector<String> getIPList(){
		Vector<String> result = new Vector<String>();
		for (Enumeration<IPInfo> e = presentIPs.elements(); e.hasMoreElements(); ) {
			IPInfo element = e.nextElement();
			if (!element.timeOutPassed(180*1000)) {
				result.add(element.getIP());
			}
		}
		return result;
	}
}

class IPInfo {
	
	private String ip;
	private long lastSeen;

	public IPInfo(String ip, long lastSeen) {
		this.ip = ip;
		this.lastSeen = lastSeen;
	}

	public String getIP () {
		return this.ip;
	}

	public void setLastSeen(long time){
		this.lastSeen = time;
	}

	public boolean timeOutPassed(int timeout){
		boolean result = false;
		long timePassedSinceLastSeen = new Date().getTime() - this.lastSeen;
		if (timePassedSinceLastSeen >= timeout)
			result = true;
		return result;
	}
}

