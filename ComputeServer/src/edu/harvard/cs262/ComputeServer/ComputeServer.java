package edu.harvard.cs262.ComputeServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The {@code Remote} interface for a compute server
 * </p>
 * 
 * This interface defines a remote server that will receive work from client,
 * and return an {@code Object} that encapsulates the results of the work. 
 * @author waldo
 *
 */
public interface ComputeServer extends Remote{
	public Object sendWork(WorkTask work) throws RemoteException;
	

}
