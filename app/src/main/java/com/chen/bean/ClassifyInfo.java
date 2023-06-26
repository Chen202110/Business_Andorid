package com.chen.bean;

import com.chen.common.Const;

import java.util.ArrayList;
import java.util.List;

public class ClassifyInfo {
    public String parameter1;
    public Integer parameter2;
    public ClassifyInfo(String parameter1){
        this.parameter1 = parameter1;
    }

    public ClassifyInfo(String parameter1,Integer parameter2){
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    private static String[] listClassifyNameArray = {
            "家电", "手机", "电脑", "服装"};

    private static String[] listClassifyUrlArray = {
            Const.PORTAL_URI+"category/select/sub/家电",Const.PORTAL_URI+"category/select/sub/手机",
            Const.PORTAL_URI+"category/select/sub/电脑",Const.PORTAL_URI+"category/select/sub/服装"
    };

    public static List<ClassifyInfo> getClassifyNameList(){
        List<ClassifyInfo> classifyInfoList = new ArrayList<>();
        for (int i=0;i<listClassifyNameArray.length;i++){
            classifyInfoList.add(new ClassifyInfo(listClassifyNameArray[i]));
        }
        return classifyInfoList;
    }

    public static List<ClassifyInfo> getClassifyUrlList(){
        List<ClassifyInfo> classifyInfoList = new ArrayList<>();
        for (int i=0; i<listClassifyUrlArray.length;i++){
            classifyInfoList.add(new ClassifyInfo(listClassifyUrlArray[i]));
        }
        return classifyInfoList;
    }
}
