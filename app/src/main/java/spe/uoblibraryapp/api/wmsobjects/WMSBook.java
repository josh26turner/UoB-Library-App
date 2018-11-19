package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;

import java.time.Year;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPPatronService;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffService;

public class WMSBook {

    private WMSNCIPPatronService patronService;
    private WMSNCIPStaffService staffService;

    private String title;
    private String publisher;
    private Integer publicationDate; // TODO: Could this be changed to type Year?



    public WMSBook(WMSNCIPElement elemHolder,
                   WMSNCIPPatronService patronService,
                   WMSNCIPStaffService staffService
    ) throws WMSParseException{

        // Save Services
        this.patronService = patronService;
        this.staffService = staffService;

        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Node element = elemHolder.getElem();
        // TODO: Extract book details from element and setup object.
    }

    WMSBook(String bookId){
         // TODO: Use search service to find the book details and setup object.
    }


    /**
     * DO NOT USE THIS ONE, IT WILL BE DELETED IN BETA BUILD
     */
    WMSBook(){
        // TODO: THIS IS ONLY TEMPORARY SO IT WORKS FOR JERRY
        title = "Diary of a wimpy kid : dog days ";
        publisher = "New York : Amulet Books";
        publicationDate = 2007;
    }

    public String getTitle(){
        return this.title;
    }

    public String getPublisher(){
        return this.publisher;
    }

    public Integer getPublicationDate(){
        return this.publicationDate;
    }

}
