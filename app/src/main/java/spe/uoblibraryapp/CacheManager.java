package spe.uoblibraryapp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class CacheManager {
    private WMSUserProfile userProfile;
    private Date dateAccessed;
    private Boolean refreshing = false;
    private static final CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    public WMSUserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(WMSUserProfile userProfile){
        this.userProfile = userProfile;
        this.dateAccessed = new Date();
    }

    Boolean isExpired(){
        if (userProfile == null){
            return true;
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateAccessed);
        cal.add(Calendar.MINUTE, Constants.Cache.cacheExpiryTime);
        return cal.getTime().before(new Date());
    }

    public void invalidateCache(){
        this.userProfile = null;
        this.dateAccessed = null;
    }

    public void setRefreshing(Boolean refreshing){
        this.refreshing = refreshing;
    }

    public Boolean getRefreshing(){
        return refreshing;
    }

}
