package io.o2mc.sdk.domain;

public class DeviceInformation {
    private String appId;
    private String os;
    private String osVersion;
    private String deviceName;

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "DeviceInformation{" +
                "appId='" + appId + '\'' +
                ", os='" + os + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }
}
