package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmOrg;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/31.
 */
public interface SmOrgManagementRepository extends BaseRepository<SmOrg, Long> {

   @Query(" select  so  from  SmOrg  so where so.relationship like ?1")
   public List<SmOrg> findAllSmorg(String  orgId);


   List<SmOrg> findByPId(Long pid);

}
