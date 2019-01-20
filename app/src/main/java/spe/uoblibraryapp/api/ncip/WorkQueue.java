package spe.uoblibraryapp.api.ncip;

import java.util.LinkedList;
import java.util.Queue;

public class WorkQueue {
    private static final WorkQueue ourInstance = new WorkQueue();

    public static WorkQueue getInstance() {
        return ourInstance;
    }

    private Queue<String> workQueue;
    private WorkQueue() {
        workQueue = new LinkedList<>();
    }

    String get(){
        return workQueue.remove();
    }

    void add(String value){
        workQueue.add(value);
    }

    boolean isEmpty(){
        return workQueue.isEmpty();
    }

    int size(){
        return workQueue.size();
    }
}
