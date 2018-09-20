package com.troy.keeper.hbz.vo;

import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.type.Sex;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class HbzUserVO {

    private Long id;

    private String login;

    private String nickName;

    private Double bond;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean activated;

    private Integer starLevel;

    private String langKey;

    private String imageUrl;

    private String activationKey;

    private String telephone;

    private Sex sex;

    private String roleName;

    private HbzOrgDTO org;

    private Long orgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public Double getBond() {
        return bond;
    }
    public void setBond(Double bond) {
        this.bond = bond;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean getActivated() {
        return activated;
    }
    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
    public Integer getStarLevel() {
        return starLevel;
    }
    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }
    public String getLangKey() {
        return langKey;
    }
    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getActivationKey() {
        return activationKey;
    }
    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public Sex getSex() {
        return sex;
    }
    public void setSex(Sex sex) {
        this.sex = sex;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public HbzOrgDTO getOrg() {
        return org;
    }
    public void setOrg(HbzOrgDTO org) {
        this.org = org;
    }
    public Long getOrgId() {
        return orgId;
    }
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
