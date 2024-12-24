package com.global.coffer.rxjava;

import java.util.List;

public class BannerBean {
    private List<BannerData> data ;

    private int errorCode;

    private String errorMsg;

    public void setData(List<BannerData> data){
        this.data = data;
    }
    public List<BannerData> getData(){
        return this.data;
    }
    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }
    public int getErrorCode(){
        return this.errorCode;
    }
    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }
    public String getErrorMsg(){
        return this.errorMsg;
    }

    public class BannerData {
        private String desc;

        private int id;

        private String imagePath;

        private int isVisible;

        private int order;

        private String title;

        private int type;

        private String url;

        public void setDesc(String desc){
            this.desc = desc;
        }
        public String getDesc(){
            return this.desc;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setImagePath(String imagePath){
            this.imagePath = imagePath;
        }
        public String getImagePath(){
            return this.imagePath;
        }
        public void setIsVisible(int isVisible){
            this.isVisible = isVisible;
        }
        public int getIsVisible(){
            return this.isVisible;
        }
        public void setOrder(int order){
            this.order = order;
        }
        public int getOrder(){
            return this.order;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }

    }
}
