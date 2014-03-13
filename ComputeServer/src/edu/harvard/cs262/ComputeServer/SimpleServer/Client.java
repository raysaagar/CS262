package edu.harvard.cs262.ComputeServer.SimpleServer;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import tasks.HelloTask;

import edu.harvard.cs262.ComputeServer.ComputeServer;
import edu.harvard.cs262.ComputeServer.WorkTask;

public class Client {
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
        int port = 1099;

		// TODO Auto-generated method stub
            System.setProperty("java.security.policy", "security.policy");

			if (System.getSecurityManager()==null){
				System.setSecurityManager(new SecurityManager());
			}

		 if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new SecurityManager());
	        }
		 try {
	            String name = "SimpleServer";
	            Registry registry = LocateRegistry.getRegistry(args[0], port);
	            ComputeServer comp = (ComputeServer) registry.lookup(name);
	            WorkTask work = new HelloTask("Hello");
	            String response = (String) comp.sendWork(work);
	            System.out.println(response);
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
		 
	}

}
