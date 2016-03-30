package authority.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smarthome.core.util.JsonUtils;
import com.smarthome.core.util.ObjectConvertUtil;
import com.smarthome.core.util.PageBean;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.authority.service.RoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml" })
public class RoleDaoTest {
	
	@Resource
	private RoleService roleService;
	@Test
	public void getRoleByParamsTest(){
		PageBean pageBean = new PageBean(1,3);
		System.out.println(JsonUtils.getJAVABeanJSON(this.roleService.getRoleByPage(pageBean)));
		System.out.println("当前总数目:"+pageBean.getTotalItems());
		System.out.println("当前总页数:"+pageBean.getAllPageTotal());
	}
	@Test
	public void getRoleWithAuthorityById(){
//		System.out.println("该角色下的具体详细信息:"+JsonUtils.getJAVABeanJSON(this.roleService.getRoleWithAuthorityById("b6d7ae00-661c-4695-af4c-f3ba01cd783a")));
		Admin admin = new Admin();
		admin.setUserId("dadada");
		admin.setRealName("ddsds");
		admin.setEnable(1);
		admin.setStartTime(new Date());
		admin.setEndTime(new Date());
		System.out.println("转换成的MAP对象:"+JsonUtils.getJAVABeanJSON(ObjectConvertUtil.beanToMap(admin)));
	
	}

}
