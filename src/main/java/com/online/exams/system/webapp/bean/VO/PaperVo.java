package com.online.exams.system.webapp.bean.VO;

import com.online.exams.system.core.mybatis.enums.PaperTypeEnum;
import com.online.exams.system.core.mybatis.enums.StatusEnum;
import com.online.exams.system.webapp.bean.BaseObject;

import java.util.Date;

/**
 * graduation.paper
 *
 * @author zhang
 * @date 2016-3-13
 */
public class PaperVo extends BaseObject {
    /**
     * id
     */
    private Integer id;

    /**
     * mongo试卷 id
     */
    private Integer mongoPaperId;

    /**
     * 试卷类型,枚举：0单选题 | 1多选题 | 2编程题 | 3单选、多选 | 4单选、编程 | 5多选、编程
     */
    private PaperTypeEnum paperType;

    /**
     * 难度系数0-10
     */
    private Double difficulty;

    /**
     * 总分
     */
    private Double totalPoints;

    /**
     * 得分
     */
    private Double score;

    /**
     * 状态：0正常 | 1删除 | 2有错误
     */
    private StatusEnum status;

    /**
     * 标签
     */
    private String tagList;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 总做对题数
     */
    private Integer totalRight;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMongoPaperId() {
        return mongoPaperId;
    }

    public void setMongoPaperId(Integer mongoPaperId) {
        this.mongoPaperId = mongoPaperId;
    }

    public PaperTypeEnum getPaperType() {
        return paperType;
    }

    public void setPaperType(PaperTypeEnum paperType) {
        this.paperType = paperType;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
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

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(Integer totalRight) {
        this.totalRight = totalRight;
    }
}
