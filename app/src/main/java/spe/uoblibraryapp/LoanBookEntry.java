package spe.uoblibraryapp;



public class LoanBookEntry {
    private String title;
    private String author;
    private BookStatus status;

    public LoanBookEntry(String title, String author, BookStatus status ){
        this.title = title;
        this.author = author;
        this.status = status;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(){
        this.title= title;
    }
    public String getAuthor(){
        return author;
    }
    public void setAuthor(){
        this.author= author;
    }
    public BookStatus getStatus(){
        return status;
    }
    public void setStatus(){
        this.status= status;
    }

}
