package com.upmc.stl.framework.common.interfaces;


import java.util.List;

public interface IHttpHeader {
    void addItem(String field, String value);
    void addItems(String field, List<String> values);
    boolean accept(String field);
}
