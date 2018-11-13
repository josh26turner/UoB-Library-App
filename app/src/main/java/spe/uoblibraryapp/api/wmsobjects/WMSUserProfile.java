package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffService;
import spe.uoblibraryapp.api.ncip.WMSNCIPPatronService;

public class WMSUserProfile {

    private String userId = null;


    private List<WMSLoan> loans;
    private List<WMSRequest> onHold;
    private List<WMSRequest> recentlyRecieved;
    private List<WMSFine> fines;

    private WMSNCIPPatronService patronService;
    private WMSNCIPStaffService staffService;



    public WMSUserProfile(
            WMSNCIPElement elemHolder,
            WMSNCIPPatronService patronService,
            WMSNCIPStaffService staffService
    ) throws WMSParseException{

        // Save Services
        this.patronService = patronService;
        this.staffService = staffService;



        // TODO: Run some checks on elem to ensure it is correct.
        Node node = elemHolder.getElem();

        // Check the node contains the correct data, before parsing.





        // Parse the node into all the attributes associated with the user.
        this.parseNode(node);
    }

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
    public List<WMSRequest> getOnHold(){
        return this.onHold;
    }

    /**
     * Gets all requests the the user has
     * @return
     */
    public List<WMSRequest> getRecentlyRecieved(){
        return this.recentlyRecieved;
    }

    public WMSCheckout checkoutBook(String bookId){
        // TODO use staff service to make checkout request.
        return new WMSCheckout();
    }


    private void parseNode(Node node) throws WMSParseException {
        List<Node> loanNodes = new ArrayList<>();
        List<Node> holdNodes = new ArrayList<>();
        List<Node> recentlyRecievedNodes = new ArrayList<>();
        List<Node> fineNodes = new ArrayList<>();

        // TODO: get nodes children and split into lists

        NodeList childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeName().equals("UserId")) {
                parseUserId(child);
            } else if (child.getNodeName().equals("LoanedItem")) {
                loanNodes.add(child);
            } else if (child.getNodeName().equals("RequestedItem")) {
                // TODO: Add check if request is on hold or recently recieved.
                // Can they be dealt with by the same list?

                holdNodes.add(child);
            } else if (child.getNodeName().equals("Ext")){
                NodeList childsChildren = child.getChildNodes();
                for (int j=0; j<childsChildren.getLength(); j++){
                    Node childsChild = childsChildren.item(j);
                    if (childsChild.getNodeName().equals("UserFiscalAccountSummary")){
                        // TODO: Find example of api response with charges

                    }
                }
            }
        }



        this.loans = parseLoans(loanNodes);
        this.onHold = parseHolds(holdNodes);
        this.recentlyRecieved = parseRecentlyRecieved(recentlyRecievedNodes);
        this.fines = parseFines(fineNodes);
    }

    private void parseUserId(Node userIdNode) throws WMSParseException{
        NodeList children = userIdNode.getChildNodes();
        for (int i=0; i<children.getLength(); i++){
            Node child = children.item(i);
            if (child.getNodeName().equals("UserIdentifierValue")){
                this.userId = child.getTextContent();
                return;
            }
        }
        if (userId == null){
            throw new WMSParseException(); //No UserId element can be found"
        }
    }

    private List<WMSLoan> parseLoans(List<Node> loanNodes) throws WMSParseException {
        List<WMSLoan> loans = new ArrayList<>();
        for (Node loan : loanNodes){
            loans.add(new WMSLoan(new WMSNCIPElement(loan), patronService, staffService));
        }
        return loans;
    }

    private List<WMSRequest> parseHolds(List<Node> holdNodes) throws WMSParseException {
        List<WMSRequest> holds = new ArrayList<>();
        for (Node node : holdNodes){
            holds.add(new WMSRequest(new WMSNCIPElement(node), patronService, staffService));
        }
        return holds;
    }

    private List<WMSRequest> parseRecentlyRecieved(List<Node> recentlyRecievedNodes) throws WMSParseException {
        List<WMSRequest> recentlyRecieved = new ArrayList<>();
        for (Node node : recentlyRecievedNodes) {
            recentlyRecieved.add(new WMSRequest(new WMSNCIPElement(node), patronService, staffService));
        }
        return recentlyRecieved;
    }

    private List<WMSFine> parseFines(List<Node> fineNodes) throws WMSParseException {
        List<WMSFine> fines = new ArrayList<>();
        for (Node node : fineNodes) {
            fines.add(new WMSFine(new WMSNCIPElement(node), patronService, staffService));
        }
        return fines;
    }

}
