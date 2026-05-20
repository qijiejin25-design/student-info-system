package com.qijiejin.studentinfo.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class StudentRequest {

    @NotBlank(message = "学号不能为空")
    @Size(max = 32)
    private String studentNo;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 64)
    private String name;

    @NotBlank
    @Pattern(regexp = "男|女", message = "性别只能是 男 / 女")
    private String gender;

    @Min(value = 10, message = "年龄不合理")
    @Max(value = 100, message = "年龄不合理")
    private Integer age;

    @NotBlank(message = "班级不能为空")
    private String className;

    private String major;

    @Pattern(regexp = "^$|^1\\d{10}$", message = "手机号格式不合法")
    private String phone;

    @Email(message = "邮箱格式不合法")
    private String email;

    private LocalDate enrollDate;

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getEnrollDate() { return enrollDate; }
    public void setEnrollDate(LocalDate enrollDate) { this.enrollDate = enrollDate; }
}
