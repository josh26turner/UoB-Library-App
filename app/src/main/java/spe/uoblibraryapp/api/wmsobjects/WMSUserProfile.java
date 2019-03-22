package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

public class WMSUserProfile {

    private String userId;
    private List<WMSLoan> loans;
    private List<WMSHold> onHold;


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

        NodeList childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            switch (child.getNodeName()) {
                case "ns1:LoanedItem":
                    loanNodes.add(child);
                    break;
                case "ns1:RequestedItem":
                    holdNodes.add(child);
                    break;
            }
        }



        this.loans = parseLoans(loanNodes);
        this.onHold = parseHolds(holdNodes);
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




    // Getters & other methods for UI people

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



}
