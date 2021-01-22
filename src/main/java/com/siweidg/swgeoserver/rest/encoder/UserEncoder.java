package com.siweidg.swgeoserver.rest.encoder;

import com.siweidg.swgeoserver.rest.encoder.utils.ElementUtils;
import com.siweidg.swgeoserver.rest.encoder.utils.PropertyXMLEncoder;
import org.jdom.Element;

/**
 * \* User: x
 * \* Date: 2020/8/20
 * \* Time: 10:49
 * \* Description:
 * \
 */
public class UserEncoder extends PropertyXMLEncoder {

    public final static String userName="userName";
    public final static String password="password";
    public final static String enabled="enabled";

    public UserEncoder(String userName,String password,String enabled) {
        super("");
        addName(userName);
        addPassword(password);
        addEnabled(enabled);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    protected void addName(final String name) {
        final Element el= ElementUtils.contains(getRoot(),userName);
        if (el==null)
            add(userName, name);
        else
            throw new IllegalStateException("userName is already set: "+el.getText());
    }

    protected void addPassword(final String name) {
        final Element el=ElementUtils.contains(getRoot(),password);
        if (el==null)
            add(password, name);
        else
            throw new IllegalStateException("password is already set: "+el.getText());
    }

    protected void addEnabled(final String name) {
        final Element el=ElementUtils.contains(getRoot(),enabled);
        if (el==null)
            add(enabled, name);
        else
            throw new IllegalStateException("password is already set: "+el.getText());
    }
}
