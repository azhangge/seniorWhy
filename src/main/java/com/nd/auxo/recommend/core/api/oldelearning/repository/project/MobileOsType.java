package com.nd.auxo.recommend.core.api.oldelearning.repository.project;

/**
 * 支付渠道
 *
 * @author wbh
 * @version latest
 * @date 2016/06/06
 */
public enum MobileOsType {
    ANDTOID(1, "Android"),

    IOS(2, "Ios");

    private final Integer value;
    private final String phrase;

    private MobileOsType(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }
    public Integer value() {
        return this.value;
    }

    public String getPhrase() {
        return this.phrase;
    }

    public String toString() {
        return Integer.toString(this.value);
    }
}
