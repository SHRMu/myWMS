package de.demarks.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import de.demarks.wms.common.service.Interface.PacketRefMangeService;
import de.demarks.wms.dao.PacketInMapper;
import de.demarks.wms.dao.PacketRefMapper;
import de.demarks.wms.domain.PacketInDO;
import de.demarks.wms.domain.PacketDTO;
import de.demarks.wms.domain.PacketRef;
import de.demarks.wms.exception.PacketManageServiceException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PacketRefManageServiceImpl implements PacketRefMangeService {

    @Autowired
    private PacketInMapper packetInMapper;
    @Autowired
    private PacketRefMapper packetRefMapper;

    @Override
    public Map<String, Object> selectRefApproximate(String trace, String status, Integer repositoryID) throws PacketManageServiceException {
        return selectRefApproximate(trace, status, repositoryID, -1, -1);
    }

    @Override
    public Map<String, Object> selectRefApproximate(String trace, String status, Integer repositoryID, int offset, int limit) throws PacketManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketRef> packetRefList;

        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;
        if (repositoryID<0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetRefList = packetRefMapper.selectByTraceApproximate(trace,status,repositoryID);
                if (packetRefList != null) {
                    PageInfo<PacketRef> pageInfo = new PageInfo<>(packetRefList);
                    total = pageInfo.getTotal();
                } else
                    packetRefList = new ArrayList<>();
            } else {
                packetRefList = packetRefMapper.selectByTraceApproximate(trace,status,repositoryID);
                if (packetRefList != null)
                    total = packetRefList.size();
                else
                    packetRefList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }

        List<PacketDTO> packetDTOS = new ArrayList<>();
        packetRefList.forEach(packetRef -> packetDTOS.add(packetRefConvertToPacketDTO(packetRef)));

        resultSet.put("data", packetDTOS);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 添加附加包裹
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public boolean addPacketRef(PacketInDO packetDO) throws PacketManageServiceException {
        try{
            if ( packetDO != null){
                Integer refID = packetDO.getId(); //主单号
                String desc = packetDO.getDesc(); //附单运单号
                List<PacketRef> packetRefList = packetRefMapper.selectByRefID(refID,null);
                if (!packetRefList.isEmpty()){
                    deletePacketRef(refID);
                }
                packetRefMapper.insert(packetDO.getTrace(),refID); //添加主单信息
                if (desc==null || desc.equals(""))
                    return true;
                if (desc.contains(",")){
                    String[] traces = desc.split(",");
                    for (String trace:
                            traces) {
                        PacketRef packetRef = packetRefMapper.selectByTrace(trace,refID,null);
                        if (packetRef == null)
                            packetRefMapper.insert(trace, refID);
                    }
                }else{
                    PacketRef packetRef = packetRefMapper.selectByTrace(desc,refID,null);
                    if ( packetRef == null)
                        packetRefMapper.insert(desc, refID);
                }
                return true;
            }
            return false;
        }catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    @Override
    public boolean updatePacketRef(PacketInDO packetDO) throws PacketManageServiceException {
        try {
            if (packetDO !=null && packetRefCheck(packetDO)){
                addPacketRef(packetDO);
                return true;
            }
            return false;
        }catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    /**
     * 删除附加包裹信息
     * @param refID
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public boolean deletePacketRef(Integer refID) throws PacketManageServiceException {
        try{
            if (refID != null){
                packetRefMapper.deleteByRefID(refID);
                return true;
            }
            return false;
        }catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }


    /**
     * 检查附加包裹信息是否修改
     * @param packetDO
     * @return
     */
    private boolean packetRefCheck(PacketInDO packetDO){
        String desc = packetDO.getDesc();
        String oldDesc = packetInMapper.selectByPacketID(packetDO.getId()).getDesc();
        if (desc.equalsIgnoreCase(oldDesc))
            return false;
        return true;
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");

    private PacketDTO packetRefConvertToPacketDTO(PacketRef packetRef){
        PacketDTO packetDTO = new PacketDTO();
        packetDTO.setId(packetRef.getId());
        packetDTO.setTrace(packetRef.getTrace());
        packetDTO.setRefid(packetRef.getRefid());
        packetDTO.setReftrace(packetDTO.getReftrace());
        packetDTO.setTime(dateFormat.format(packetRef.getTime()));
        packetDTO.setStatus(packetRef.getStatus());
        if (packetRef.getTrace().equalsIgnoreCase(packetRef.getReftrace()))
            packetDTO.setDesc(packetRef.getDesc()); //只有主单保留子单描述信息
        packetDTO.setRepositoryID(packetRef.getRepositoryID());
        return packetDTO;
    }
}
