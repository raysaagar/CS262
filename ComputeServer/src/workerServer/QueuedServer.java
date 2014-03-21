package workerServer;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.UUID;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import edu.harvard.cs262.ComputeServer.ComputeServer;
import edu.harvard.cs262.ComputeServer.WorkQueue;
import edu.harvard.cs262.ComputeServer.WorkTask;

public class QueuedServer implements ComputeServer, WorkQueue {

	private Hashtable<UUID, ComputeServer> workers;
	private LinkedList<UUID> freeWorkers, busyWorkers;
	
	private QueuedServer(){
		super();
		workers = new Hashtable<UUID, ComputeServer>();
		freeWorkers = new LinkedList<UUID>();
		busyWorkers = new LinkedList<UUID>();
	}
	
	@Override
	public UUID registerWorker(ComputeServer server) throws RemoteException {
		UUID key = UUID.randomUUID();
		workers.put(key, server);
		freeWorkers.add(key);
		return key;
	}

	@Override
	public boolean unregisterWorker(UUID workerID) throws RemoteException{
        /* If worker is busy then we wait. */

		if (null == workers.get(workerID)){
			return true;
		}
		
		workers.remove(workerID);
		freeWorkers.remove(workerID);
		busyWorkers.remove(workerID);
		return true;
	}
		
	@Override
	public Object sendWork(WorkTask work) throws RemoteException {
        /* Gets one of the things on the queue, and sends work to it */

        /* TODO Custom java exception on trying to remove a freeworker that
         * says "hey, no more workers sry." */
        UUID workerID = freeWorkers.remove();
        busyWorkers.add(workerID);
        ComputeServer worker = workers.get(workerID);
        String response = (String) worker.sendWork(work);

        busyWorkers.remove(workerID);
        freeWorkers.add(workerID);

		return response;
	}

	@Override
	public boolean PingServer() throws RemoteException {
		return true;
	}

    public static void main(String args[]){
        /* Variable port so we can have multiple workers on a single machine */
        int port = 1099;
        String queuedHostname = "rinchiera.com";

        try {
            System.setProperty("java.security.policy", "security.policy");

            if (System.getSecurityManager()==null){
                System.setSecurityManager(new SecurityManager());
            }

            QueuedServer mySrv = new QueuedServer();
            WorkQueue stub = (WorkQueue) UnicastRemoteObject.exportObject(mySrv, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("QueuedServer", stub);

            System.out.println("Ready to rock.");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
