package com.qijiejin.studentinfo;

import com.qijiejin.studentinfo.dto.PageResponse;
import com.qijiejin.studentinfo.dto.StudentQuery;
import com.qijiejin.studentinfo.dto.StudentRequest;
import com.qijiejin.studentinfo.entity.Student;
import com.qijiejin.studentinfo.exception.BusinessException;
import com.qijiejin.studentinfo.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentInfoApplicationTests {

    @Autowired
    private StudentService studentService;

    @Test
    void contextLoads() { }

    @Test
    void pageQueryReturnsSeedData() {
        StudentQuery q = new StudentQuery();
        PageResponse<Student> page = studentService.page(q);
        assertThat(page.getTotal()).isGreaterThanOrEqualTo(10);
        assertThat(page.getItems()).hasSize(10);
    }

    @Test
    void filterByClassName() {
        StudentQuery q = new StudentQuery();
        q.setClassName("计算机1班");
        PageResponse<Student> page = studentService.page(q);
        assertThat(page.getItems()).allMatch(s -> s.getClassName().equals("计算机1班"));
    }

    @Test
    void createAndDelete() {
        StudentRequest req = new StudentRequest();
        req.setStudentNo("T9999");
        req.setName("测试");
        req.setGender("男");
        req.setAge(20);
        req.setClassName("测试班");
        req.setEnrollDate(LocalDate.now());
        Student created = studentService.create(req);
        assertThat(created.getId()).isNotNull();

        // 重复学号应抛业务异常
        assertThatThrownBy(() -> studentService.create(req))
                .isInstanceOf(BusinessException.class);

        studentService.delete(created.getId());
    }
}
