package io.kalishak.controller;

import ca.fiercest.aurasdk.AuraSDK;
import io.kalishak.comm.Communicator;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class AuraController implements Controller {
    private final @Nullable Path defaultConfigurationPath;
    private final long maxDelay;
    private AuraSDK aura;

    public AuraController(@Nullable Path defaultConfigurationPath, long maxDelay) {
        this.defaultConfigurationPath = defaultConfigurationPath;
        this.maxDelay = maxDelay;
    }

    @Override
    public boolean setup(Communicator comm) {
        this.aura = new AuraSDK();

        return true;
    }

    @Override
    public void run(Communicator comm) {

    }

    @Override
    public boolean stop(Communicator comm) {
        this.aura.ReleaseControl();

        return true;
    }

    @Override
    public String toString() {
        return "Asus Aura";
    }
}
