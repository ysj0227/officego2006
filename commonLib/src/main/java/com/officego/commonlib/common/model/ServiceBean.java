package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shijie
 * Date 2021/3/1
 **/
public class ServiceBean {

    /**
     * TenantConsultation : 18116002460
     * TechnicalSupport : 18116002460
     * OwnerEmail : business@officego.com
     * TenantEmail : service@officego.com
     * OwnerConsultation : 18016320482
     */

    @SerializedName("TenantConsultation")
    private String TenantConsultation;
    @SerializedName("TechnicalSupport")
    private String TechnicalSupport;
    @SerializedName("OwnerEmail")
    private String OwnerEmail;
    @SerializedName("TenantEmail")
    private String TenantEmail;
    @SerializedName("OwnerConsultation")
    private String OwnerConsultation;

    public String getTenantConsultation() {
        return TenantConsultation;
    }

    public void setTenantConsultation(String TenantConsultation) {
        this.TenantConsultation = TenantConsultation;
    }

    public String getTechnicalSupport() {
        return TechnicalSupport;
    }

    public void setTechnicalSupport(String TechnicalSupport) {
        this.TechnicalSupport = TechnicalSupport;
    }

    public String getOwnerEmail() {
        return OwnerEmail;
    }

    public void setOwnerEmail(String OwnerEmail) {
        this.OwnerEmail = OwnerEmail;
    }

    public String getTenantEmail() {
        return TenantEmail;
    }

    public void setTenantEmail(String TenantEmail) {
        this.TenantEmail = TenantEmail;
    }

    public String getOwnerConsultation() {
        return OwnerConsultation;
    }

    public void setOwnerConsultation(String OwnerConsultation) {
        this.OwnerConsultation = OwnerConsultation;
    }
}
