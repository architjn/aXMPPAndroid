package com.architjn.myapp.xmpp;

/**
 * Created by architjn on 10/10/2016.
 */

public class SmackInvocationException extends Exception {
    public SmackInvocationException(Exception e) {
        super(e.getMessage());
    }
}
