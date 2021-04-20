package com.siweidg.swgeoserver.comm.constants;

/**
 * \* User: x
 * \* Date: 2021/4/2
 * \* Time: 12:21
 * \* Description:
 * \
 */
public class LayerConstant{
    public enum  StateEnum{
        NOT_AVAILABLE(-1,"不可用"),
        AVAILABLE(0,"已经上传未发布"),
        AVAILABLE_PUBLISH(1,"已发布可用");
        private int code;
        private String msg;

        StateEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum  FlagEnum{
        VECTOR(0,"矢量"),GRID(1,"栅格");
        private int code;
        private String msg;

        FlagEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}