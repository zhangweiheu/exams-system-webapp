package com.online.exams.system.webapp.bean.VO;

import com.online.exams.system.core.mybatis.enums.QuestionTypeEnum;
import com.online.exams.system.core.mybatis.enums.StatusEnum;
import com.online.exams.system.webapp.bean.BaseObject;

import java.util.Date;

/**
 * Created by zhang on 2016/3/13.
 */
public class QuestionVo extends BaseObject {
    /**
     * id
     */
    private Integer id;

    /**
     * 试题类型，枚举：0单选 | 1多选 | 2编程题
     */
    private QuestionTypeEnum questionType;

    /**
     * 试题主干
     */
    private String title;

    /**
     * 试题选项
     */
    private String options;

    /**
     * 试题答案， 编程题：test_case的id；
     */
    private String answers;

    /**
     * 难度系数1-10
     */
    private Integer difficulty;

    /**
     * 优先级1-10
     */
    private Integer priority;

    /**
     * 题目状态，枚举：0正常 | 1已删除 | 2有错误
     */
    private StatusEnum status;

    /**
     * 是否正确
     */
    private Boolean isRight;

    /**
     * 标签
     */
    private String tagList;

    /**
     * 总完成数
     */
    private Integer totalDone;

    /**
     * 总正确数
     */
    private Integer totalSuccess;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QuestionTypeEnum getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionTypeEnum questionType) {
        this.questionType = questionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public Integer getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Integer totalDone) {
        this.totalDone = totalDone;
    }

    public Integer getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(Integer totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }
}
