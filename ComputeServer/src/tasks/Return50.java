package tasks;

import edu.harvard.cs262.ComputeServer.WorkTask;
import java.io.Serializable;

public class Return50 implements WorkTask, Serializable {

    String str;
    private static final long serialVersionUID = 9999L;

    public Return50(String str) {
        this.str = str;
    }

    public String doWork() {
        return this.str + " universe!";
    }
}
