
package saqib.rasul;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * class to do some common things for client & server to get RMI working
 * 
 * @author srasul
 * 
 */
public abstract class RmiStarter {

    /**
     * 
     * @param clazzToAddToServerCodebase a class that should be in the java.rmi.server.codebase property.
     */
    public RmiStarter(Class<?> clazzToAddToServerCodebase) {

    	System.setProperty("java.rmi.server.hostname", getIP());
        System.setProperty("java.rmi.server.codebase", clazzToAddToServerCodebase
            .getProtectionDomain().getCodeSource().getLocation().toString());

        System.setProperty("java.security.policy", PolicyFileLocator.getLocationOfPolicyFile());

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        //doCustomRmiHandling();
    }

    /**
     * extend this class and do RMI handling here
     */
    public abstract void doCustomRmiHandling(String serverID);
    
    public static String getIP(){
    	Enumeration<NetworkInterface> e = null;
    	String localIP = null;
		try {
			e = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			System.out.println("IP not found, connected??");
			System.exit(0);
		}
		while(e.hasMoreElements())
		{
		    NetworkInterface n = (NetworkInterface) e.nextElement();
		    Enumeration<InetAddress> ee = n.getInetAddresses();
		    while (ee.hasMoreElements())
		    {
		        InetAddress i = (InetAddress) ee.nextElement();
		        if(!i.isLoopbackAddress() && i instanceof Inet4Address){
		        	localIP = i.getHostAddress();
		        }
		    }
		}
		return localIP;
    }

}
