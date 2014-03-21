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
        String hostname = args[0];
        int port = 1099;

		// TODO Auto-generated method stub
        System.setProperty("java.security.policy", "security.policy");

		if (System.getSecurityManager()==null){
			System.setSecurityManager(new SecurityManager());
		}

		 try {
            String name = "SimpleServer";
            // hack to force compile
            Registry registry = null;
            try {
            	registry = LocateRegistry.getRegistry(hostname, port);
            }
            catch (Exception e) {
            	System.out.println("Unable to connect to server " + hostname + ":" + port);
            	System.out.println(e);
            }

            System.out.println("Obtaining registry object from " + hostname + ":" + port);
            ComputeServer comp = (ComputeServer) registry.lookup(name);
            System.out.println("Success!");

            WorkTask work = new HelloTask("Hello");
            System.out.println("Sending work to server...");
            String response = (String) comp.sendWork(work);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

	}
}
