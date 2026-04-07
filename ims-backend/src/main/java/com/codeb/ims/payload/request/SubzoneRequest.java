package com.codeb.ims.payload.request;

public class SubzoneRequest {
    private String subzoneName;
    private Long brandId;
    private Boolean isActive = true;

    public String getSubzoneName() {
        return subzoneName;
    }

    public void setSubzoneName(String subzoneName) {
        this.subzoneName = subzoneName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
