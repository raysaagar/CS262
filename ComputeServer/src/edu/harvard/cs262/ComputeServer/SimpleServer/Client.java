package edu.harvard.cs262.ComputeServer.SimpleServer;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import tasks.Return50;
import tasks.HelloTask;

import edu.harvard.cs262.ComputeServer.ComputeServer;
import edu.harvard.cs262.ComputeServer.WorkTask;

public class Client {

    /**
     * @param args
     */

    public static void main(String[] args) {
        String queuedHostname = "rinchiera.com"; // Hostname of WorkQueue
        int queuedPort        = 1099; // Port of WorkQueue
        Registry registry     = null; // Necessary for compile

        /* If WorkQueue is being operated by a different group */
        if(args.length > 0) {
            queuedHostname = args[0];
        }

        /* This policy is very unsafe. We don't implement remote so it should
         * be fine. */
        System.setProperty("java.security.policy", "security.policy");
        if (System.getSecurityManager()==null){
            System.setSecurityManager(new SecurityManager());
        }

        try {

            try {
                registry = LocateRegistry.getRegistry(queuedHostname, queuedPort);
            }
            catch (Exception e) {
                System.out.println("Unable to connect to WorkQueue server " + queuedHostname + ":" + queuedPort);
                System.out.println(e);
            }

            String name = "QueuedServer";
            ComputeServer comp = (ComputeServer) registry.lookup(name);

            WorkTask work = new Return50("Hello");
            System.out.println("Sending work to server...");
            String response = (String) comp.sendWork(work);
            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }
}
