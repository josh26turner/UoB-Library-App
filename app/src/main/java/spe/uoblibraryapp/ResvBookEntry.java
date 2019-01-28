package spe.uoblibraryapp;

public class ResvBookEntry {
    private String title;
    private String author;
    private Integer queuePos;
    private Integer queueLength;
    private String collectLocation;
    private Boolean collectReady;

    public ResvBookEntry(String title, String author, Integer pos, Integer len, String loc, Boolean collectReady){
        this.title = title;
        this.author = author;
        this.queuePos = pos;
        this.queueLength = len;
        this.collectLocation = loc;
        this.collectReady = collectReady;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public Integer getQueuePos(){
        return this.queuePos;
    }

    public Integer getQueueLength(){
        return this.queueLength;
    }

    public String getCollectLocation(){
        return this.collectLocation;
    }

    public Boolean getCollectReady(){
        return this.collectReady;
    }
}
