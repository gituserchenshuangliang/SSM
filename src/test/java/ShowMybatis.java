import com.ssm.config.RootConfig;
import com.ssm.service.UserRedPacketService;
import com.ssm.util.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 演示
 * @author Cherry
 * 2020年5月28日
 */
@WebAppConfiguration//使用Spring MVC则一定要加@WebAppConfiguration注解，否则无法载入web上下文环境
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class ShowMybatis {
    @Autowired
    private UserRedPacketService userRedPacketService = null;
    Logger logger = LoggerUtil.getLog(ShowMybatis.class);

    @Test
    public void show(){
        long s = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++){
            userRedPacketService.grapRedPacket((long)4,(long)(30005+i));
        }
        long e = System.currentTimeMillis();
        logger.info("耗时："+ (e-s)/1000 +"s");
    }

    //多线程
    public Thread runAdd(int i){
        return  new Thread(() -> {
            userRedPacketService.grapRedPacket((long)4,(long)(20005+i));
        });
    }
}