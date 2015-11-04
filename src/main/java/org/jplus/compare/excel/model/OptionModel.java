/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.compare.excel.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import org.jplus.compare.excel.bean.OptionBean;
import org.jplus.compare.excel.util.NumberUtil;
import org.jplus.util.ObjectHelper;

/**
 *
 * @author hyberbin
 */
public class OptionModel extends DefaultTableModel {

    private final static String[] tableHeader = {"是否比较", "要比较的项", "阀值"};
    private final boolean[] isEditAble = {true, false, true};

    /**
     * 默认情况下这个方法不用重新实现的，但是这样就会造成如果这个列式boolean的类型，就当做string来处理了
     * 如果是boolean的类型那么用checkbox来显示
     *
     * @return
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        } else {
            return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return isEditAble[column];
    }

    public OptionModel() {
        setColumnIdentifiers(tableHeader);
    }

    /**
     * 设置全选
     */
    public void selectAll() {
        for (int i = 0; i < this.getRowCount(); i++) {
            this.setValueAt(true, i, 0);
        }
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < this.getRowCount(); i++) {
            boolean b = (Boolean) getValueAt(i, 0);
            if (b) {
                count++;
            }
        }
        return count;
    }

    /**
     * 选中所有表
     */
    public List<String> getSelectedTables() {
        List<String> list = new ArrayList<String>(0);
        for (int i = 0; i < this.getRowCount(); i++) {
            Boolean b = (Boolean) getValueAt(i, 0);
            if (Boolean.TRUE.equals(b)) {
                list.add(this.getValueAt(i, 1).toString());
            }
        }
        return list;
    }

    /**
     * 选中所有表
     */
    public List<String> getAllTables() {
        List<String> list = new ArrayList<String>(0);
        for (int i = 0; i < this.getRowCount(); i++) {
            list.add(this.getValueAt(i, 1).toString());
        }
        return list;
    }

    /**
     * 反选所有表
     */
    public void reSelectAllTable() {
        for (int i = 0; i < this.getRowCount(); i++) {
            Boolean b = (Boolean) getValueAt(i, 0);
            this.setValueAt(!b, i, 0);
        }
    }

    public void clear() {
        getDataVector().clear();
    }

    /**
     * 选中所有表
     */
    public void selectAllTable() {
        for (int i = 0; i < this.getRowCount(); i++) {
            this.setValueAt(true, i, 0);
        }
    }

    /**
     * 全不选
     */
    public void unselectAll() {
        for (int i = 0; i < this.getRowCount(); i++) {
            this.setValueAt(false, i, 0);
        }
    }

    public void addItem(OptionBean optionBean) {
        this.addRow(new Object[]{Boolean.TRUE.equals(optionBean.getCompare()), optionBean.getItemName(), optionBean.getThresholdValue()});
    }

    public void addItems(final List<OptionBean> optionBeans) {
        new Thread() {

            @Override
            public void run() {
                if (ObjectHelper.isNotEmpty(optionBeans)) {
                    for (OptionBean optionBean : optionBeans) {
                        addItem(optionBean);
                    }
                }
            }

        }.start();
    }

    public List<OptionBean> getOptionBeans() {
        List<OptionBean> list = new ArrayList<OptionBean>();
        for (int i = 0; i < this.getRowCount(); i++) {
            Boolean b = (Boolean) getValueAt(i, 0);
            OptionBean optionBean = new OptionBean();
            optionBean.setCompare(b);
            optionBean.setItemName(getValueAt(i, 1).toString());
            optionBean.setThresholdValue(NumberUtil.toFloat(getValueAt(i, 2)));
            list.add(optionBean);
        }
        return list;
    }
}
