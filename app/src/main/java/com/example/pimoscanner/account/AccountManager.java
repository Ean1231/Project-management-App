package com.example.pimoscanner.account;

import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalInfo;
import com.esri.arcgisruntime.portal.PortalUser;

public class AccountManager {

    private static final String AGOL_PORTAL_URL = "http://www.arcgis.com";

    private static AccountManager sAccountManager;

    private Portal mPortal;

    private Portal mAGOLPortal;

    private PortalUser mPortalUser;

    private PortalInfo mPortalInfo;

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        if (sAccountManager == null) {
            sAccountManager = new AccountManager();
        }
        return sAccountManager;
    }

    /**
     * Gets the Portal instance the app is currently signed into. Returns null
     * if the user hasn't signed in to a portal.
     */
    public Portal getPortal() {
        return mPortal;
    }

    /**
     * Sets the portal the app is currently signed in to.
     */
    public void setPortal(Portal portal) {
        mPortalUser = null;
        mPortalInfo = null;

        mPortal = portal;

        try {
            mPortalUser = mPortal != null ? mPortal.getUser() : null;
            mPortalInfo = mPortal != null ? mPortal.getPortalInfo() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the ArcGIS Online portal instance (http://www.arcgis.com).
     *
     * @return the ArcGIS Online portal instance
     */
    public Portal getAGOLPortal() {
        if (mAGOLPortal == null) {
            mAGOLPortal = new Portal(AGOL_PORTAL_URL);
        }
        return mAGOLPortal;
    }

    public boolean isSignedIn() {
        return mPortal != null;
    }

    public PortalUser getPortalUser() {
        return mPortalUser;
    }

    public PortalInfo getPortalInfo() {
        return mPortalInfo;
    }
}
