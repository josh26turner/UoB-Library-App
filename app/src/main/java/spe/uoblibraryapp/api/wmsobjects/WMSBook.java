package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPPatronService;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffService;

/**
 * This represents a book, not a physical copy of a book.
 */
public class WMSBook {

    private String title;
    private String author;
    private String publisher;
    private Integer publicationDate; // TODO: Could this be changed to type Year?
    private String bookId;


    /**
     * Constructor
     * @param elemHolder Contains the node information
     * @throws WMSParseException throws if the book fails to parse
     */
    public WMSBook(WMSNCIPElement elemHolder) throws WMSParseException{


        Node element = elemHolder.getElem();

        if (!element.getNodeName().equals("ns1:BibliographicDescription")){
            throw new WMSParseException("WMSBook needs a <ns1:BibliographicDescription> Node");
        }

        parseNCIPElement(element);
    }

    /**
     * Constructor
     * This will create a book from the oclc number
     * @param bookId The OCLC number for a book
     */
    WMSBook(String bookId){
         // TODO: Use search service to find the book details and setup object.
        this(); // Temporary to give values.
        this.bookId = bookId;
    }


    /**
     * Constructor
     * DO NOT USE THIS ONE, only used for testing.
     */
    private WMSBook(){
        // TODO: THIS IS ONLY TEMPORARY SO IT WORKS FOR JERRY
        title = "Diary of a wimpy kid : dog days";
        publisher = "New York : Amulet Books";
        publicationDate = 2007;
        bookId = "123456789";
        author = "Shanske, Darien,";
    }


    // Parsers

    /**
     * Parses a NCIP node and populates the object with data
     * @param node the Node to parse
     */
    private void parseNCIPElement(Node node){
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




    // Getters for UI people

    /**
     * Gets the OCLC Number for the book
     * @return The OCLC Number
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Gets the title for the book
     * @return the title
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Gets the publisher for the book
     * @return the publisher
     */
    public String getPublisher(){
        return this.publisher;
    }

    /**
     * Gets the publication date (year)
     * @return the publication date
     */
    public Integer getPublicationDate(){
        return this.publicationDate;
    }

    /**
     * Gets the author of the book
     * @return the author
     */
    public String getAuthor() {
        return this.author;
    }
}
