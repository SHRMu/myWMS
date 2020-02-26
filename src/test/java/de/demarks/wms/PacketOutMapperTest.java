package de.demarks.wms;


import de.demarks.wms.dao.PacketOutMapper;
import de.demarks.wms.domain.PacketOutDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration(locations = {"classpath:config/SpringApplicationConfiguration.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class PacketOutMapperTest {

    @Autowired
    PacketOutMapper packetOutMapper;

//    @Test
//    public void selectAllTest(){
//        List<PacketOutDO> packetDOS = packetOutMapper.selectAll(null, null);
//        System.out.println(packetDOS.size());
//    }

//    @Test
//    public void selectByPacketIDTest(){
//        PacketOutDO packetDO = packetOutMapper.selectByPacketID(1);
//        if (packetDO!=null)
//            System.out.println(packetDO.toString());
//    }

//    @Test
//    public void selectByTraceApproximateTest(){
//        List<PacketInDO> packetDOS = packetInMapper.selectByTraceApproximate("121", "发货中", 2001, 3001);
//        System.out.println(packetDOS.size());
//    }

//    @Test
//    public void selectByDate(){
//        List<PacketInDO> packetDOS = packetInMapper.selectByDate(2001, 3001, null, new Date());
//        System.out.println(packetDOS.size());
//    }

//    @Test
//    public void insertTest(){
//        PacketInDO packetDO = new PacketInDO();
//
//    }

//    @Test
//    public void insertBatchTest(){
//        List<PacketInDO> packetDOS = packetInMapper.selectByTraceApproximate("", null, null, null);
//        for (PacketInDO packetDO:
//             packetDOS) {
//            packetDO.setTime(new Date());
//        }
//        packetInMapper.insertBatch(packetDOS);
//    }

//    @Test
//    public void updateTest(){
//        PacketInDO packetDO = packetInMapper.selectByPacketID(4);
//        if (packetDO != null){
//            packetDO.setStatus("已签收");
//            packetInMapper.update(packetDO);
//        }
//    }

//    @Test
//    public void deleteByPacketIDTest(){
//        packetInMapper.deleteByPacketID(4);
//    }
}
