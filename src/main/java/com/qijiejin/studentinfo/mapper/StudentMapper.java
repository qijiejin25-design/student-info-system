package com.qijiejin.studentinfo.mapper;

import com.qijiejin.studentinfo.dto.StudentQuery;
import com.qijiejin.studentinfo.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMapper {

    long count(@Param("q") StudentQuery query);

    List<Student> findPage(@Param("q") StudentQuery query);

    List<Student> findAll(@Param("q") StudentQuery query);

    Student findById(@Param("id") Long id);

    Student findByStudentNo(@Param("studentNo") String studentNo);

    int insert(Student student);

    int update(Student student);

    int deleteById(@Param("id") Long id);

    int batchInsert(@Param("list") List<Student> students);
}
