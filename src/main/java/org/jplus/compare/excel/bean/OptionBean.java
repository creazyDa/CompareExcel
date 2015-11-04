/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.compare.excel.bean;

/**
 *
 * @author hyberbin
 */
public class OptionBean {
    private Integer id;
    
    private String itemName;
    
    private Boolean compare;
    
    private Float thresholdValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean getCompare() {
        return compare;
    }

    public void setCompare(Boolean compare) {
        this.compare = compare;
    }

    public Float getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Float thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    
}
