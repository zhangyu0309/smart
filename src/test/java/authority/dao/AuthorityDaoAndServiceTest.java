package authority.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.authority.dao.jdbc.AuthorityJDBCDao;
import com.smarthome.platform.authority.service.AdminService;
import com.smarthome.platform.authority.service.AuthorityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml" })
public class AuthorityDaoAndServiceTest {
	@Resource
	private AuthorityJDBCDao authorityJdbcDao ;
	@Resource
	private AdminService adminService;
	@Test
	public void getMenuAuthorityByUserIdTest(){
		System.out.println("当前获取的功能菜单是:"+JsonUtils.getJAVABeanJSON(this.authorityJdbcDao.getMenuAuthorityByUserId("535e1191-3a85-4ad7-b9a0-7f61d3910663")));
	}
	@Test
	public void getOpAuthorityByUserIdTest(){
		System.out.println("当前用户的操作权限是:"+JsonUtils.getJAVABeanJSON(this.authorityJdbcDao.getOpAuthorityByUserId("lipeipei")));
	}
	@Test
	public void getOpAuthorityByUserIdForServiceTest(){
		System.out.println("当前用户的操作权限是:"+JsonUtils.getJAVABeanJSON(this.adminService.getOPAuthorityList("lipeipei")));
	}

}
