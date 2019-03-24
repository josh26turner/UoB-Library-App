package spe.uoblibraryapp.api.ncip;

import android.os.Bundle;

import java.util.LinkedList;
import java.util.Queue;

public class WorkQueue {
    private static final WorkQueue ourInstance = new WorkQueue();
    private Queue<WorkQueueObject> workQueue;

    static WorkQueue getInstance() {
        return ourInstance;
    }

    private WorkQueue() {
        workQueue = new LinkedList<>();
    }

    public WorkQueueObject get(){
        return workQueue.remove();
    }

    public void add(String value, Bundle extras){
        workQueue.add(new WorkQueueObject(value, extras));
    }

    public boolean isEmpty(){
        return workQueue.isEmpty();
    }

    public void clear(){
        workQueue.clear();
    }

    class WorkQueueObject{
        private String action;
        private Bundle extras;

        WorkQueueObject(String action, Bundle extras){
            this.action = action;
            this.extras = extras;
        }

        public Bundle getExtras() {
            return extras;
        }
        public String getAction() {
            return action;
        }
    }
}
