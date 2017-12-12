package bigdata.controller;

import bigdata.domain.*;
import bigdata.service.*;
import bigdata.utils.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
public class WsController {

    @Autowired
    private FollowService followService;
    @Autowired
    private rawDataService rawDataService;
    @Autowired
    private userRService userRService;
    @Autowired
    private itemRService itemRService;
    @Autowired
    private evalService evalService;
    @Autowired
    private fpgService fpgService;
    @Autowired
    private windowFollowService windowFollowService;
    @Autowired
    private singleService singleService;

    @RequestMapping("/main")
    public String handleChat(ModelMap modelMap) {
        return "main";
    }

    @RequestMapping("/charts")
    public String handleFollowChat(ModelMap modelMap) {
        return "charts";
    }

    @RequestMapping("/rawdataTable")
    public String handleRawdataTable(ModelMap modelMap) {
        return "rawdataTable";
    }

    @RequestMapping("/followdataTable")
    public String handleFollowDataTable(ModelMap modelMap) {
        return "followdataTable";
    }

    @RequestMapping("/userRTable")
    public String handleUserRTable(ModelMap modelMap) {
        return "userRTable";
    }

    @RequestMapping("/itemRTable")
    public String handleItemRTable(ModelMap modelMap) {
        return "itemRTable";
    }

    @RequestMapping("/evalTable")
    public String handleEvalTable(ModelMap modelMap) {
        return "evalTable";
    }

    @RequestMapping("/fpgTable")
    public String handleFpgTable(ModelMap modelMap) {
        return "fpgTable";
    }

    @RequestMapping("/evalCharts")
    public String handleEvalCharts(ModelMap modelMap) {
        return "evalCharts";
    }

    @RequestMapping("/fpgCharts")
    public String handleFpgCharts(ModelMap modelMap) {
        return "fpgCharts";
    }

    @RequestMapping("/singleFollowChart")
    public String handleSingleFollowChart(ModelMap modelMap) {
        return "singleFollowChart";
    }

    //制作关注度图
    @RequestMapping(value = "/getData")
    @ResponseBody
    public Map<String, Object> getData(){
        List<Follow> result = followService.getData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getFollowData")
    @ResponseBody
    public Map<String, Object> getFollowData(){
        List<Follow> result = followService.getAllData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getRawData")
    @ResponseBody
    public Map<String, Object> getRawData(){
        List<rawData> result = rawDataService.getData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getUserData")
    @ResponseBody
    public Map<String, Object> getUserData(){
        List<userR> result = userRService.getData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getItemData")
    @ResponseBody
    public Map<String, Object> getItemData(){
        List<itemR> result = itemRService.getData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getFpgData")
    @ResponseBody
    public Map<String, Object> getFpgData(){
        List<fpg> result =fpgService.getData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getEvalData")
    @ResponseBody
    public Map<String, Object> getEvalData(){
        List<eval> result =evalService.getData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getWindowFollowData")
    @ResponseBody
    public Map<String, Object> getWindowFollowData(){
        List<windowFollow> result =windowFollowService.getData();
        return ResultJson.resultSuccess(result);
    }

    @RequestMapping(value = "/getSingleFollowData")
    @ResponseBody
    public Map<String, Object> getSingleFollowData(){
        List<single> result =singleService.getData();
        return ResultJson.resultSuccess(result);
    }

}
