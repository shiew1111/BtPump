package com.ionicframework.BtPumpPlugin;

/**
 * Created by fishermen21 on 19.05.17.
 */

public interface DisplayUpdater {
    void clear();

    void update(byte[] quarter, int which);
}
