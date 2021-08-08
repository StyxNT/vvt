package com.styxnt.vvtserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.utils.CommonResponse;

/**
 *@author StyxNT
 */
public interface TeamService extends IService<Team> {

    /**
     * 删除小队
     * @param teamId
     * @return
     */
    CommonResponse deleteTeam(Integer teamId);
}
