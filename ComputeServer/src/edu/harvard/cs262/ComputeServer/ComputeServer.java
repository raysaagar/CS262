package edu.harvard.cs262.ComputeServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The {@code Remote} interface for a compute server </p>
 * 
 * This interface defines a remote server that will receive work from client,
 * and return an {@code Object} that encapsulates the results of the work.
 * 
 * 
 */
public interface ComputeServer extends Remote {
	/**
	 * Get a {@link WorkTask} object, which will be performed
	 * 
	 * @param work
	 *            a {@link WorkTask} object, essentially a {@link Serializeable}
	 *            object that encapsulates a closure of some work that needs to
	 *            be done
	 * @return an {@link Object} that encapsulates the result of the work that
	 *         has been performed
	 * @throws RemoteException
	 */
	public Object sendWork(WorkTask work) throws RemoteException;

}
