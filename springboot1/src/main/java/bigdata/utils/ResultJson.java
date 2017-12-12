package bigdata.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultJson {
    /**
     * 返回一个map容器
     * @return
     */
    public static Map<String, Object> resultMap(){
        return new HashMap<String, Object>();
    }

    /**
     * 返回成功JsonMap
     * @return
     */
    public static Map<String, Object> resultSuccess(){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return  result;
    }

    /**
     * 返回成功JsonMap
     * @param data 数据对象
     * @return
     */
    public static LinkedHashMap<String, Object> resultSuccess(Object data){
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("success", true);
        result.put("data", data);
        return  result;
    }

    /**
     * 返回失败JsonMap
     * @return
     */
    public static Map<String, Object> resultError(){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        return  result;
    }

    /**
     * 返回失败JsonMap
     * @param msg 错误消息
     * @return
     */
    public static Map<String, Object> resultError(Object msg){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        result.put("msg", msg);
        return  result;
    }
}
