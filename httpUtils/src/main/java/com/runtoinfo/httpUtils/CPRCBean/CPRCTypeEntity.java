package com.runtoinfo.httpUtils.CPRCBean;

/**
 * Created by QiaoJunChao on 2018/8/31.
 *
 * 评论、回复、赞、收藏
 * 类型综合
 */

public class CPRCTypeEntity {
    /**
     * TYPE
     */
    public final static int COMMENT = 0;
    public final static int REPLY = 1;
    public final static int PRAISE = 2;
    public final static int COLLECTION = 3;

    /**
     * TargetType
     */
    public final static int NEWS = 0;
    public final static int TOPICS = 1;
    public final static int TARGET_COMMENT = 2;
    public final static int TARGET_REPLY = 3;

    /**
     * ParentType
     */
    public final static int PARENT_COMMENT = 0;
    public final static int PARENT_REPLY = 1;
}
