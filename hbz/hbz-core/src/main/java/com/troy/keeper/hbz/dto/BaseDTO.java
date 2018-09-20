package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leecheng on 2017/9/1.
 */
@Getter
@Setter
public class BaseDTO implements Serializable {

    @ValueFormat(validations = {
            @Validation(use = "platform_user_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "u_a", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "u_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "auth_d", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "auth_merge", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_r", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "role_menu", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "role_auth", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "role_r", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "role_merge", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "platform_user_disable", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "platform_user_role_link", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    @QueryColumn
    protected Long id;

    @QueryColumn
    private Long createdBy;

    @QueryColumn
    private Long createdDate;

    private String createdDateDesc;

    @QueryColumn(propName = "createdDate", queryOper = "lt")
    private Long createdDateLT;
    @QueryColumn(propName = "createdDate", queryOper = "le")
    private Long createdDateLE;
    @QueryColumn(propName = "createdDate", queryOper = "gt")
    private Long createdDateGT;
    @QueryColumn(propName = "createdDate", queryOper = "ge")
    private Long createdDateGE;
    @QueryColumn
    private Long lastUpdatedBy;

    @QueryColumn
    private Long lastUpdatedDate;

    @QueryColumn(propName = "lastUpdatedDate", queryOper = "lt")
    private Long lastUpdatedDateLT;
    @QueryColumn(propName = "lastUpdatedDate", queryOper = "le")
    private Long lastUpdatedDateLE;
    @QueryColumn(propName = "lastUpdatedDate", queryOper = "gt")
    private Long lastUpdatedDateGT;
    @QueryColumn(propName = "lastUpdatedDate", queryOper = "ge")
    private Long lastUpdatedDateGE;
    @QueryColumn
    private String status;

    @QueryColumn(propName = "status", queryOper = "in")
    private List<String> statuses;

    /**
     * 页码
     */
    private Integer page = 0;

    private Integer size = 11;

    private List<String[]> sorts;
    /**
     * 共享秘钥 rsaDecode(base64Decode(secret))
     */
    private String secret;

    /**
     * 传输的报文明文
     */
    private String body;

    /**
     * 算法 mad5(secret + body)
     */
    private String sign;

    /**
     * 认证码
     */
    private String authCode;

    private Boolean updateNull = true;

    public Pageable getPageRequest() {
        Sort sort = getSort();
        if (sort != null)
            return new PageRequest(getPage(), getSize(), sort);
        return new PageRequest(getPage(), getSize());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDateDesc() {
        return createdDateDesc;
    }

    public void setCreatedDateDesc(String createdDateDesc) {
        this.createdDateDesc = createdDateDesc;
    }

    public Long getCreatedDateLT() {
        return createdDateLT;
    }

    public void setCreatedDateLT(Long createdDateLT) {
        this.createdDateLT = createdDateLT;
    }

    public Long getCreatedDateLE() {
        return createdDateLE;
    }

    public void setCreatedDateLE(Long createdDateLE) {
        this.createdDateLE = createdDateLE;
    }

    public Long getCreatedDateGT() {
        return createdDateGT;
    }

    public void setCreatedDateGT(Long createdDateGT) {
        this.createdDateGT = createdDateGT;
    }

    public Long getCreatedDateGE() {
        return createdDateGE;
    }

    public void setCreatedDateGE(Long createdDateGE) {
        this.createdDateGE = createdDateGE;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedDateLT() {
        return lastUpdatedDateLT;
    }

    public void setLastUpdatedDateLT(Long lastUpdatedDateLT) {
        this.lastUpdatedDateLT = lastUpdatedDateLT;
    }

    public Long getLastUpdatedDateLE() {
        return lastUpdatedDateLE;
    }

    public void setLastUpdatedDateLE(Long lastUpdatedDateLE) {
        this.lastUpdatedDateLE = lastUpdatedDateLE;
    }

    public Long getLastUpdatedDateGT() {
        return lastUpdatedDateGT;
    }

    public void setLastUpdatedDateGT(Long lastUpdatedDateGT) {
        this.lastUpdatedDateGT = lastUpdatedDateGT;
    }

    public Long getLastUpdatedDateGE() {
        return lastUpdatedDateGE;
    }

    public void setLastUpdatedDateGE(Long lastUpdatedDateGE) {
        this.lastUpdatedDateGE = lastUpdatedDateGE;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<String[]> getSorts() {
        return sorts;
    }

    public void setSorts(List<String[]> sorts) {
        this.sorts = sorts;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        if (sorts != null && sorts.size() > 0) {
            for (int i = 0; i < sorts.size(); i++) {
                String[] sortInfo = sorts.get(i);
                switch (sortInfo[1].toUpperCase()) {
                    case "ASC": {
                        orders.add(new Sort.Order(Sort.Direction.ASC, sortInfo[0]));
                    }
                    break;
                    case "DESC": {
                        orders.add(new Sort.Order(Sort.Direction.DESC, sortInfo[0]));
                    }
                    break;
                    default: {
                        throw new RuntimeException("不支持的排序" + sortInfo[1]);
                    }
                }
            }
            if (orders.size() > 0)
                return new Sort(orders);
            return null;
        }
        return null;
    }

}
