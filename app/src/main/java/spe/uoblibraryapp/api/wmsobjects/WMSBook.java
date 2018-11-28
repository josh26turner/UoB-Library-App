package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.Year;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPPatronService;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffService;

/**
 * This represents a book, not a physical copy of a book.
 */
public class WMSBook {

    private WMSNCIPPatronService patronService;
    private WMSNCIPStaffService staffService;


    private String title;
    private String author;
    private String publisher;
    private Integer publicationDate; // TODO: Could this be changed to type Year?
    private String bookId;


    /**
     * Constructor
     * @param elemHolder Contains the node information
     * @param patronService the patron service
     * @param staffService the staff service
     * @throws WMSParseException throws if the book fails to parse
     */
    public WMSBook(WMSNCIPElement elemHolder,
                   WMSNCIPPatronService patronService,
                   WMSNCIPStaffService staffService
    ) throws WMSParseException{

        // Save Services
        this.patronService = patronService;
        this.staffService = staffService;

        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Node element = elemHolder.getElem();

        if (!element.getNodeName().equals("ns1:BibliographicDescription")){
            throw new WMSParseException("WMSBook needs a <ns1:BibliographicDescription> Node");
        }
        // TODO: Extract book details from element and setup object.

        parseElement(element);
    }

    WMSBook(String bookId){
         // TODO: Use search service to find the book details and setup object.
        this(); // Temporary to give values.
        this.bookId = bookId;
    }


    /**
     * DO NOT USE THIS ONE, IT WILL BE DELETED IN BETA BUILD
     */
    private WMSBook(){
        // TODO: THIS IS ONLY TEMPORARY SO IT WORKS FOR JERRY
        title = "Diary of a wimpy kid : dog days ";
        publisher = "New York : Amulet Books";
        publicationDate = 2007;
        bookId = "123456789";
        author = "Shanske, Darien,";
    }

    public String getBookId() {
        return bookId;
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

    public String getAuthor() { return this.author; }




    private void parseElement(Node node){
        NodeList children = node.getChildNodes();
        for (int i=0; i < children.getLength(); i++){
            Node child = children.item(i);
            switch (child.getNodeName()) {
                case "ns1:Author":
                    author = child.getTextContent();
                    break;
                case "ns1:PublicationDate":
                    publicationDate = Integer.valueOf(child.getTextContent());
                    break;
                case "ns1:Publisher":
                    publisher = child.getTextContent();
                    break;
                case "ns1:Title":
                    title = child.getTextContent();
                    break;
                case "ns1:BibliographicRecordId":
                    NodeList childsChildren = child.getChildNodes();
                    for (int x=0; x < childsChildren.getLength(); x++){
                        Node childsChild = childsChildren.item(x);
                        if (childsChild.getNodeName().equals("ns1:BibliographicRecordIdentifier")){
                            bookId = childsChild.getTextContent();
                        }
                    }
                    break;
            }
        }

    }
}
