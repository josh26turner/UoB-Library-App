package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

public class WMSUserProfile {

    private String userId = null;


    private List<WMSLoan> loans;
    private List<WMSHold> onHold;
    private List<WMSHold> recentlyReceived;
    private List<WMSFine> fines;



    /**
     * Constructor
     * @param elemHolder This contains the node to parse
//     * @param patronService The patron service
//     * @param staffService The staff service
     * @throws WMSParseException Thrown if there was an error parsing the node
     */
    public WMSUserProfile(
            WMSNCIPElement elemHolder
    ) throws WMSParseException{



        Node node = elemHolder.getElem();

        // Check the node contains the correct data, before parsing.

        if (!node.getNodeName().equals("ns1:LookupUserResponse")){
            throw new WMSParseException("WMSUserProfile requires a <ns1:LookupUserResponse> Node");
        }

        // Parse the node into all the attributes associated with the user.
        this.parseNode(node);
    }



    // Parsers for nodes

    private void parseNode(Node node) throws WMSParseException {
        List<Node> loanNodes = new ArrayList<>();
        List<Node> holdNodes = new ArrayList<>();
        List<Node> recentlyReceivedNodes = new ArrayList<>(); // Will this be needed TODO!
        List<Node> fineNodes = new ArrayList<>();

        NodeList childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            switch (child.getNodeName()) {
                case "ns1:UserId":
                    parseUserId(child);
                    break;
                case "ns1:LoanedItem":
                    loanNodes.add(child);
                    break;
                case "ns1:RequestedItem":
                    // TODO: Add check if request is on hold or recently recieved.
                    // Can they be dealt with by the same list?
                    holdNodes.add(child);
                    break;
                case "ns1:Ext":
                    NodeList childsChildren = child.getChildNodes();
                    for (int j=0; j<childsChildren.getLength(); j++){
                        Node childsChild = childsChildren.item(j);
                        if (childsChild.getNodeName().equals("UserFiscalAccountSummary")){
                            // TODO: Find example of api response with charges
                            // Implement this once we know, not a high priority until final build.
                        }
                    }
                    break;
            }
        }



        this.loans = parseLoans(loanNodes);
        this.onHold = parseHolds(holdNodes);
        this.recentlyReceived = parseRecentlyReceived(recentlyReceivedNodes);
        this.fines = parseFines(fineNodes);
    }

    private void parseUserId(Node userIdNode) throws WMSParseException{
        NodeList children = userIdNode.getChildNodes();
        for (int i=0; i<children.getLength(); i++){
            Node child = children.item(i);
            if (child.getNodeName().equals("ns1:UserIdentifierValue")){
                this.userId = child.getTextContent();
                return;
            }
        }
        if (this.userId == null){
            throw new WMSParseException("No UserId element can be found");
        }
    }

    private List<WMSLoan> parseLoans(List<Node> loanNodes) throws WMSParseException {
        List<WMSLoan> loans = new ArrayList<>();
        for (Node loan : loanNodes){
            loans.add(new WMSLoan(new WMSNCIPElement(loan)));
        }
        return loans;
    }

    private List<WMSHold> parseHolds(List<Node> holdNodes) throws WMSParseException {
        List<WMSHold> holds = new ArrayList<>();
        for (Node node : holdNodes){
            holds.add(new WMSHold(new WMSNCIPElement(node)));
        }
        return holds;
    }

    private List<WMSHold> parseRecentlyReceived(List<Node> recentlyReceivedNodes) throws WMSParseException {
        List<WMSHold> recentlyReceived = new ArrayList<>();
        for (Node node : recentlyReceivedNodes) {
            recentlyReceived.add(new WMSHold(new WMSNCIPElement(node)));
        }
        return recentlyReceived;
    }

    private List<WMSFine> parseFines(List<Node> fineNodes) throws WMSParseException {
        List<WMSFine> fines = new ArrayList<>();
        for (Node node : fineNodes) {
            fines.add(new WMSFine(new WMSNCIPElement(node)));
        }
        return fines;
    }




    // Getters & other methods for UI people

    /**
     * Gets a users fines
     * @return Returns a list of all fines the user has.
     */
    public List<WMSFine> getFines(){
        return this.fines;
    }

    /**
     * Gets all books a user has on loan.
     * @return Returns a list of all loans the user has
     */
    public List<WMSLoan> getLoans(){
        return this.loans;
    }

    /**
     * Gets all requests that the user has made with are on hold.
     * @return Returns a list of requests
     */
    public List<WMSHold> getOnHold(){
        return this.onHold;
    }

    /**
     * Gets all requests the the user has recently made, and not actioned yet by the library.
     * @return Returns a list of requests
     */
    public List<WMSHold> getRecentlyReceived(){
        return this.recentlyReceived;
    }

    /**
     * Gets the userId
     * @return the userId
     */
    public String getUserId(){
        return this.userId;
    }

}
