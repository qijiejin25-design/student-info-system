package com.qijiejin.studentinfo.service;

import com.qijiejin.studentinfo.dto.PageResponse;
import com.qijiejin.studentinfo.dto.StudentQuery;
import com.qijiejin.studentinfo.dto.StudentRequest;
import com.qijiejin.studentinfo.entity.Student;
import com.qijiejin.studentinfo.exception.BusinessException;
import com.qijiejin.studentinfo.mapper.StudentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public PageResponse<Student> page(StudentQuery query) {
        long total = studentMapper.count(query);
        List<Student> items = total > 0 ? studentMapper.findPage(query) : List.of();
        return new PageResponse<>(total, query.getPage(), query.getSize(), items);
    }

    public List<Student> listAll(StudentQuery query) {
        return studentMapper.findAll(query);
    }

    @Cacheable(value = "student", key = "#id")
    public Student getById(Long id) {
        Student s = studentMapper.findById(id);
        if (s == null) throw new BusinessException(404, "学生不存在: " + id);
        return s;
    }

    @Transactional
    public Student create(StudentRequest req) {
        if (studentMapper.findByStudentNo(req.getStudentNo()) != null) {
            throw new BusinessException("学号已存在: " + req.getStudentNo());
        }
        Student s = new Student();
        BeanUtils.copyProperties(req, s);
        studentMapper.insert(s);
        return s;
    }

    @CacheEvict(value = "student", key = "#id")
    @Transactional
    public Student update(Long id, StudentRequest req) {
        Student exist = studentMapper.findById(id);
        if (exist == null) throw new BusinessException(404, "学生不存在: " + id);
        Student dup = studentMapper.findByStudentNo(req.getStudentNo());
        if (dup != null && !dup.getId().equals(id)) {
            throw new BusinessException("学号已被其他学生占用: " + req.getStudentNo());
        }
        BeanUtils.copyProperties(req, exist);
        exist.setId(id);
        studentMapper.update(exist);
        return exist;
    }

    @CacheEvict(value = "student", key = "#id")
    @Transactional
    public void delete(Long id) {
        if (studentMapper.deleteById(id) == 0) {
            throw new BusinessException(404, "学生不存在: " + id);
        }
    }

    @Transactional
    public int batchImport(List<Student> students) {
        if (students.isEmpty()) return 0;
        return studentMapper.batchInsert(students);
    }
}
