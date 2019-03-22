package spe.uoblibraryapp.api.wmsobjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import spe.uoblibraryapp.Constants;
import spe.uoblibraryapp.FragmentLoans;
import spe.uoblibraryapp.api.XMLParser;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

import static android.content.Context.MODE_PRIVATE;


/**
 * This is used to represent a loan, from WMS.
 */
public class WMSLoan {
    private final static String TAG = "WMSLoan";

    private String itemId;
    private String agencyId;
    private WMSBook book;
    private Date dueDate;
    private Date checkedOutDate;
    private Integer renewalCount;
    private Integer reminderLevel;
    private String mediumType;
    private Boolean isRenewable;
    private Context isRenewableTextViewContext;

    /**
     * Constructor
     * @param elemHolder This contains the node information
     * @throws WMSParseException throws if the node fails to parse
     */
    WMSLoan(WMSNCIPElement elemHolder) throws WMSParseException{

        // Get Node.
        Node element = elemHolder.getElem();

        // Check correct node is passed to WMSLoan
        if (!element.getNodeName().equals("ns1:LoanedItem")){
            throw new WMSParseException("Node is not correct loan node");
        }

        // Parse the node :o
        try {
            parseNode(element);
        } catch (ParseException e){
            throw new WMSParseException(e.getMessage());
        }
    }


    /**
     * To parse WMS dates into Java Dates
     * @param strDate this is a date from WMS
     * @return This returns the Java Date object from the parsed date
     * @throws ParseException Throws if the date fails to parse
     */
    private Date parseDate(String strDate) throws ParseException{
        String formattedStrDate = strDate.replace("T", "-").replace("Z", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return format.parse(formattedStrDate);
    }

    /**
     * This is for parsing the node during initialisation
     * @param node The top level node to be parsed
     * @throws ParseException thrown if a date fails to parse
     * @throws WMSParseException thrown if the WMSBook object fails to parse
     */
    private void parseNode(Node node) throws ParseException, WMSParseException{
        NodeList children = node.getChildNodes();
        for (int i=0; i<children.getLength(); i++){
            Node child = children.item(i);
            switch (child.getNodeName()){
                case "ns1:ItemId":
                    NodeList childsChildren = child.getChildNodes();
                    for (int x=0; x<childsChildren.getLength(); x++){
                        Node childsChild = childsChildren.item(x);
                        switch (childsChild.getNodeName()){
                            case "ns1:ItemIdentifierValue":
                                itemId = childsChild.getTextContent();
                                break;
                            case "ns1:AgencyId":
                                agencyId = childsChild.getTextContent();
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case "ns1:ReminderLevel":
                    reminderLevel = Integer.valueOf(child.getTextContent());
                    break;
                case "ns1:DateDue":
                    dueDate = parseDate(child.getTextContent());
                    break;
                case "ns1:MediumType":
                    mediumType = child.getTextContent();
                    break;
                case "ns1:Ext":
                    NodeList childsChildren2 = child.getChildNodes();
                    for (int x=0; x<childsChildren2.getLength(); x++){
                        Node childsChild = childsChildren2.item(x);
                        switch (childsChild.getNodeName()){
                            case "ns1:RenewalCount":
                                renewalCount = Integer.valueOf(childsChild.getTextContent());
                                break;
                            case "ns1:DateCheckedOut":
                                checkedOutDate = parseDate(childsChild.getTextContent());
                                break;
                            case "ns1:BibliographicDescription":
                                book = new WMSBook(new WMSNCIPElement(childsChild));
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }



    // Getters for UI people to use

    /**
     * This gets the WMSBook object for the book a loan is concerning
     * @return The WMSBook object
     */
    public WMSBook getBook(){
        return this.book;
    }

    /**
     * Gets the due date for the loan
     * @return the due date
     */
    public Date getDueDate(){
        return this.dueDate;
    }

    /**
     * Gets the date that the book was checked out
     * @return the checked out date
     */
    public Date getCheckedOutDate(){
        return this.checkedOutDate;
    }

    /**
     * Gets the renewal count for the loan
     * @return the renewal count
     */
    public Integer getRenewalCount(){
        return this.renewalCount;
    }

    /**
     * Gets the reminder level for a loan
     * @return the reminder level
     */
    public Integer getReminderLevel() {
        return this.reminderLevel;
    }

    /**
     * Gets the item id that the loan is for (identifies a specific book)
     * @return the item id
     */
    public String getItemId() {
        return this.itemId;
    }

    /**
     * Gets the agency id the loan was made from (should always be the same)
     * @return the agency id
     */
    public String getAgencyId() {
        return this.agencyId;
    }

    /**
     * Gets the medium type (book, ... etc)
     * @return the medium type
     */
    public String getMediumType() {
        return this.mediumType;
    }

    public Boolean getRenewable() {
        return isRenewable;
    }

    public void setIsRenewable(Boolean isRenewable){
        this.isRenewable = isRenewable;
        FragmentLoans.listViewAdapter.notifyDataSetChanged();
    }

    /**
     * Gets renewal status, if the book can be renewed
     * @return if can be renewed
     */
    public String getRenewableStatus() {
        return "Fetching...";
    }

    public Boolean isOverdue() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dueDate);
        cal.add(Calendar.DATE, 1);
        return cal.getTime().before(new Date());
    }

    public void fetchIsRenewable(Context context){
        isRenewableTextViewContext = context;
        new GetRenewStatus().execute();
    }


    private class GetRenewStatus extends AsyncTask<URL, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(URL... urls) {
            RequestQueue requestQueue = Volley.newRequestQueue(isRenewableTextViewContext);
            SharedPreferences prefs = isRenewableTextViewContext.getSharedPreferences("userDetails", MODE_PRIVATE);
            String accessToken = prefs.getString("authorisationToken", "");
            String url = String.format(Constants.APIUrls.bookAvailability, getBook().getBookId());

            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest request = new StringRequest(Request.Method.GET, url, future, future) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                }
            };
            requestQueue.add(request);
            boolean result;
            try{
                String response = future.get(30, TimeUnit.SECONDS);
                result = parseResponse(response);
            } catch (InterruptedException e){
                Log.d(TAG , "Iterrupt for " + getBook().getBookId());
                result = false;
            } catch (ExecutionException e){
                Log.d(TAG , "Execution for " + getBook().getBookId());
                result = false;
            } catch (TimeoutException e){
                Log.d(TAG , "Timeout for " + getBook().getBookId());
                result = false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            WMSLoan.this.setIsRenewable(result);
        }

        private Boolean parseResponse(String xml){
            try {
                Document doc = XMLParser.parse(xml);
                NodeList itemIdList = doc.getElementsByTagName("itemId");
                NodeList renewableList = doc.getElementsByTagName("renewable");
                if(itemIdList.getLength() == 1){
                    Node renewableNode = renewableList.item(0);
                    Node value = renewableNode.getAttributes().getNamedItem("value");
                    return value.getNodeValue().equals("1");
                }
                for (int i=0; i<itemIdList.getLength(); i++){
                    Node node = itemIdList.item(i);
                    //Log.e(TAG, node.getTextContent());
                    //Log.e(TAG, itemId);
                    if (node.getTextContent().equals(itemId)){
                        Node renewableNode = renewableList.item(i);
                        Node value = renewableNode.getAttributes().getNamedItem("value");

                        return value.getNodeValue().equals("1");
                    }
                    //Log.e(TAG, node.getNodeName());
                }

            } catch (Exception ex){
                ex.printStackTrace();
            }
            return false;
        }
    }

}
