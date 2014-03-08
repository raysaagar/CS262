package tasks;

import edu.harvard.cs262.ComputeServer.WorkTask;
import java.io.Serializable;

public class HelloTask implements WorkTask, Serializable {

    String str;

    public HelloTask(String str) {
        this.str = str;
    }

    public Object doWork() {
        return this.str + " world!";
    }
}
