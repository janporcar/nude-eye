import presentation.Controller;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.WARNING);
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.WARNING);
        }
        Controller controller = new Controller();
        controller.run();
    }
}
