/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.compare.excel.db;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Transient;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.crud.Hyberbin;
import org.jplus.hyb.database.sqlite.SqliteUtil;
import org.jplus.compare.excel.bean.OptionBean;
import org.jplus.util.ObjectHelper;
import org.jplus.util.Reflections;

/**
 *
 * @author hyberbin
 */
public class SqliteDao {

    static {
        SimpleConfigurator.addConfigurator(new DbConfig("org.sqlite.JDBC", "jdbc:sqlite:./data.db", "", "", DbConfig.DEFAULT_CONFIG_NAME));
        checkTable();
    }
    
    private static void checkTable() {
        Class[] tabClasses = new Class[]{OptionBean.class};
        for (Class tabClasse : tabClasses) {
            if (!SqliteUtil.tableExist(tabClasse.getSimpleName())) {
                autoCreateTable(tabClasse);
            }
        }
    }
    
    public static void autoCreateTable(Class bean){
        StringBuilder sql = new StringBuilder("create table ").append(bean.getSimpleName()).append("(id integer primary key autoincrement");
        List<Field> allFields = Reflections.getAllFields(bean);
        for (Field allField : allFields) {
            if (allField.getName().toLowerCase().equals("id") || allField.isAnnotationPresent(Transient.class)) {
                continue;
            }
            sql.append(",").append(allField.getName());
            if (Float.class.isAssignableFrom(allField.getType())) {
                sql.append(" float ");
            }else if (Number.class.isAssignableFrom(allField.getType())) {
                sql.append(" int ");
            } else {
                sql.append(" text ");
            }
        }
        sql.append(")");
        SqliteUtil.execute(sql.toString());
    }

    public static List getAll(Class clazz) {
        Hyberbin hyberbin = new Hyberbin(Reflections.instance(clazz.getName()));
        try {
            List showAll = hyberbin.showAll();
            return showAll;
        } catch (SQLException ex) {
            Logger.getLogger(SqliteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST;
    }

    public static void saveOption(OptionBean optionBean) {
        deleteByKey(optionBean, "itemName");
        Hyberbin hyberbin = new Hyberbin(optionBean);
        try {
            hyberbin.insert("id");
        } catch (SQLException ex) {
            Logger.getLogger(SqliteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteAll(Class clazz) {
        Hyberbin hyberbin = new Hyberbin(Reflections.instance(clazz.getName()));
        try {
            hyberbin.delete("");
        } catch (SQLException ex) {
            Logger.getLogger(SqliteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void saveOptions(final List<OptionBean> optionBeans) {
        new Thread() {

            @Override
            public void run() {
                deleteAll(OptionBean.class);
                if (ObjectHelper.isNotEmpty(optionBeans)) {
                    for (OptionBean optionBean : optionBeans) {
                        saveOption(optionBean);
                    }
                }
            }

        }.start();
    }

    public static void deleteByKey(OptionBean optionBean, String key) {
        Hyberbin hyberbin = new Hyberbin(optionBean);
        try {
            hyberbin.deleteByKey(key);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
