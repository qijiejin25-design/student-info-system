package com.qijiejin.studentinfo.service;

import com.qijiejin.studentinfo.entity.Student;
import com.qijiejin.studentinfo.exception.BusinessException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/** Excel 导入导出。表头与字段顺序固定。 */
@Service
public class ExcelService {

    private static final String[] HEADERS = {
            "学号", "姓名", "性别", "年龄", "班级", "专业", "电话", "邮箱", "入学日期"
    };

    public void writeStudents(OutputStream out, List<Student> students) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("学生信息");
            Row header = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                header.createCell(i).setCellValue(HEADERS[i]);
            }
            int rowIdx = 1;
            for (Student s : students) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(s.getStudentNo());
                row.createCell(1).setCellValue(s.getName());
                row.createCell(2).setCellValue(s.getGender());
                row.createCell(3).setCellValue(s.getAge() == null ? 0 : s.getAge());
                row.createCell(4).setCellValue(s.getClassName());
                row.createCell(5).setCellValue(s.getMajor() == null ? "" : s.getMajor());
                row.createCell(6).setCellValue(s.getPhone() == null ? "" : s.getPhone());
                row.createCell(7).setCellValue(s.getEmail() == null ? "" : s.getEmail());
                row.createCell(8).setCellValue(s.getEnrollDate() == null ? "" : s.getEnrollDate().toString());
            }
            for (int i = 0; i < HEADERS.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
        }
    }

    public List<Student> readStudents(MultipartFile file) {
        List<Student> out = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook wb = WorkbookFactory.create(is)) {
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isEmpty(row)) continue;
                Student s = new Student();
                s.setStudentNo(stringValue(row.getCell(0)));
                s.setName(stringValue(row.getCell(1)));
                s.setGender(stringValue(row.getCell(2)));
                String ageStr = stringValue(row.getCell(3));
                if (!ageStr.isEmpty()) {
                    try { s.setAge((int) Double.parseDouble(ageStr)); }
                    catch (NumberFormatException ignored) {}
                }
                s.setClassName(stringValue(row.getCell(4)));
                s.setMajor(stringValue(row.getCell(5)));
                s.setPhone(stringValue(row.getCell(6)));
                s.setEmail(stringValue(row.getCell(7)));
                String dateStr = stringValue(row.getCell(8));
                Cell c8 = row.getCell(8);
                if (c8 != null && c8.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c8)) {
                    s.setEnrollDate(c8.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } else if (!dateStr.isEmpty()) {
                    try { s.setEnrollDate(LocalDate.parse(dateStr)); }
                    catch (Exception ignored) {}
                }
                validate(s, i + 1);
                out.add(s);
            }
        } catch (IOException e) {
            throw new BusinessException("读取 Excel 失败: " + e.getMessage());
        }
        return out;
    }

    private void validate(Student s, int rowNum) {
        if (isBlank(s.getStudentNo()) || isBlank(s.getName())
                || isBlank(s.getGender()) || isBlank(s.getClassName())) {
            throw new BusinessException("第 " + rowNum + " 行：学号 / 姓名 / 性别 / 班级 必填");
        }
        if (!"男".equals(s.getGender()) && !"女".equals(s.getGender())) {
            throw new BusinessException("第 " + rowNum + " 行：性别只能是 男 / 女");
        }
    }

    private boolean isEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            if (!stringValue(row.getCell(c)).isEmpty()) return false;
        }
        return true;
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }

    private String stringValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                yield d == (long) d ? Long.toString((long) d) : Double.toString(d);
            }
            case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
