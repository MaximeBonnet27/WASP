package com.upmc.stl.framework.request.implem;

import com.upmc.stl.framework.request.enums.EMethodType;
import com.upmc.stl.framework.request.interfaces.IMethod;

public class Method implements IMethod {

    private EMethodType methodType;
    private String URL;
    private String protocol;

    public Method() {
    }

    @Override
    public EMethodType getMethodType() {
        return methodType;
    }

    @Override
    public void setMethodType(EMethodType methodType) {
        this.methodType = methodType;
    }

    @Override
    public String getURL() {
        return URL;
    }

    // TODO: 17/02/16 Il faudrait gérer les exceptions
    @Override
    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return methodType + " " + URL + " " + protocol;
    }
}