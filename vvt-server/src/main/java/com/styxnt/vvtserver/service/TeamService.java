package com.styxnt.vvtserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.utils.CommonResponse;

import java.util.List;

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

    /**
     * 查询当前用户创建的小队信息
     * @return
     */
    List<Team> getTeamsByCurrentUser();
}
