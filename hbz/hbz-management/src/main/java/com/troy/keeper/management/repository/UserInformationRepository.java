package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.UserInformation;
import com.troy.keeper.management.dto.UserInformationDTO;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/23.
 */
public interface UserInformationRepository   extends BaseRepository<UserInformation, Long> {

//   //新增客户信息时判断是否重复  通过 身份证号，开户行，银行账号 去重
//    @Query("select    ui   from   UserInformation ui where ui.idCard=?1 and ui.bank =?2  and ui.bankAccount =?3")
//    public List<UserInformation> checkeRepeat(String idCard, String bank, Long  bankAccount);

      //新增身份证验证
      @Query("select   count(ui.idCard)   from  UserInformation ui where ui.idCard=?1")
      public Long  idCard(String idCard);
      //新增赢行账号
      @Query("select   count(ui.bankAccount)   from  UserInformation ui where ui.bankAccount=?1")
      public Long  bankAccount(Long bankAccount);








    //////修改
    //校验身份证号是否重复身份证号
    @Query("select   count(ui.idCard)   from  UserInformation ui where ui.idCard=?1 and  ui.id <>?2")
    public Long  checkIdCard(String idCard,Long  id);

    //校验银行账号是否重复
    @Query(" select  count(ui.bankAccount)  from   UserInformation ui where ui.bankAccount=?1 and ui.id <>?2")
    public  Long checkBankAccount(Long bankAccount,Long  id);

    //此方法也是校验银行账号是否重复
//    public List<UserInformation> findByBankAccountAndAndIdNot(Long bankAccount, Long id);

    //删除客户信息
    @Modifying
    @Query("update UserInformation ui set ui.status='0' where ui.id=?1")
    public void deleteUserInformation(Long   id);

    //查询客户分类的 name 值
    @Query(" select  htv.name   from   HbzTypeVal htv  where htv.type=?1 and  htv.val=?2 ")
    public String   findUserClassification(String type,String  val);

//    //查询数据字典 开户行的name值
//    public String findBankName(String type,String val);



}
