package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.dtclookup;


public class DtcListItem {
    String code;
    String summary;

    public DtcListItem(String code, String summary){
        this.code = code;
        this.summary = summary;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
