import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class SH_XmlRpcClient {
	
	public String[] get_topology_data() throws Exception {

		XmlRpcClientConfigImpl conf = new XmlRpcClientConfigImpl();
		conf.setServerURL(new URL("http://localhost:8000"));
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(conf);
		
		List params = new ArrayList();
		params.add("3");

		// topology request
		HashMap<String,String> map = new HashMap<String,String>();
		Object result =  client.execute("topology_request", params);
		map = (HashMap)result;
		
//		System.out.println(map.get("topology"));
//		System.out.println(map.get("linkdata"));
//		System.out.println(map.get("flowdata"));
		
		String[] data = {map.get("topology"),map.get("linkdata"), map.get("flowdata")};
		return data;
	}

	
	
	public int send_route_change(String[] routedata) throws Exception {

		XmlRpcClientConfigImpl conf = new XmlRpcClientConfigImpl();
		conf.setServerURL(new URL("http://localhost:8000"));
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(conf);

		//route change request
		List request = new ArrayList();
		
		request.add(routedata[0]);
		request.add(routedata[1]);
		request.add(routedata[2]);
		request.add(routedata[3]);
		request.add(routedata[4]);
		request.add(routedata[5]);
		request.add(routedata[6]);
		request.add(routedata[7]);
		request.add(routedata[8]);
		request.add(routedata[9]);
		request.add(routedata[10]);
		request.add(routedata[11]);
		request.add(routedata[12]);
		request.add(routedata[13]);
		request.add(routedata[14]);
		request.add(routedata[15]);
		request.add(routedata[16]);
		request.add(routedata[17]);
		request.add(routedata[18]);
		request.add(routedata[19]);
		request.add(routedata[20]);
		request.add(routedata[21]);
		request.add(routedata[22]);
		request.add(routedata[23]);
		request.add(routedata[24]);
		
		//remote handle flow
		Object handle =  client.execute("route_change_request", request);
		int hand = (int)handle;
		
		System.out.println(hand);
		return hand;
	}
}
