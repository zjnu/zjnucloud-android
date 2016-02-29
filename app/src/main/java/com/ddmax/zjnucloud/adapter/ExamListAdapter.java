package com.ddmax.zjnucloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.exam.Exam;
import com.ddmax.zjnucloud.model.exam.ExamList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/11/3 19:45.
 */
public class ExamListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Exam> mExamList;
    // 用于记录学期标题的位置
    private List<Integer> positions;

    public ExamListAdapter(ExamList examList, Context mContext) {
        this.mContext = mContext;
        this.mExamList = new ArrayList<>();
        this.positions = new ArrayList<>();
        initData(examList);
    }

    /**
     * 单门课成绩ViewHolder
     */
    public static class ExamViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name) TextView name;
        @Bind(R.id.tv_classno) TextView classno;
        @Bind(R.id.tv_teacher) TextView teacher;
        @Bind(R.id.tv_date) TextView date;
        @Bind(R.id.tv_time) TextView time;
        @Bind(R.id.tv_place) TextView place;
        @Bind(R.id.tv_stu_no) TextView studentno;

        public ExamViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 渲染具体的ViewHolder
     * @param viewGroup ViewHolder的容器
     * @param viewType  一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return ExamViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_exam_card, viewGroup, false);
        return new ExamViewHolder(v);
    }

    /**
     * 绑定ViewHolder成绩数据
     * @param holder RecyclerView.ViewHolder
     * @param position 数据源List<Semester>的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Exam exam = mExamList.get(position);

        if (exam == null) {
            return;
        }
        bindExamItem(exam, (ExamViewHolder) holder);
    }

    private void bindExamItem(Exam exam, ExamViewHolder holder) {
        holder.name.setText(exam.name);
        holder.classno.setText(exam.classno);
        holder.teacher.setText(mContext.getString(R.string.exam_teacher, exam.teacher));
        holder.date.setText(mContext.getString(R.string.exam_date, exam.date));
        holder.time.setText(mContext.getString(R.string.exam_time, exam.time));
        holder.place.setText(mContext.getString(R.string.exam_place, exam.place));
        holder.studentno.setText(mContext.getString(R.string.exam_student_no, exam.studentno));
    }

    private void initData(ExamList examList) {
        // 初始化数据
        if (examList != null) {
            mExamList = new ArrayList<>();
            mExamList.addAll(examList.exams);
        }
    }

    public void updateData(ExamList examList) {
        this.initData(examList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mExamList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
