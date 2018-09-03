package com.nd.auxo.recommend.core.api.oldelearning.repository.project;

import lombok.Data;

/**
 * 项目移动应用配置信息
 *
 * @author wbh
 * @version latest
 * @date 2016/06/03
 */
@Data
public class MobileApp {
    private String osType;
    private String version;
    private String downloadAddr;
    private String codeName;

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDownloadAddr(String downloadAddr) {
        this.downloadAddr = downloadAddr;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getOsType() {
        return osType;
    }

    public String getVersion() {
        return version;
    }

    public String getDownloadAddr() {
        return downloadAddr;
    }

    public String getCodeName() {
        return codeName;
    }
}

