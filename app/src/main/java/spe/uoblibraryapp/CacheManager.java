package spe.uoblibraryapp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class CacheManager {
    private static final CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    private WMSUserProfile userProfile;
    private Date dateAccessed;

    private CacheManager() {
    }

    WMSUserProfile getUserProfile() {
        return userProfile;
    }

    void setUserProfile(WMSUserProfile userProfile){
        this.userProfile = userProfile;
        this.dateAccessed = new Date();
    }

    Boolean isExpired(){
        if (userProfile == null){
            return true;
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateAccessed);
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime().before(new Date());
    }

    public void invalidateCache(){
        this.userProfile = null;
        this.dateAccessed = null;
    }

}
