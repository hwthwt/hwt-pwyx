/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */

package com.ygsoft.std.ny.cms.impl.context;

import com.ygsoft.ecp.core.framework.annotations.ContextProvider;
import com.ygsoft.ecp.core.framework.annotations.ContextProviderDesc;
import com.ygsoft.ecp.core.framework.annotations.OSGiService;
import com.ygsoft.ecp.core.framework.domain.GeneralContext;
import com.ygsoft.ecp.core.framework.model.DataModelState;
import com.ygsoft.ecp.service.tool.OSGiUtil;
import com.ygsoft.ecp.service.tool.StringUtil;
import com.ygsoft.ecp.service.tool.UuidUtil;
import com.ygsoft.ecp.core.framework.annotations.Topic;
import com.ygsoft.std.ny.cms.service.context.IHallBuyApplyContext;
import com.ygsoft.std.ny.cms.service.context.IHallBuyApplyQueryContext;
import com.ygsoft.std.ny.cms.service.role.IHallBuyApplyRole;
import com.ygsoft.std.ny.platform.impl.common.utils.LoginInfoUtil;
import com.ygsoft.std.ny.cms.service.model.HallBuyApplyVO;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Contexts实现类.<br>
 * @author huangmaoqia <br>
 * @version 1.0.0 2017-09-18 <br>
 */
@Service
@OSGiService
@Transactional
@ContextProvider
@ContextProviderDesc(caption = "HallBuyApplyContext实体数据访问场景类", type = "java")
@Topic(classId = "ecp.tableentity", typeId = "HallBuyApply")
public class HallBuyApplyContext extends GeneralContext<HallBuyApplyVO, IHallBuyApplyRole>
	implements IHallBuyApplyContext {

    /**
     * setHallBuyApplyRole.
     * @param entityRole 
     */
	@Autowired
	public void setHallBuyApplyRole(final IHallBuyApplyRole entityRole) {
		super.setGeneralRole(entityRole);
	}
	
	private IHallBuyApplyQueryContext iHallBuyApplyQueryContext;
	private IHallBuyApplyQueryContext getIHallBuyApplyQueryContext(){
		if(iHallBuyApplyQueryContext==null){
			iHallBuyApplyQueryContext=OSGiUtil.getService(IHallBuyApplyQueryContext.class);
		}
		return iHallBuyApplyQueryContext;
	}
	
	
	/**
	 * 保存一条
	 */
	public String saveOne(final HallBuyApplyVO vo){
		String tenantid=LoginInfoUtil.getTenantId();
		String compid=LoginInfoUtil.getCompId();
		String userid=LoginInfoUtil.getUserId();
		
		String objid=vo.getObjid();
		if(StringUtil.isNullOrTrimEmptyString(objid)){
			//add
			objid=UuidUtil.newUUID();
			vo.setModelState(DataModelState.NEW);
			vo.setObjid(objid);
			vo.setTenantid(tenantid);
			vo.setCompid(compid);
			vo.setCreateuserid(userid);
			vo.setCreatetime(new Date());
			vo.setUpdateuserid(userid);
			vo.setUpdatetime(new Date());
			vo.setState("001");
		}else{
			//update
			vo.setModelState(DataModelState.MODIFY);
			vo.setUpdateuserid(userid);
			vo.setUpdatetime(new Date());
		}
		this.save(null, vo);
		return objid;
	}
	
	/**
	 * 删除一条
	 */
	public void deleteOne(final String objid){
		String userid=LoginInfoUtil.getUserId();
		HallBuyApplyVO vo=getIHallBuyApplyQueryContext().getOne(objid);
		if(vo!=null){
			vo.setModelState(DataModelState.MODIFY);
			vo.setCreateuserid(userid);
			vo.setCreatetime(new Date());
			vo.setUpdateuserid(userid);
			vo.setUpdatetime(new Date());
			vo.setState("000");
			this.save(null, vo);
		}
	}
	
	/**
	 * 回复申请
	 */
	public String reply(final HallBuyApplyVO vo){
		String userid=LoginInfoUtil.getUserId();
		vo.setReplyuserid(userid);
		vo.setReplytime(new Date());
		vo.setState("002");
		return this.saveOne(vo);
	}
}


