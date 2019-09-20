package cn.com.bluemoon.shorturl.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果
 *
 * @date 2019/9/18
 */
@Data
@Accessors(chain = true)
public class ResultBean {
    private boolean isSuccess;
    private int responseCode;
    private String responseMsg;
    private Map<String, String> data;

    public ResultBean() {
    }

    public static ResultBean getSuccess(Map<String, String> data){
        return new ResultBean().setData(data).setResponseCode(1).setResponseMsg("操作成功！").setSuccess(true);
    }
    public void addData(String key, String value){
        if (this.data == null){
            this.data = new HashMap<>(16);
        }
        this.data.put(key, value);
    }

}
