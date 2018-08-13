package com.advertisement.cashcow.module.main.information;

import com.advertisement.cashcow.common.base.BaseApiBean;

/**
 * 作者：吴国洪 on 2018/8/7
 * 描述：版本升级实体
 */
public class AppVersionUpdateApiBean extends BaseApiBean {


    /**
     * resultData : {"recordtime":"2018-08-07 09:34:56","filename":"app-release.apk","appsize":"6.5M","changelog":"1.更新主题背景颜色\n 2.优化查询性能","id":"20dff007-464e-4846-8b81-f09089c44a9c","appurl":"http://192.168.5.100:8080/moneyTree//resources/apks/6bf9ceb0-d78b-4568-b846-b93a012c12c9/app-release.apk","versioncode":"1","versionname":"1.0"}
     */

    private ResultDataBean resultData;

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        /**
         * recordtime : 2018-08-07 09:34:56
         * filename : app-release.apk
         * appsize : 6.5M
         * changelog : 1.更新主题背景颜色
         2.优化查询性能
         * id : 20dff007-464e-4846-8b81-f09089c44a9c
         * appurl : http://192.168.5.100:8080/moneyTree//resources/apks/6bf9ceb0-d78b-4568-b846-b93a012c12c9/app-release.apk
         * versioncode : 1
         * versionname : 1.0
         */

        private String recordtime;
        private String filename;
        private String appsize;
        private String changelog;
        private String id;
        private String appurl;
        private String versioncode;
        private String versionname;

        public String getRecordtime() {
            return recordtime;
        }

        public void setRecordtime(String recordtime) {
            this.recordtime = recordtime;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getAppsize() {
            return appsize;
        }

        public void setAppsize(String appsize) {
            this.appsize = appsize;
        }

        public String getChangelog() {
            return changelog;
        }

        public void setChangelog(String changelog) {
            this.changelog = changelog;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAppurl() {
            return appurl;
        }

        public void setAppurl(String appurl) {
            this.appurl = appurl;
        }

        public String getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(String versioncode) {
            this.versioncode = versioncode;
        }

        public String getVersionname() {
            return versionname;
        }

        public void setVersionname(String versionname) {
            this.versionname = versionname;
        }
    }
}
