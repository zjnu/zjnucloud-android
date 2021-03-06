package com.ddmax.zjnucloud.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.score.Score;
import com.ddmax.zjnucloud.model.score.ScoreList;
import com.ddmax.zjnucloud.model.score.Semester;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/11/3 19:45.
 */
public class ScoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int COURSE_ITEM = 0;
    private static final int SEMESTER_ITEM = 1;

    private Context mContext;
    private List<String> mTitleList;
    private List<Score> mScoreList;
    // 用于记录学期标题的位置
    private List<Integer> positions;

    public ScoreListAdapter(ScoreList scoreList, Context mContext) {
        this.mContext = mContext;
        this.mTitleList = new ArrayList<>();
        this.mScoreList = new ArrayList<>();
        this.positions = new ArrayList<>();
        initData(scoreList);
    }

    /**
     * 单门课成绩ViewHolder
     */
    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name) TextView name;
        @Bind(R.id.tv_date) TextView credit;
        @Bind(R.id.tv_gp) TextView gradepoint;
        @Bind(R.id.tv_mark_final) TextView mark_final;
        @Bind(R.id.tv_mark_first) TextView mark_first;
        @Bind(R.id.tv_mark_second) TextView mark_second;
        @Bind(R.id.tv_id) TextView id;

        public CourseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 带学期标题的一组成绩ViewHolder
     */
    public class SemesterViewHolder extends CourseViewHolder {
        TextView semester;

        public SemesterViewHolder(View itemView) {
            super(itemView);
            this.semester = (TextView) itemView.findViewById(R.id.score_group);
        }
    }

    /**
     * 渲染具体的ViewHolder
     * @param viewGroup ViewHolder的容器
     * @param viewType  一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return SemesterViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == COURSE_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_score_card, viewGroup, false);
            return new CourseViewHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.group_score_card, viewGroup, false);
            return new SemesterViewHolder(v);
        }
    }

    /**
     * 绑定ViewHolder成绩数据
     * @param holder RecyclerView.ViewHolder
     * @param position 数据源List<Semester>的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Score score = mScoreList.get(position);

        if (score == null) {
            return;
        }

        if (holder instanceof SemesterViewHolder) {
            // 若当前课程卡片位置应有学期标题
            String title = mTitleList.get(positions.indexOf(position));
            bindSemesterItem(score, title, (SemesterViewHolder) holder);
        } else if (holder instanceof CourseViewHolder) {
            bindCourseItem(score, (CourseViewHolder) holder);
        }
    }

    private void bindCourseItem(Score score, CourseViewHolder holder) {
        holder.name.setText(score.name);
        holder.credit.setText(mContext.getString(R.string.course_credit, score.credit));
        // 判断并设置重修、补考、正考成绩
        if (!TextUtils.isEmpty(score.retakemark)) {
            setMarkText(holder.mark_final, null, score.retakemark);
            setMarkText(holder.mark_first, mContext.getString(R.string.course_mark_first), score.mark);
            setMarkText(holder.mark_second, mContext.getString(R.string.course_mark_second), score.makeupmark);
        } else if (!TextUtils.isEmpty(score.makeupmark)) {
            setMarkText(holder.mark_final, null, score.makeupmark);
            setMarkText(holder.mark_first, mContext.getString(R.string.course_mark_first), score.mark);
            if (holder.mark_second.getVisibility() != View.GONE) {
                holder.mark_second.setVisibility(View.GONE);
            }
        } else {
            setMarkText(holder.mark_final, null, score.mark);
            if (holder.mark_first.getVisibility() != View.GONE) {
                holder.mark_first.setVisibility(View.GONE);
            }
            if (holder.mark_second.getVisibility() != View.GONE) {
                holder.mark_second.setVisibility(View.GONE);
            }
        }
        holder.gradepoint.setText(mContext.getString(R.string.course_gradepoint, score.gradepoint));
        holder.id.setText(mContext.getString(R.string.course_id, score.id));
    }

    private void bindSemesterItem(Score score, String title, SemesterViewHolder holder) {
        bindCourseItem(score, holder);
        holder.semester.setText(title);
    }

    /**
     * 根据成绩等级显示不同颜色
     * 记分制：
     * 1、百分制
     * 2、五级计分制：A,B,C,D,不合格F
     * 3、二级记分制：合格P，不合格F
     * @param view TextView
     * @param infoText 分数前的文字信息，无则设为null
     * @param markText 分数字符串
     */
    private void setMarkText(TextView view, String infoText, String markText) {
        int color;
        try {
            double mark = Double.valueOf(markText);
            if (mark >= 60) {
                color = ContextCompat.getColor(mContext, R.color.material_green);
            } else {
                // 如果小于60分，显示红色
                color = ContextCompat.getColor(mContext, R.color.material_red);
            }
        } catch (NumberFormatException e) {
            // 不是百分制的情况
            if (!markText.contains("F")) {
                color = ContextCompat.getColor(mContext, R.color.material_green);
            } else {
                color = ContextCompat.getColor(mContext, R.color.material_red);
            }
        }
        // 设置分数值的颜色
        if (infoText != null && !TextUtils.isEmpty(infoText)) {
            // 分数值前带有文字信息，如正考：59，则文字部分不设置颜色
            final SpannableStringBuilder sb = new SpannableStringBuilder(String.format(infoText, markText));
            sb.setSpan(new ForegroundColorSpan(color), sb.length() - markText.length(),
                    sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            view.setText(sb);
        } else {
            view.setText(markText);
            view.setTextColor(color);
        }
        // 设置View可见
        view.setVisibility(View.VISIBLE);
    }

    private void initData(ScoreList scoreList) {
        // 初始化数据
        if (scoreList != null) {
            List<Semester> semesters = scoreList.scores;
            int lastCoursesSize = 0; // 上个学期的课程数量
            // 清空列表中已有的数据
            this.mTitleList = new ArrayList<>();
            this.mScoreList = new ArrayList<>();
            this.positions = new ArrayList<>();
            for (int i = 0; i < semesters.size(); i++) {
                Semester semester = semesters.get(i);
                // 添加学期标题名称
                String title = semester.semester + " 绩点:" + semester.gpa + " 学分:" + semester.credits;
                mTitleList.add(title);
                // 添加当前学期所有课程到mCourseList
                List<Score> scores = semester.values;
                mScoreList.addAll(scores);
                // 要添加学期名称的位置
                if (i == 0) {
                    positions.add(0);
                    lastCoursesSize = scores.size();
                } else {
                    int position = positions.get(i - 1) + lastCoursesSize;
                    lastCoursesSize = scores.size();
                    positions.add(position);
                }
            }
        }
    }

    public void updateData(ScoreList scoreList) {
        this.initData(scoreList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (positions.contains(position)) {
            return SEMESTER_ITEM;
        }
        return COURSE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
