package edu.harvard.cs262.ComputeServer.SimpleServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.UUID;

import edu.harvard.cs262.ComputeServer.ComputeServer;
import edu.harvard.cs262.ComputeServer.WorkQueue;
import edu.harvard.cs262.ComputeServer.WorkTask;


public class Server implements ComputeServer {

	private static final long serialVersionUID = 1L;
	public Server(){
		super();
	}
	
	@Override
	public Object sendWork(WorkTask work) throws RemoteException {
		return work.doWork();
	}
	
	public static void main(String args[]){
        /* Variable port so we can have multiple workers on a single machine */
        String port = args[0];
        String queuedHostname = "rinchiera.com";
        int queuedPort = 1100;

        try {

            System.setProperty("java.security.policy", "security.policy");

            if (System.getSecurityManager()==null){
                System.setSecurityManager(new SecurityManager());
            }



            Server mySrv = new Server();
            ComputeServer stub = (ComputeServer)UnicastRemoteObject.exportObject(mySrv, 0);

            /* First we set up our registry */
            /*
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("SimpleServer", stub);
            */

            /* Next, we try to contact other queue server's registry */
            String name = "QueuedServer";
            // hack to force compile
            Registry registry = null;
            try {
            	registry = LocateRegistry.getRegistry(queuedHostname, queuedPort);
            }
            catch (Exception e) {
            	System.out.println("Unable to connect to queue server " + queuedHostname + ":" + port);
            	System.out.println(e);
            }

            WorkQueue workQueue = (WorkQueue) registry.lookup(name);
            UUID response = (UUID) workQueue.registerWorker(stub);

            System.out.println(response);


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

	@Override
	public boolean PingServer() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
