package com.qijiejin.studentinfo.dto;

public class StudentQuery {
    private String name;
    private String studentNo;
    private String className;
    private String gender;

    private Integer page = 1;
    private Integer size = 10;

    public int getOffset() {
        return Math.max(0, (page - 1) * size);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
