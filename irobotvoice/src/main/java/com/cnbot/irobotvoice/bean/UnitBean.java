package com.cnbot.irobotvoice.bean;

import java.util.List;

/**
 * 描述：
 * 作者：dc on 2019/9/21 14:58
 * 邮箱：597210600@qq.com
 */
public class UnitBean {

    /**
     * result : {"version":"2.0","timestamp":"2019-09-02 17:47:29.387","service_id":"S21140","log_id":"ad22d450-cd66-11e9-82e7-e95522336948","session_id":"service-session-id-1567417649386-541e06bfd1084135ae0972b1bfdc01cb","interaction_id":"service-interactive-id-1567417649387-199b40a4e0a84fbdb3efa21500921475","response_list":[{"status":0,"msg":"ok","origin":"73870","schema":{"intent_confidence":1,"intent":"BUILT_CHAT"},"action_list":[{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"hi，你好。","type":"chat"},{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"你好！学什么的","type":"chat"},{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"你好啊 研究生！","type":"chat"}],"qu_res":{}},{"status":0,"msg":"OK","origin":"73871","schema":{"intent_confidence":99.999992370605,"slots":[{"word_type":"","father_idx":-1,"confidence":100,"length":0,"name":"faq_question","original_word":"你好","session_offset":0,"begin":0,"normalized_word":"你好","merge_method":"add"},{"word_type":"","father_idx":-1,"confidence":99.999992370605,"length":0,"name":"qid","original_word":"50775","session_offset":0,"begin":0,"normalized_word":"50775","merge_method":"update"}],"domain_confidence":0,"intent":"BUILT_FAQ_HELLO"},"action_list":[{"action_id":"built_faq_hello_satisfy","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"阿尼哈赛哟～一起聊聊咩？","type":"satisfy"}],"qu_res":{"candidates":[{"intent_confidence":99.999992370605,"match_info":"你好","slots":[{"word_type":"","father_idx":-1,"confidence":99.999992370605,"length":0,"name":"qid","original_word":"50775","begin":0,"need_clarify":false,"normalized_word":""}],"extra_info":{},"domain_confidence":0,"from_who":"faq_qu","intent":"BUILT_FAQ_HELLO","intent_need_clarify":false}],"qu_res_chosen":"{\"domain_confidence\":0.0,\"extra_info\":{},\"from_who\":\"faq_qu\",\"intent\":\"BUILT_FAQ_HELLO\",\"intent_confidence\":99.99999237060547,\"intent_need_clarify\":false,\"match_info\":\"你好\",\"slots\":[{\"begin\":0,\"confidence\":99.99999237060547,\"father_idx\":-1,\"length\":0,\"name\":\"qid\",\"need_clarify\":false,\"normalized_word\":\"\",\"original_word\":\"50775\",\"word_type\":\"\"}]}\n","sentiment_analysis":{"pval":0.99886959791183,"label":"1"},"lexical_analysis":[],"raw_query":"你好","status":0,"timestamp":0}},{"status":0,"msg":"ok","origin":"73872","schema":{"intent_confidence":0,"slots":[],"domain_confidence":0,"intent":""},"action_list":[{"action_id":"fail_action","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"我不知道应该怎么答复您。","type":"failure"}],"qu_res":{"candidates":[],"qu_res_chosen":"","sentiment_analysis":{"pval":0.998,"label":"1"},"lexical_analysis":[{"etypes":[],"basic_word":["你好"],"weight":1,"term":"你好","type":"19"}],"raw_query":"你好","status":0,"timestamp":0}},{"status":0,"msg":"ok","origin":"73873","schema":{"intent_confidence":0,"slots":[],"domain_confidence":0,"slu_tags":[],"intent":""},"action_list":[{"action_id":"fail_action","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"我不知道应该怎么答复您。","type":"failure"}],"qu_res":{"candidates":[{"intent_confidence":100,"match_info":"{\"group_id\":\"637\",\"id\":\"1667577\",\"informal_word\":\"你好\",\"match_keywords\":\" \",\"match_pattern\":\"[W:2-30]\",\"ori_pattern\":\"[W:2-30]\",\"ori_slots\":{\"confidence\":100.0,\"domain_confidence\":0.0,\"extra_info\":{},\"from_who\":\"smart_qu\",\"intent\":\"CMD_UPDATE_USER_WILD_TARGET\",\"intent_confidence\":100.0,\"intent_need_clarify\":false,\"match_info\":\"[W:2-30] \",\"slots\":[{\"begin\":-1,\"confidence\":100.0,\"father_idx\":-1,\"length\":4,\"name\":\"user_wild_target\",\"need_clarify\":false,\"normalized_word\":\"\",\"original_word\":\"你好\",\"word_type\":\"\"}]},\"real_threshold\":1.0,\"threshold\":0.8999999761581421}","slots":[{"word_type":"","father_idx":-1,"confidence":100,"length":4,"name":"user_wild_target","original_word":"你好","begin":-2,"need_clarify":false,"normalized_word":"你好"}],"extra_info":{"group_id":"637","real_threshold":"1","threshold":"0.9"},"confidence":100,"domain_confidence":0,"from_who":"pow-slu-lev1","intent":"CMD_UPDATE_USER_WILD_TARGET","intent_need_clarify":false}],"qu_res_chosen":"","sentiment_analysis":{"pval":0.998,"label":"1"},"lexical_analysis":[{"etypes":[],"basic_word":["你好"],"weight":1,"term":"你好","type":"19"}],"raw_query":"你好","status":0,"timestamp":0}},{"status":0,"msg":"ok","origin":"41716","schema":{"intent_confidence":100,"slots":[],"domain_confidence":0,"intent":"BUILT_IAQ"},"action_list":[{"action_id":"build_iaq_satisfy","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"不好意思，我没有理解您的问题，可以换个方式再问下呢。","type":"failure"}],"qu_res":{}}]}
     * error_code : 0
     */

