package workerServer;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.UUID;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.harvard.cs262.ComputeServer.ComputeServer;
import edu.harvard.cs262.ComputeServer.WorkQueue;
import edu.harvard.cs262.ComputeServer.WorkTask;

public class QueuedServer implements ComputeServer, WorkQueue {

    private Hashtable<UUID, ComputeServer> workers;
    private LinkedList<UUID> freeWorkers, busyWorkers;
    private final Lock lock = new ReentrantLock();

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
        /* TODO We to enact some policy in the case that the worker is busy
         * Dead simple suggestion is just to wait until it's done and then
         * remove it from the queue.  */

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

        /* We want to wait for workers. Our lock prevents synchronization
         * issues around multiple threads trying to modify free worker queue.
         * */
        lock.lock();
        while (freeWorkers.size() == 0) {
            lock.unlock();
            try {
                Thread.sleep(2000);
            } catch(InterruptedException ex) {
               Thread.currentThread().interrupt();
            }
            lock.lock();

            /* If we can't ping the server, then it's down and we shoudln't
             * send it work. TODO what if it's "down" but it just stalls. We
             * should catch timeouts... */
            try {
                freeWorkers.peek().pingServer();
            } catch (Exception e) {
                freeWorkers.remove();
            }
        }

        UUID workerID = freeWorkers.remove();
        lock.unlock();

        busyWorkers.add(workerID);
        ComputeServer worker = workers.get(workerID);
        Object response = worker.sendWork(work);

        busyWorkers.remove(workerID);
        freeWorkers.add(workerID);

        return response;
    }

    @Override
    public boolean PingServer() throws RemoteException {
        return true;
    }

    public static void main(String args[]){

        int port        = 1099; // Port this server is running on
        String hostname = "rinchiera.com"; // Hostname this server is running on

        try {

            /* This policy is very unsafe */
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager()==null){
                System.setSecurityManager(new SecurityManager());
            }

            /* Make server availible to clients and compute servers */
            QueuedServer queuedServer = new QueuedServer();
            WorkQueue stub = (WorkQueue) UnicastRemoteObject.exportObject(queuedServer, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("QueuedServer", stub);

            System.out.println("WorkQueue is ready.");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
