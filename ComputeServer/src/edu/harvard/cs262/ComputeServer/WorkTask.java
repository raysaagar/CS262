package edu.harvard.cs262.ComputeServer;

/**
 * A WorkTask is the general interface to a closure that can be passed around to
 * be done on some other machine</p>
 * 
 * The WorkTask will need to be self-contained; that is, it needs to contain all
 * of the data that will be needed for the work (or handles in a
 * location-independent form that allows the data to be found) and the code that
 * actually does the work. It returns an Object, which the receiver will need
 * to cast into the correct form. There is a single method, {@code doWork()}, 
 * that will be called by the receiver of the object to get the work done.
 * 
 * @author waldo
 * 
 */
public interface WorkTask {
	public Object doWork();
}
