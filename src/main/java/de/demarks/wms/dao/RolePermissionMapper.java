package de.demarks.wms.dao;

import de.demarks.wms.domain.RolePermissionDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限信息 Mapper
 *
 * @author ken
 * @since  2017/2/26.
 */
@Repository
public interface RolePermissionMapper {

    List<RolePermissionDO> selectAll();
}
