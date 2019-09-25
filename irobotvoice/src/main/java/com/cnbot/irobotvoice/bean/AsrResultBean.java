package com.cnbot.irobotvoice.bean;

/**
 * 描述：
 * 作者：dc on 2019/7/6 15:20
 * 邮箱：597210600@qq.com
 */
public class AsrResultBean {


    /**
     * header : {"namespace":"SpeechRecognizer","name":"RecognitionCompleted","status":20000000,"message_id":"5a7a05ea8dec49059ffa67b5ab62afbb","task_id":"cef3332ff0a34342a53eff343d676664","status_text":"Gateway:SUCCESS:Success."}
     * payload : {"result":"你好","duration":3600}
     */

    private HeaderBean header;
    private PayloadBean payload;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class HeaderBean {
        /**
         * namespace : SpeechRecognizer
         * name : RecognitionCompleted
         * status : 20000000
         * message_id : 5a7a05ea8dec49059ffa67b5ab62afbb
         * task_id : cef3332ff0a34342a53eff343d676664
         * status_text : Gateway:SUCCESS:Success.
         */

        private String namespace;
        private String name;
        private int status;
        private String message_id;
        private String task_id;
        private String status_text;

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }
    }

    public static class PayloadBean {
        /**
         * result : 你好
         * duration : 3600
         */

        private String result;
        private int duration;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
