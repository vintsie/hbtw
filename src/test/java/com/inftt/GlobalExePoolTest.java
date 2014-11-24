package com.inftt;

import com.inftt.framework.GlobalExePool;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Global executor pool testing
 * <p/>
 * Created by Sam on 2014/11/24.
 */
public class GlobalExePoolTest {

    @Test
    public void testExePool() throws Exception {
        GlobalExePool.createOnePool("m1", 5, 200, 60, TimeUnit.SECONDS);
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new ExePoolTestRunnable(i);
            GlobalExePool.executeCommand("m1", runnable);
        }
        GlobalExePool.shutdown("m1");
    }
}

class ExePoolTestRunnable implements Runnable {
    int id = -1;

    public ExePoolTestRunnable(int i) {
        id = i;
    }

    @Override
    public void run() {
        System.out.println(id);
    }
}
