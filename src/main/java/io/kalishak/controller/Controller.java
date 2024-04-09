package io.kalishak.controller;

import io.kalishak.comm.Communicator;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public interface Controller {
    boolean setup(Communicator comm);

    void run(Communicator comm);

    boolean stop(Communicator comm);

    @Override
    String toString();

    enum Type {
        RAZER_CHROMA,
        ASUS_AURA,
        CORSAIR_iCUE,
        NONE
    }

    @FunctionalInterface
    interface Factory<C extends Controller> {
        C create(@Nullable Path defaultConfigurationPath, long maxDelay);
    }
}
