package com.ddmax.zjnucloud.model.exam;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/12/21 21:05.
 */
@Table(name = "exam_list")
public class ExamList extends Model implements Serializable {

    @Column(name = "status")
    public int status;
    @Column(name = "message")
    public String message;
    @Column(name = "info")
    public String info;
    @Column(name = "exams")
    public List<Exam> exams;

    @Column(name = "detail")
    public String detail; // 错误详细信息，如"Invalid Token"

    public List<Exam> exams() {
        return getMany(Exam.class, "Exam");
    }
}
