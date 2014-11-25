/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inftt.framework;

import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global command executor.
 * <p/>
 * Created by Sam on 2014/11/24.
 */
public class GlobalExePool {

    transient static Logger log = Logger.getLogger(GlobalExePool.class.getName());

    /**
     * This is the core thread pool. key stands for the executor name, so base on this
     * multiple executor is supported.
     */
    static Map<String, ThreadPoolExecutor> corePools = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    /**
     * block external queue, the runnable queue only can be created by GlobalExePool
     */
    static Map<String, BlockingQueue<Runnable>> coreQueues = new ConcurrentHashMap<String, BlockingQueue<Runnable>>();


    /**
     * Create one execute pool using the name poolId.
     *
     * @param poolId          pool flag, as well as pool name.
     * @param corePoolSize    core pool size
     * @param maximumPoolSize maximum pool size
     * @param keepAliveTime   keep alive time
     * @param unit            unit
     */
    public static void createOnePool(String poolId, int corePoolSize,
                                     int maximumPoolSize, long keepAliveTime,
                                     TimeUnit unit) {
        if (hasBeenConstructed(poolId)) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, "Attention!! Executor pool named {0} has already been created!!", poolId);
            }
            return;
        }
        createOneQueue(poolId);
        ThreadPoolExecutor tpe =
                new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, createOneQueue(poolId));
        corePools.put(poolId, tpe);
    }

    /**
     * Create inner runnable queue.
     *
     * @param poolId pool flag, as well as pool name.
     */
    static BlockingQueue<Runnable> createOneQueue(String poolId) {
        BlockingQueue<Runnable> queue = coreQueues.get(poolId);
        if (null == queue) {
            queue = new LinkedBlockingQueue<Runnable>();
            coreQueues.put(poolId, queue);
        }
        return queue;
    }

    /**
     * Consider that the executor pool has been created yet.
     *
     * @param poolId pool flag, as well as pool name.
     * @return if the executor pool named poolId has been created, return true.
     * else return false.
     */
    static boolean hasBeenConstructed(String poolId) {
        return corePools.containsKey(poolId) && null != corePools.get(poolId);
    }

    /**
     * Put one runnable command into executor pool queue.
     *
     * @param poolId  pool flag, as well as pool name.
     * @param command runnable command.
     */
    public static void executeCommand(String poolId, Runnable command) {
        if (!hasBeenConstructed(poolId)) {
            throw new RuntimeException("No executor found naming by" + poolId);
        }
        corePools.get(poolId).execute(command);
    }

    /**
     * Pick up thread executor pool.
     *
     * @param poolId pool flag, as well as pool name.
     * @return Thread Executor Pool
     */
    public static ThreadPoolExecutor pickUpExePool(String poolId) {
        return corePools.get(poolId);
    }

    /**
     * shutdown the thread pool executor. If current executor pool contains unhandled command,
     * then try to sleep 2 seconds, after that retry to shutdown the executor. This kind of circle
     * process will not stop util the executor is shutdown successfully.
     *
     * @param poolId pool flag, as well as pool name.
     */
    public static void shutdown(String poolId) {
        if (hasBeenConstructed(poolId)) {
            ThreadPoolExecutor tpe = corePools.get(poolId);
            while (true) {
                if (tpe.getActiveCount() == 0 && tpe.getQueue().size() == 0) {
                    tpe.shutdown();
                    corePools.remove(poolId);
                    coreQueues.remove(poolId);
                    if (log.isLoggable(Level.INFO)) {
                        log.log(Level.INFO, "Thread executor pool named {0} shutdown successfully.", poolId);
                    }
                    break;
                } else {
                    log.log(Level.WARNING,
                            "Commands remaining in executor pool {0}, retry shutdown in 2 seconds.", poolId);
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        throw new RuntimeException("Try to sleep error.");
                    }
                }
            }
        }
    }
}
