package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzTypeValDTO;

import java.util.List;

/**
 * Created by leecheng on 2017/11/22.
 */
public interface HbzTypeValService {

    List<HbzTypeValDTO> getByTypeAndLanguage(String type, String language);

    HbzTypeValDTO getByTypeAndValAndLanguage(String type,String val,String language);

}
