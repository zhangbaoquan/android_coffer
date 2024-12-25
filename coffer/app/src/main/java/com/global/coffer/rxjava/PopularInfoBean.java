package com.global.coffer.rxjava;

import java.util.List;

public class PopularInfoBean {
    private List<PopularData> data;

    private int errorCode;

    private String errorMsg;

    public void setData(List<PopularData> data) {
        this.data = data;
    }

    public List<PopularData> getData() {
        return this.data;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public class PopularData {
        private String name;
        private int id;
        private String url;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }

    }
}
