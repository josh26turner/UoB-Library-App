package spe.uoblibraryapp.api.ncip;

import java.util.LinkedList;
import java.util.Queue;

public class WorkQueue {
    private static final WorkQueue ourInstance = new WorkQueue();
    private Queue<String> workQueue;

    static WorkQueue getInstance() {
        return ourInstance;
    }

    private WorkQueue() {
        workQueue = new LinkedList<>();
    }

    public String get(){
        return workQueue.remove();
    }

    public void add(String value){
        workQueue.add(value);
    }

    boolean isEmpty(){
        return workQueue.isEmpty();
    }
}
