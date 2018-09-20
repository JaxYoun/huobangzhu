package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzWareType;
import com.troy.keeper.hbz.repository.HbzWareTypeRepository;
import com.troy.keeper.hbz.service.HbzWareTypeService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzWareTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by leecheng on 2017/12/18.
 */
@Service
@Transactional
public class HbzWareTypeServiceImpl extends BaseEntityServiceImpl<HbzWareType, HbzWareTypeDTO> implements HbzWareTypeService {
    @Autowired
    HbzWareTypeMapper mapper;
    @Autowired
    HbzWareTypeRepository rep;

    @Override
    public BaseMapper<HbzWareType, HbzWareTypeDTO> getMapper() {
        return mapper;
    }

    @Override
    public BaseRepository getRepository() {
        return rep;
    }

    @Override
    public List<HbzWareTypeDTO> querySub(Long id) {
        HbzWareTypeDTO d = new HbzWareTypeDTO();
        d.setId(id);
        HbzWareTypeDTO type = get(d);
        LinkedList<HbzWareTypeDTO> types = new LinkedList<>();
        types.add(type);
        List<HbzWareTypeDTO> s = new ArrayList<>();
        while (types.size() > 0) {
            HbzWareTypeDTO c = types.removeFirst();
            if (c != null && c.getStatus().equals("1")) {
                s.add(c);
                HbzWareTypeDTO q = new HbzWareTypeDTO();
                q.setParentId(c.getId());
                q.setStatus("1");
                List<HbzWareTypeDTO> ss = query(q);
                types.addAll(ss);
            }
        }
        return s;
    }

    @Override
    public String createTypeNo(Long parentId) {
        if (parentId != null) {
            HbzWareTypeDTO query = new HbzWareTypeDTO();
            query.setId(parentId);
            HbzWareTypeDTO parent = get(query);
            if (parent != null) {
                String prefixNo = parent.getTypeNo();
                int level = 0;
                while (parent != null) {
                    parent = parent.getParent();
                    level++;
                }
                prefixNo = prefixNo.substring(0, 2 * (level));
                int ao = 1;
                while (true) {
                    String typeNo = prefixNo + StringHelper.frontCompWithZore(ao++, 2);
                    typeNo = typeNo + StringHelper.makeRepeat("0", 8 - typeNo.length());
                    if (rep.countByTypeNo(typeNo).longValue() == 0L) {
                        return typeNo;
                    }
                }
            }
            throw new IllegalStateException("无效");
        } else {
            //生成顶级编号
            int no = 1;
            while (true) {
                String typeNo = StringHelper.frontCompWithZore(no++, 2);
                typeNo = typeNo + StringHelper.makeRepeat("0", 8 - typeNo.length());
                if (rep.countByTypeNo(typeNo).longValue() == 0L) {
                    return typeNo;
                }
            }
        }
    }
}
