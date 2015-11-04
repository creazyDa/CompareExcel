/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.compare.excel.util;

/**
 *
 * @author hyberbin
 */
public class NumberUtil {
    public static Float toFloat(Object o){
        if(o==null){
            return 0f;
        }else{
            try{
            return Float.valueOf(o.toString().trim());
            }catch (Exception e){
                return 0f;
            }
        }
    }
}
