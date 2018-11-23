package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;

import java.util.Date;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

public class WMSLoan {

    private WMSBook book;
    private Date dueDate;
    private Date checkedOutDate;
    private Integer renewalCount;

    WMSLoan(WMSNCIPElement elemHolder) throws WMSParseException{

        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Node element = elemHolder.getElem();
        book = new WMSBook();
        dueDate = new Date();
        checkedOutDate = new Date();
        renewalCount = 1;
    }

    public WMSBook getBook(){
        return this.book;
    }

    public Date getDueDate(){
        return this.dueDate;
    }

    public Date getCheckedOutDate(){
        return this.checkedOutDate;
    }
    public Integer getRenewalCount(){
        return this.renewalCount;
    }
}
