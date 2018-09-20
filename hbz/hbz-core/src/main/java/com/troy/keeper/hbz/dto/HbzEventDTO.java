package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/10/17.
 */
@Getter
@Setter
public class HbzEventDTO extends BaseDTO {

    private Integer port;
    private String requestHeader;

    @QueryColumn
    private Event event;

    @QueryColumn(propName = "eventTime", queryOper = "equal")
    private Long eventTime;

    @QueryColumn(propName = "eventTime", queryOper = "lt")
    private Long eventTimeLT;

    @QueryColumn(propName = "eventTime", queryOper = "le")
    private Long eventTimeLE;

    @QueryColumn(propName = "eventTime", queryOper = "gt")
    private Long eventTimeGT;

    @QueryColumn(propName = "eventTime", queryOper = "ge")
    private Long eventTimeGE;

    @QueryColumn
    private String username;

    @QueryColumn
    private String eventUrl;

    @QueryColumn(propName = "eventUrl", queryOper = "like")
    private String eventUrlLIKE;

    @QueryColumn
    private String eventParameter;

    @QueryColumn(propName = "eventParameter", queryOper = "like")
    private String eventParameterLIKE;

    @QueryColumn(queryOper = "like")
    private String grantedAuthorities;

    @QueryColumn
    private Boolean debug;

    @QueryColumn
    private String ip;

}
