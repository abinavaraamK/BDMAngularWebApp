/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hex.vo;

/**
 *
 * @author 3568
 */
public class ConnectionName {
    private String value;
    public ConnectionName(String value) {
        this.value = value;
    }
    public void setValue(String newValue) {
        value = newValue;
    }
    public String getValue() {
        return value;
    }
}