    private ResultBean result;
    private int error_code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * version : 2.0
         * timestamp : 2019-09-02 17:47:29.387
         * service_id : S21140
         * log_id : ad22d450-cd66-11e9-82e7-e95522336948
         * session_id : service-session-id-1567417649386-541e06bfd1084135ae0972b1bfdc01cb
         * interaction_id : service-interactive-id-1567417649387-199b40a4e0a84fbdb3efa21500921475
         * response_list : [{"status":0,"msg":"ok","origin":"73870","schema":{"intent_confidence":1,"intent":"BUILT_CHAT"},"action_list":[{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"hi，你好。","type":"chat"},{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"你好！学什么的","type":"chat"},{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"你好啊 研究生！","type":"chat"}],"qu_res":{}},{"status":0,"msg":"OK","origin":"73871","schema":{"intent_confidence":99.999992370605,"slots":[{"word_type":"","father_idx":-1,"confidence":100,"length":0,"name":"faq_question","original_word":"你好","session_offset":0,"begin":0,"normalized_word":"你好","merge_method":"add"},{"word_type":"","father_idx":-1,"confidence":99.999992370605,"length":0,"name":"qid","original_word":"50775","session_offset":0,"begin":0,"normalized_word":"50775","merge_method":"update"}],"domain_confidence":0,"intent":"BUILT_FAQ_HELLO"},"action_list":[{"action_id":"built_faq_hello_satisfy","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"阿尼哈赛哟～一起聊聊咩？","type":"satisfy"}],"qu_res":{"candidates":[{"intent_confidence":99.999992370605,"match_info":"你好","slots":[{"word_type":"","father_idx":-1,"confidence":99.999992370605,"length":0,"name":"qid","original_word":"50775","begin":0,"need_clarify":false,"normalized_word":""}],"extra_info":{},"domain_confidence":0,"from_who":"faq_qu","intent":"BUILT_FAQ_HELLO","intent_need_clarify":false}],"qu_res_chosen":"{\"domain_confidence\":0.0,\"extra_info\":{},\"from_who\":\"faq_qu\",\"intent\":\"BUILT_FAQ_HELLO\",\"intent_confidence\":99.99999237060547,\"intent_need_clarify\":false,\"match_info\":\"你好\",\"slots\":[{\"begin\":0,\"confidence\":99.99999237060547,\"father_idx\":-1,\"length\":0,\"name\":\"qid\",\"need_clarify\":false,\"normalized_word\":\"\",\"original_word\":\"50775\",\"word_type\":\"\"}]}\n","sentiment_analysis":{"pval":0.99886959791183,"label":"1"},"lexical_analysis":[],"raw_query":"你好","status":0,"timestamp":0}},{"status":0,"msg":"ok","origin":"73872","schema":{"intent_confidence":0,"slots":[],"domain_confidence":0,"intent":""},"action_list":[{"action_id":"fail_action","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"我不知道应该怎么答复您。","type":"failure"}],"qu_res":{"candidates":[],"qu_res_chosen":"","sentiment_analysis":{"pval":0.998,"label":"1"},"lexical_analysis":[{"etypes":[],"basic_word":["你好"],"weight":1,"term":"你好","type":"19"}],"raw_query":"你好","status":0,"timestamp":0}},{"status":0,"msg":"ok","origin":"73873","schema":{"intent_confidence":0,"slots":[],"domain_confidence":0,"slu_tags":[],"intent":""},"action_list":[{"action_id":"fail_action","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"我不知道应该怎么答复您。","type":"failure"}],"qu_res":{"candidates":[{"intent_confidence":100,"match_info":"{\"group_id\":\"637\",\"id\":\"1667577\",\"informal_word\":\"你好\",\"match_keywords\":\" \",\"match_pattern\":\"[W:2-30]\",\"ori_pattern\":\"[W:2-30]\",\"ori_slots\":{\"confidence\":100.0,\"domain_confidence\":0.0,\"extra_info\":{},\"from_who\":\"smart_qu\",\"intent\":\"CMD_UPDATE_USER_WILD_TARGET\",\"intent_confidence\":100.0,\"intent_need_clarify\":false,\"match_info\":\"[W:2-30] \",\"slots\":[{\"begin\":-1,\"confidence\":100.0,\"father_idx\":-1,\"length\":4,\"name\":\"user_wild_target\",\"need_clarify\":false,\"normalized_word\":\"\",\"original_word\":\"你好\",\"word_type\":\"\"}]},\"real_threshold\":1.0,\"threshold\":0.8999999761581421}","slots":[{"word_type":"","father_idx":-1,"confidence":100,"length":4,"name":"user_wild_target","original_word":"你好","begin":-2,"need_clarify":false,"normalized_word":"你好"}],"extra_info":{"group_id":"637","real_threshold":"1","threshold":"0.9"},"confidence":100,"domain_confidence":0,"from_who":"pow-slu-lev1","intent":"CMD_UPDATE_USER_WILD_TARGET","intent_need_clarify":false}],"qu_res_chosen":"","sentiment_analysis":{"pval":0.998,"label":"1"},"lexical_analysis":[{"etypes":[],"basic_word":["你好"],"weight":1,"term":"你好","type":"19"}],"raw_query":"你好","status":0,"timestamp":0}},{"status":0,"msg":"ok","origin":"41716","schema":{"intent_confidence":100,"slots":[],"domain_confidence":0,"intent":"BUILT_IAQ"},"action_list":[{"action_id":"build_iaq_satisfy","refine_detail":{"option_list":[],"interact":"","clarify_reason":""},"confidence":100,"custom_reply":"","say":"不好意思，我没有理解您的问题，可以换个方式再问下呢。","type":"failure"}],"qu_res":{}}]
         */

        private String version;
        private String timestamp;
        private String service_id;
        private String log_id;
        private String session_id;
        private String interaction_id;
        private List<ResponseListBean> response_list;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getLog_id() {
            return log_id;
        }

        public void setLog_id(String log_id) {
            this.log_id = log_id;
        }

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getInteraction_id() {
            return interaction_id;
        }

        public void setInteraction_id(String interaction_id) {
            this.interaction_id = interaction_id;
        }

        public List<ResponseListBean> getResponse_list() {
            return response_list;
        }

        public void setResponse_list(List<ResponseListBean> response_list) {
            this.response_list = response_list;
        }

        public static class ResponseListBean {
            /**
             * status : 0
             * msg : ok
             * origin : 73870
             * schema : {"intent_confidence":1,"intent":"BUILT_CHAT"}
             * action_list : [{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"hi，你好。","type":"chat"},{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"你好！学什么的","type":"chat"},{"action_id":"","refine_detail":{},"confidence":1,"custom_reply":"","say":"你好啊 研究生！","type":"chat"}]
             * qu_res : {}
             */

            private int status;
            private String msg;
            private String origin;
            private SchemaBean schema;
            private QuResBean qu_res;
            private List<ActionListBean> action_list;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public SchemaBean getSchema() {
                return schema;
            }

            public void setSchema(SchemaBean schema) {
                this.schema = schema;
            }

            public QuResBean getQu_res() {
                return qu_res;
            }

            public void setQu_res(QuResBean qu_res) {
                this.qu_res = qu_res;
            }

            public List<ActionListBean> getAction_list() {
                return action_list;
            }

            public void setAction_list(List<ActionListBean> action_list) {
                this.action_list = action_list;
            }

            public static class SchemaBean {
            }

            public static class QuResBean {
            }

            public static class ActionListBean {
                /**
                 * action_id :
                 * refine_detail : {}
                 * confidence : 1
                 * custom_reply :
                 * say : hi，你好。
                 * type : chat
                 */

                private String action_id;
                private RefineDetailBean refine_detail;
                private double confidence;
                private String custom_reply;
                private String say;
                private String type;

                public String getAction_id() {
                    return action_id;
                }

                public void setAction_id(String action_id) {
                    this.action_id = action_id;
                }

                public RefineDetailBean getRefine_detail() {
                    return refine_detail;
                }

                public void setRefine_detail(RefineDetailBean refine_detail) {
                    this.refine_detail = refine_detail;
                }

                public double getConfidence() {
                    return confidence;
                }

                public void setConfidence(double confidence) {
                    this.confidence = confidence;
                }

                public String getCustom_reply() {
                    return custom_reply;
                }

                public void setCustom_reply(String custom_reply) {
                    this.custom_reply = custom_reply;
                }

                public String getSay() {
                    return say;
                }

                public void setSay(String say) {
                    this.say = say;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public static class RefineDetailBean {

                    /**
                     * option_list : [{"info":{"next_expect_intent":"FAQ_FOUND"},"option":"你的工作是什么啊"},{"info":{"next_expect_intent":"FAQ_FOUND"},"option":"你每天吃些什么"},{"info":{"next_expect_intent":"FAQ_FOUND"},"option":"你的名字是什么"}]
                     * interact : select
                     * clarify_reason :
                     */

                    private String interact;
                    private String clarify_reason;
                    private List<OptionListBean> option_list;

                    public String getInteract() {
                        return interact;
                    }

                    public void setInteract(String interact) {
                        this.interact = interact;
                    }

                    public String getClarify_reason() {
                        return clarify_reason;
                    }

                    public void setClarify_reason(String clarify_reason) {
                        this.clarify_reason = clarify_reason;
                    }

                    public List<OptionListBean> getOption_list() {
                        return option_list;
                    }

                    public void setOption_list(List<OptionListBean> option_list) {
                        this.option_list = option_list;
                    }

                    public static class OptionListBean {
                        /**
                         * info : {"next_expect_intent":"FAQ_FOUND"}
                         * option : 你的工作是什么啊
                         */

                        private InfoBean info;
                        private String option;

                        public InfoBean getInfo() {
                            return info;
                        }

                        public void setInfo(InfoBean info) {
                            this.info = info;
                        }

                        public String getOption() {
                            return option;
                        }

                        public void setOption(String option) {
                            this.option = option;
                        }

                        public static class InfoBean {
                            /**
                             * next_expect_intent : FAQ_FOUND
                             */

                            private String next_expect_intent;

                            public String getNext_expect_intent() {
                                return next_expect_intent;
                            }

                            public void setNext_expect_intent(String next_expect_intent) {
                                this.next_expect_intent = next_expect_intent;
                            }
                        }
                    }
                }
            }
        }
    }
}
