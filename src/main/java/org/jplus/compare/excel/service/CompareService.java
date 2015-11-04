/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.compare.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jplus.compare.excel.bean.OptionBean;
import org.jplus.compare.excel.db.SqliteDao;
import org.jplus.compare.excel.util.NumberUtil;
import org.jplus.hyb.database.sqlite.SqliteUtil;
import org.jplus.hyberbin.excel.service.BaseExcelService;
import org.jplus.hyberbin.excel.service.ImportTableService;
import org.jplus.util.ObjectHelper;

/**
 *
 * @author hyberbin
 */
public class CompareService {
    
    public static List<String> getSheetNames(String excelPath){
        List<String> names = new ArrayList<String>();
        InputStream inputStream = null;
        try {
            File file = new File(excelPath);
            inputStream = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                names.add(workbook.getSheetName(i));
            }
        } catch (IOException ex) {
            Logger.getLogger(CompareService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(CompareService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return names;
    }

    public static List<OptionBean> getOptionBeansFromExcel(String excelPath, String sheetName) {
        Map<String, OptionBean> optionMap = getOptionMap();
        List<OptionBean> optionBeans = new ArrayList<OptionBean>();
        InputStream inputStream = null;
        try {
            File file = new File(excelPath);
            inputStream = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = BaseExcelService.getRow(sheet, 0);
            short lastCellNum = row.getLastCellNum();
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    String string = BaseExcelService.getString(cell);
                    if (!ObjectHelper.isNullOrEmptyString(string)) {
                        OptionBean optionBean = new OptionBean();
                        optionBean.setItemName(string);
                        OptionBean get = optionMap.get(string);
                        if (ObjectHelper.isNotEmpty(get)) {
                            optionBean.setCompare(get.getCompare());
                            optionBean.setThresholdValue(get.getThresholdValue());
                        }
                        optionBeans.add(optionBean);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(CompareService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(CompareService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return optionBeans;
    }

    public static Map<String, OptionBean> getOptionMap() {
        Map<String, OptionBean> map = new HashMap<String, OptionBean>();
        List<OptionBean> all = SqliteDao.getAll(OptionBean.class);
        for (OptionBean optionBean : all) {
            map.put(optionBean.getItemName(), optionBean);
        }
        return map;
    }

    public static List<Map> getExcelContent(String excelPath, String sheetName) {
        InputStream inputStream = null;
        try {
            File file = new File(excelPath);
            inputStream = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            ImportTableService tableService = new ImportTableService(sheet);
            tableService.setStartRow(1);
            tableService.doImport();
            return tableService.read(getExcelHeader(sheet), Map.class);
        } catch (Exception ex) {
            Logger.getLogger(CompareService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(CompareService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Collections.EMPTY_LIST;
    }

    public static String[] getExcelHeader(Sheet sheet) {
        List<String> header = new ArrayList<String>();
        Row row = BaseExcelService.getRow(sheet, 0);
        short lastCellNum = row.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String string = BaseExcelService.getString(cell);
                header.add(string);
            }
        }
        return header.toArray(new String[]{});

    }

    public static Map<String, Map> getSalaryMap(List<Map> maps, String key) {
        Map<String, Map> map = new HashMap<String, Map>();
        for (Map m : maps) {
            Object v = m.get(key);
            if (v != null) {
                map.put(v.toString(), m);
            }
        }
        return map;
    }

    public static List<Map> compare(Map<String, Map> last, Map<String, Map> current, String[] sortedColumns, Map<String, OptionBean> optionMap) {
        List<Map> maps = new ArrayList<Map>();
        for (String key : last.keySet()) {
            if (current.containsKey(key)) {
                Map lastMap = last.get(key);
                Map currentMap = current.get(key);
                boolean added = false;
                float totalAbs = 0;
                for (String column : sortedColumns) {
                    Float lastFloat = NumberUtil.toFloat(lastMap.get(column));
                    Float currentFloat = NumberUtil.toFloat(currentMap.get(column));
                    OptionBean optionBean = optionMap.get(column);
                    if (optionBean != null && optionBean.getThresholdValue() != null && optionBean.getThresholdValue() > 0) {
                        float abs = Math.abs(lastFloat - currentFloat);
                        totalAbs += abs;
                        if (abs >= optionBean.getThresholdValue()) {
                            currentMap.put("相差值", totalAbs);
                            BaseExcelService.setErrorStyle((Cell) currentMap.get("cell" + column));
                            if (!added) {
                                maps.add(currentMap);
                                added = true;
                            }
                        }
                    }
                }
            }
        }
        if (ObjectHelper.isNotEmpty(maps)) {
            Cell cell = (Cell) (maps.get(0)).get("cell" + SqliteUtil.getProperty("cellKey"));
            Workbook workbook = cell.getSheet().getWorkbook();
            File file = new File("标记过的Excel.xls");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
                fileOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(CompareService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return maps;
    }
}
