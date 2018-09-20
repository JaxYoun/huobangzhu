package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.AgreementTemplate;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/1 15:07
 */
public interface AgreementTemplateRepository extends BaseRepository<AgreementTemplate, Long> {

    @Query("SELECT MAX(t.id) AS id FROM AgreementTemplate AS t")
    Long getMaxId();

}
