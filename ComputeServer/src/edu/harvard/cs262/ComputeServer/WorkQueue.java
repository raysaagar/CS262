/**
 * 
 */
package edu.harvard.cs262.ComputeServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * A WorkQueue is a server that allows other {@code ComputeServer} to register with it, and then hands out the 
 * work to all the registered servers</p>
 * 
 * This type of server may not do any computation itself, but will allow elastic scaling of the compute server
 * infrastructure by allowing others to register and do work.</p>
 *
 */
public interface WorkQueue extends Remote{
	public UUID registerWorker(ComputeServer server)
		throws RemoteException;
	public boolean unregisterWorker(UUID workerID)
		throws RemoteException;
}
