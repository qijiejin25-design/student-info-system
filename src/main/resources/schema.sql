DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_no VARCHAR(32) NOT NULL UNIQUE COMMENT '学号',
    name VARCHAR(64) NOT NULL COMMENT '姓名',
    gender VARCHAR(8) NOT NULL COMMENT '性别',
    age INT COMMENT '年龄',
    class_name VARCHAR(64) NOT NULL COMMENT '班级',
    major VARCHAR(64) COMMENT '专业',
    phone VARCHAR(32) COMMENT '电话',
    email VARCHAR(128) COMMENT '邮箱',
    enroll_date DATE COMMENT '入学日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_student_name ON student(name);
CREATE INDEX idx_student_class ON student(class_name);

CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    nickname VARCHAR(64),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
