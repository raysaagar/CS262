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

        int port              = 1099; // Port this server is running on
        String queuedHostname = "rinchiera.com"; // Hostname of WorkQueue
        int queuedPort        = 1099; // Port of WorkQueue
        Registry registry     = null; // Necessary for Compile


        /* Variable port so we can have multiple workers on a single machine */
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        /* If WorkQueue is being operated by a different group */
        if(args.length > 1) {
            queuedHostname = args[1];
        }

        try {

            /* This policy is very unsafe */
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager()==null){
                System.setSecurityManager(new SecurityManager());
            }

            /* This stub is sent to the WorkQueue */
            Server computeServer = new Server();
            ComputeServer stub = (ComputeServer)UnicastRemoteObject.exportObject(computeServer, 0);

            try {
                registry = LocateRegistry.getRegistry(queuedHostname, queuedPort);
            }
            catch (Exception e) {
                System.out.println("Unable to connect to WorkQueue server " + queuedHostname + ":" + queuedPort);
                System.out.println(e);
            }

            String workQueueRegistryName = "QueuedServer";
            WorkQueue workQueue = (WorkQueue) registry.lookup(workQueueRegistryName);
            UUID response = (UUID) workQueue.registerWorker(stub);

            System.out.println("Registered to WorkQueue with ID: " + response);


        } catch (Exception e) {
            System.err.println("CompterServer exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public boolean PingServer() throws RemoteException {
        // TODO Auto-generated method stub
        return true;
    }
}
