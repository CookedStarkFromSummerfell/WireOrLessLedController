package io.kalishak.controller;

import com.razer.java.JAppInfoType;
import com.razer.java.JChromaSDK;
import com.razer.java.sampleapp.ChromaEffects;
import io.kalishak.Main;
import io.kalishak.comm.Communicator;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

public class ChromaController extends ChromaEffects implements Controller {
    private final @Nullable Path defaultConfigurationPath;
    private final long maxDelay;

    private boolean waitForExit = true;
    private boolean isInitialised = false;

    public ChromaController(@Nullable Path defaultConfigurationPath, long maxDelay) {
        this.defaultConfigurationPath = defaultConfigurationPath;
        this.maxDelay = maxDelay;
    }

    @Override
    public boolean setup(Communicator comm) {
        Main.LOGGER.info("Initialising Chroma Controller");

        sChromaAnimationAPI = JChromaSDK.getInstance();

        JAppInfoType appInfo = new JAppInfoType();

        appInfo.setTitle("WireOrLessController");
        appInfo.setDescription("Ambient lightning application using Razer Chroma SDK");
        appInfo.setAuthorName("Kalishak");

        appInfo.supportedDevice = (0x01 | 0x04 | 0x20);
        appInfo.category = 2;

        int result = sChromaAnimationAPI.initSDK(appInfo);

        if (result == 0) {
            isInitialised = true;
            Main.LOGGER.info("Initialized ChromaSDK.");
        } else {
            Main.LOGGER.error("Failed to initialize ChromaSDK, error: " + result);
            Main.LOGGER.info("Closing application.");
        }

        return isInitialised;
    }

    @Override
    public void run(Communicator comm) {

    }

    @Override
    public boolean stop(Communicator comm) {
        if (!isInitialised) {
            return false;
        }

        waitForExit = false;
        Timer timer = new Timer("Timer");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (isInitialised) {
                        sChromaAnimationAPI.stopAll();
                        sChromaAnimationAPI.closeAll();
                        sChromaAnimationAPI.uninit();
                        isInitialised = false;
                    }

                    sChromaAnimationAPI = null;
                } catch (Exception e) {
                    Main.LOGGER.error("Caught error during stopping Chroma SDK: ", e);
                }
            }
        };

        timer.schedule(task, 100);

        return true;
    }

    @Override
    public String toString() {
        return "Razer Chroma";
    }
}
