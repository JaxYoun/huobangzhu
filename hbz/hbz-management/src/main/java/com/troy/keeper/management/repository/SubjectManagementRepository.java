package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.SubjectManagement;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2018/1/21.
 */
public interface SubjectManagementRepository    extends BaseRepository<SubjectManagement, Long> {

    //查询货物编号的最大值
    @Query(" select  max(sm.subjectCode)  from  SubjectManagement sm ")
    public String subjectCode();


}
