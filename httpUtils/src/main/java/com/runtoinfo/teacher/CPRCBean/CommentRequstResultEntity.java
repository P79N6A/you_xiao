package com.runtoinfo.teacher.CPRCBean;

/**
 * Created by QiaoJunChao on 2018/8/31.
 */

public class CommentRequstResultEntity {

       public int tenantId;
       public int campusId;
       public int userId;
       public int type;
       public int parentId;
       public int parentType;
       public int target;
       public int targetType;
       public String content;
       public int level;
       public boolean isApproved;
       public String approvedBy;
       public String approvedTime;
       public int replyNumber;
       public String userAvatar;
       public int nickName;
       public boolean hasPraise;
       public int praiseId;
       public int id;

       public int getTenantId() {
              return tenantId;
       }

       public void setTenantId(int tenantId) {
              this.tenantId = tenantId;
       }

       public int getCampusId() {
              return campusId;
       }

       public void setCampusId(int campusId) {
              this.campusId = campusId;
       }

       public int getUserId() {
              return userId;
       }

       public void setUserId(int userId) {
              this.userId = userId;
       }

       public int getType() {
              return type;
       }

       public void setType(int type) {
              this.type = type;
       }

       public int getParentId() {
              return parentId;
       }

       public void setParentId(int parentId) {
              this.parentId = parentId;
       }

       public int getParentType() {
              return parentType;
       }

       public void setParentType(int parentType) {
              this.parentType = parentType;
       }

       public int getTarget() {
              return target;
       }

       public void setTarget(int target) {
              this.target = target;
       }

       public int getTargetType() {
              return targetType;
       }

       public void setTargetType(int targetType) {
              this.targetType = targetType;
       }

       public String getContent() {
              return content;
       }

       public void setContent(String content) {
              this.content = content;
       }

       public int getLevel() {
              return level;
       }

       public void setLevel(int level) {
              this.level = level;
       }

       public boolean isApproved() {
              return isApproved;
       }

       public void setApproved(boolean approved) {
              isApproved = approved;
       }

       public String getApprovedBy() {
              return approvedBy;
       }

       public void setApprovedBy(String approvedBy) {
              this.approvedBy = approvedBy;
       }

       public String getApprovedTime() {
              return approvedTime;
       }

       public void setApprovedTime(String approvedTime) {
              this.approvedTime = approvedTime;
       }

       public int getReplyNumber() {
              return replyNumber;
       }

       public void setReplyNumber(int replyNumber) {
              this.replyNumber = replyNumber;
       }

       public String getUserAvatar() {
              return userAvatar;
       }

       public void setUserAvatar(String userAvatar) {
              this.userAvatar = userAvatar;
       }

       public int getNickName() {
              return nickName;
       }

       public void setNickName(int nickName) {
              this.nickName = nickName;
       }

       public boolean isHasPraise() {
              return hasPraise;
       }

       public void setHasPraise(boolean hasPraise) {
              this.hasPraise = hasPraise;
       }

       public int getPraiseId() {
              return praiseId;
       }

       public void setPraiseId(int praiseId) {
              this.praiseId = praiseId;
       }

       public int getId() {
              return id;
       }

       public void setId(int id) {
              this.id = id;
       }
}
