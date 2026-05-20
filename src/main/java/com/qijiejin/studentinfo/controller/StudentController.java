package com.qijiejin.studentinfo.controller;

import com.qijiejin.studentinfo.dto.ApiResponse;
import com.qijiejin.studentinfo.dto.PageResponse;
import com.qijiejin.studentinfo.dto.StudentQuery;
import com.qijiejin.studentinfo.dto.StudentRequest;
import com.qijiejin.studentinfo.entity.Student;
import com.qijiejin.studentinfo.service.ExcelService;
import com.qijiejin.studentinfo.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final ExcelService excelService;

    public StudentController(StudentService studentService, ExcelService excelService) {
        this.studentService = studentService;
        this.excelService = excelService;
    }

    @GetMapping
    public ApiResponse<PageResponse<Student>> page(StudentQuery query) {
        return ApiResponse.ok(studentService.page(query));
    }

    @GetMapping("/{id}")
    public ApiResponse<Student> get(@PathVariable Long id) {
        return ApiResponse.ok(studentService.getById(id));
    }

    @PostMapping
    public ApiResponse<Student> create(@Valid @RequestBody StudentRequest req) {
        return ApiResponse.ok(studentService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable Long id, @Valid @RequestBody StudentRequest req) {
        return ApiResponse.ok(studentService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ApiResponse.ok();
    }

    @GetMapping("/export")
    public void export(StudentQuery query, HttpServletResponse response) throws IOException {
        List<Student> all = studentService.listAll(query);
        String filename = URLEncoder.encode("学生信息.xlsx", StandardCharsets.UTF_8);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        excelService.writeStudents(response.getOutputStream(), all);
    }

    @PostMapping("/import")
    public ApiResponse<Integer> importExcel(@RequestParam("file") MultipartFile file) {
        List<Student> students = excelService.readStudents(file);
        int n = studentService.batchImport(students);
        return ApiResponse.ok(n);
    }
}
