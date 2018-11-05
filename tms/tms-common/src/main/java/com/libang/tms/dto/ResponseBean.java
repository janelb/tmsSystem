package com.libang.tms.dto;

/**
 * @author libang
 * @date 2018/8/30 21:38
 */
public class ResponseBean {
   private String message;
    private String status;
    private Object data;


    public ResponseBean(){}

    public static ResponseBean success(){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus("success");
        return responseBean;
    }
    public static ResponseBean success(String message){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus("success");
        responseBean.setMessage(message);
        return responseBean;
    }
    public static ResponseBean success(Object data){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus("success");
        responseBean.setData(data);
        return responseBean;
    }
    public static ResponseBean error(String message){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus("error");
        responseBean.setMessage(message);
        return responseBean;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
