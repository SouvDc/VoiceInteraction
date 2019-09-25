package com.cnbot.irobotvoice.bean;

import java.util.List;

/**
 * Created by DengJia on 2018/7/10.
 */

/**
 * Copyright (c) 2016--2019/2/21  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 语音分析返回结果实体类
 * @FileName: NlpBean.java
 * @author: dc
 * @date: 2019/2/21 11:37
 * @version: 1.0
 */

public class NlpBean {


    /**
     * type : 8
     * content : 您好，我是阳光小智，很高兴认识您。
     * moduleId : chat
     * commands : []
     * props : {"categoryId":"2ddbbd2744a04f1fbbf7353a6bb3ed79","objectId":"001523505660926178140050568c0428","sessionId":"455d04e1296f4144be6a1c270b71cd9f"}
     * nodeId : 001523505660932178200050568c0428
     * similarity : 1.0
     * callbackParamType : 0
     */

    private int type;
    private String content;
    private String moduleId;
    private PropsBean props;
    private String nodeId;
    private double similarity;
    private int callbackParamType;
    private List<?> commands;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public PropsBean getProps() {
        return props;
    }

    public void setProps(PropsBean props) {
        this.props = props;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public int getCallbackParamType() {
        return callbackParamType;
    }

    public void setCallbackParamType(int callbackParamType) {
        this.callbackParamType = callbackParamType;
    }

    public List<?> getCommands() {
        return commands;
    }

    public void setCommands(List<?> commands) {
        this.commands = commands;
    }

    public static class PropsBean {
        /**
         * categoryId : 2ddbbd2744a04f1fbbf7353a6bb3ed79
         * objectId : 001523505660926178140050568c0428
         * sessionId : 455d04e1296f4144be6a1c270b71cd9f
         */

        private String categoryId;
        private String objectId;
        private String sessionId;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
