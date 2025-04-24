package com.soybean.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author soybean
 * @date 2025/3/18 13:16
 * @description
 */
public class ServerEachTickTaskManager {

    private static Map<String, EachTickTask> activeTasks = new HashMap<>();
    private static boolean initialized = false;
    public static class EachTickTask{
        public int tickCount; // 任务执行的tick计数 -1为永久执行
        public int interval; // 每一tick执行间隔
        public Runnable callback; // 回调函数
        public boolean completed; // 任务是否已完成
        public int eachTickCount = MAX_EACH_TICK_COUNT; // 如果tickCount=-1，循环利用该函数
        public static int MAX_EACH_TICK_COUNT = 20 * 60 * 60;
        /**
         *
         * @param tickCount 任务执行的tick计数 -1为永久执行
         * @param interval 每一tick执行间隔
         * @param callback 任务是否已完成
         */
        public EachTickTask(int tickCount, int interval, Runnable callback){
            this.tickCount = tickCount;
            this.interval = interval;
            this.callback = callback;
            this.completed = false;
        }

        public EachTickTask(int interval,Runnable callback){ //默认永久执行，自定义间隔
            this(-1,interval,callback);
        }
        public EachTickTask(Runnable callback){ //默认永久执行，每1s执行一次
            this(-1,20,callback);
        }

        public void tick(){
            if(completed) return;
            if(tickCount > -1){
                if(tickCount % interval == 0){
                    callback.run();
                }
                tickCount --;
                if(tickCount <= 0){
                    setCompleted(true);
                }
            }else{
                if(eachTickCount % interval == 0){
                    callback.run();
                }
                eachTickCount --;
                if(eachTickCount <= 0){
                    eachTickCount = MAX_EACH_TICK_COUNT;
                }
            }
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public boolean getCompleted(){
            return completed;
        }
    }

    public static void scheduleTask(String taskId, int tickCount,int interval, Runnable callback) { //默认执行器
        activeTasks.put(taskId, new EachTickTask(tickCount,interval, callback));
    }

    public static void scheduleTask(int tickCount,int interval, Runnable callback) { //默认执行器
        scheduleTask(UUID.randomUUID().toString(),tickCount,interval,callback);
    }

    public static void scheduleTask(int interval, Runnable runnable){ //永久执行器 自定义间隔
        scheduleTask(UUID.randomUUID().toString(),-1 , interval, runnable);
    }

    public static void init() {
        if(!initialized){
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                Iterator<Map.Entry<String, EachTickTask>> iterator = activeTasks.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, EachTickTask> entry = iterator.next();
                    EachTickTask task = entry.getValue();
                    task.tick();
                    if (task.getCompleted()) {
                        iterator.remove();
                    }
                }
            });
            initialized = true;
        }
    }
}
