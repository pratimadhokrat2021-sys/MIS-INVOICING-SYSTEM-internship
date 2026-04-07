package com.codeb.ims.payload.request;

public class ChainRequest {
    private String companyName;
    private String gstnNo;
    private Integer groupId;
    private Boolean isActive;

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getGstnNo() { return gstnNo; }
    public void setGstnNo(String gstnNo) { this.gstnNo = gstnNo; }

    public Integer getGroupId() { return groupId; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
