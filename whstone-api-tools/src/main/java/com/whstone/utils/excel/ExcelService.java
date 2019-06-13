package com.whstone.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;


public class ExcelService {

    @SuppressWarnings("deprecation")
    public void readExcel(InputStream is) {

        try {

            Workbook workBook = null;
            try {
                workBook = new XSSFWorkbook(is); // 支持2007及以上版本
            } catch (Exception ex) {
                //	workBook = new HSSFWorkbook(new FileInputStream(filePath)); // 支持2003及以前
                workBook = new HSSFWorkbook(is);
            }

            // 获得Excel中工作表个数
            System.out.println("工作表个数 :" + workBook.getNumberOfSheets());

            // 循环每个工作表
            for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
                // 创建工作表
                Sheet sheet = workBook.getSheetAt(i);

                int rows = sheet.getPhysicalNumberOfRows(); // 获得行数
                //初始化list对象

                if (rows > 0) {
                    sheet.getMargin(Sheet.TopMargin);
                    for (int r = 1; r < rows; r++) { // 行循环
                        Row row = sheet.getRow(r);
                        if (row != null) {

                            int cells = row.getLastCellNum();// 获得列数

                            for (short c = 0; c < cells; c++) { // 列循环
                                Cell cell = row.getCell(c);
                                String value = getValue(cell);
                                System.out.println("第" + (r + 1) + "行 " + "第" + (c + 1) + "列：" + value);

                            }
                        }
                    }
                }

                // 查询合并的单元格
                for (i = 0; i < sheet.getNumMergedRegions(); i++) {
                    System.out.println("第" + i + "个合并单元格");
                    CellRangeAddress region = sheet.getMergedRegion(i);
                    int row = region.getLastRow() - region.getFirstRow() + 1;
                    int col = region.getLastColumn() - region.getFirstColumn()
                            + 1;
                    System.out.println("起始行:" + region.getFirstRow());
                    System.out.println("起始列:" + region.getFirstColumn());
                    System.out.println("所占行:" + row);
                    System.out.println("所占列:" + col);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //处理单元格的值
    public static String getValue(Cell cell) {

        String value = "";
        switch (cell.getCellType()) {

            case Cell.CELL_TYPE_NUMERIC: // 数值型
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 如果是date类型则 ，获取该cell的date值
                    value = DateUtil.getJavaDate(cell.getNumericCellValue())
                            .toString();
                } else {// 纯数字
                    value = String.valueOf(cell.getNumericCellValue());
                }
                break;
            /* 此行表示单元格的内容为string类型 */
            case Cell.CELL_TYPE_STRING: // 字符串型
                value = cell.getRichStringCellValue().toString();
                break;
            case Cell.CELL_TYPE_FORMULA:// 公式型
                // 读公式计算值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getRichStringCellValue().toString();
                }
                // cell.getCellFormula();读公式
                break;
            case Cell.CELL_TYPE_BOOLEAN:// 布尔
                value = " " + cell.getBooleanCellValue();
                break;
            /* 此行表示该单元格值为空 */
            case Cell.CELL_TYPE_BLANK: // 空值
                value = "";
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                value = "";
                break;
            default:
                value = cell.getRichStringCellValue().toString();
        }

        return value;
    }


    public static void main(String args[]) {

        ExcelService im = new ExcelService();

//		im.readExcel("C:/Users/Administrator/Desktop/军民融合项目/专利数据/样表.xls");
        // im.readExcel("F:/2007.xlsx");
        // im.readExcel("F:/2007.xls"); //2007下保存为2003

    }

}
