package io.kalishak;

import io.kalishak.comm.Communicator;
import io.kalishak.comm.serial.SerialCommunicator;
import io.kalishak.comm.wifi.WiFiCommunicator;
import io.kalishak.controller.AuraController;
import io.kalishak.controller.ChromaController;
import io.kalishak.controller.Controller;
import io.kalishak.controller.ICueController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


public class Main {
    public static final Logger LOGGER = LoggerFactory.getLogger("LED-Controller");
    public static final boolean ERRORS_DEFAULT = true;
    public static final long DEFAULT_DELAY = 1000L;

    public static Communicator.Factory<?> communicatorMaker;
    public static Controller.Factory<?> controllerMaker;

    public static void main(String[] args) {
        System.out.println("Hello world!");

        chooseCommunicator(communicatorTypeFromString(args));
        chooseController(controllerTypeFromString(args));

        Communicator communicator = communicatorMaker.create(ERRORS_DEFAULT, DEFAULT_DELAY);
        Controller controller = controllerMaker.create(null, DEFAULT_DELAY);
    }

    private static Communicator.Type communicatorTypeFromString(String[] args) {
        if (args.length > 0) {
            if (args[0].contains("communicator")) {
                String[] typeOf = args[0].split("=");

                for (Communicator.Type type : Communicator.Type.values()) {
                    if (type.name().toLowerCase(Locale.ROOT).equals(typeOf[1])) {
                        return type;
                    }
                }
            }
        }

        return Communicator.Type.NONE;
    }

    private static Controller.Type controllerTypeFromString(String[] args) {
        if (args.length > 1) {
            if (args[1].contains("controller")) {
                String[] typeOf = args[1].split("=");

                for (Controller.Type type : Controller.Type.values()) {
                    if (type.name().toLowerCase(Locale.ROOT).equals(typeOf[1])) {
                        return type;
                    }
                }
            }
        }

        return Controller.Type.NONE;
    }

    private static void chooseCommunicator(Communicator.Type type) {
        if (type == Communicator.Type.NONE) {
            Main.LOGGER.error("Cannot initialize none communicator!");
            return;
        }

        if (type == Communicator.Type.SERIAL) {
            communicatorMaker = SerialCommunicator::new;
        } else {
            communicatorMaker = WiFiCommunicator::new;
        }
    }

    private static void chooseController(Controller.Type type) {
        if (type == Controller.Type.NONE) {
            Main.LOGGER.error("Cannot initialize none controller!");
            return;
        }

        controllerMaker = switch (type) {
            case RAZER_CHROMA -> ChromaController::new;
            case ASUS_AURA -> AuraController::new;
            case CORSAIR_iCUE -> ICueController::new;
            default -> null;
        };
    }
}