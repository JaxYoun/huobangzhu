package com.troy.keeper.hbz.service.task;

import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzUserAutoService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.type.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leecheng on 2018/1/26.
 */
@Service
@Transactional
public class HbzUserAutoServiceTaskImpl implements HbzUserAutoService {

    @Autowired
    HbzUserService hbzUserService;

    @Override
    public void auto() {
        //TODO 计算星级
        HbzUserDTO query = new HbzUserDTO();
        query.setStatus("1");
        query.setActivated(true);
        query.setRoles(Arrays.asList(Role.PersonDriver, Role.Consignor, Role.DeliveryBoy, Role.EnterpriseAdmin, Role.EnterpriseConsignor));
        List<HbzUserDTO> users = hbzUserService.query(query);
        users.forEach(user -> {
            int score = user.getScore() == null ? 0 : user.getScore();
            int userScore = user.getUserScore() == null ? 0 : user.getUserScore();

            int starLevel = score / 100;
            if (starLevel > 5) starLevel = 5;
            if (starLevel < 1) starLevel = 1;

            int userStarLevel = userScore / 100;
            if (userStarLevel > 5) userStarLevel = 5;
            if (userStarLevel < 1) userStarLevel = 1;

            user.setScore(score);
            user.setUserScore(userScore);
            user.setStarLevel(starLevel);
            user.setUserStarLevel(userStarLevel);

            hbzUserService.save(user);
        });

    }
}
