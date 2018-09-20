package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.dto.HbzUserDTO;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/22.
 */
@Data
public class HbzScoreChangeMapDTO extends HbzBaseMapDTO {

    private Long id;

    private String type;

    private String recNo;

    private HbzUserDTO user;

    private Long userId;

    private Integer delta;

    private String action;

    private String adjustType;

    private String msg;

    private String time;
    private String timeLT;
    private String timeLE;
    private String timeGT;
    private String timeGE;

}
