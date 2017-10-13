package com.cnsunway.saas.wash.viewmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by peter on 16/3/21.
 */
public class ViewModel {
    public static final int STATUS_INIT = 0;
    public static final int STATUS_NET_ERROR = 1;
    public static final int STATUS_SERVER_ERROR = 2;
    public static final int STATUS_REQUEST_SUCC = 3;
    public static final int STATUS_REQUEST_FAIL = 4;
    public static final String PROPERTY_NETREQUEST_STATUS = "requestStatus";

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        int oldValue = this.requestStatus;
        this.requestStatus = requestStatus;
        changeSupport.firePropertyChange(PROPERTY_NETREQUEST_STATUS, 0, requestStatus);
    }

    private int requestStatus = STATUS_INIT;

    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(
            this);

    public void addPropertyChangeListener(String propertyName,PropertyChangeListener l) {
        changeSupport.addPropertyChangeListener(propertyName,l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changeSupport.removePropertyChangeListener(l);
    }
}
