package io.kalishak.controller;

import ca.fiercest.cuesdk.CueSDK;
import ca.fiercest.cuesdk.NoServerException;
import io.kalishak.Main;
import io.kalishak.comm.Communicator;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class ICueController implements Controller {
    private final @Nullable Path defaultConfigurationPath;
    private final long maxDelay;
    private CueSDK cue;

    public ICueController(@Nullable Path defaultConfigurationPath, long maxDelay) {
        this.defaultConfigurationPath = defaultConfigurationPath;
        this.maxDelay = maxDelay;
    }

    @Override
    public boolean setup(Communicator comm) {
        try {
            this.cue = new CueSDK(true);
            return true;
        } catch (NoServerException e) {
            Main.LOGGER.error("Error during iCue setup: ", e);
        }

        return false;
    }

    @Override
    public void run(Communicator comm) {

    }

    @Override
    public boolean stop(Communicator comm) {
        return false;
    }

    @Override
    public String toString() {
        return "Corsair iCue";
    }
}
