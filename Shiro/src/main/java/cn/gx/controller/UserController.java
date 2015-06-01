package cn.gx.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gx.entity.Role;
import cn.gx.entity.User;
import cn.gx.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService us;
	
	@Resource
	private ObjectMapper objectMapper;
	
	@RequestMapping(value="/register.do")
	public String register(User user,Integer[] ids,Model model){
		
		User u=us.findUserByUsername(user.getUsername());
		String email=us.findEmail(user.getEmail());
		if(u==null&&email==null){
			us.register(user,ids);
			return "redirect:/login.html";
		}
		model.addAttribute("message", "用户名已存在");
		return "/register";
	}
	
	@RequestMapping("/showRoles.do")
	@ResponseBody
	public String showRoles(){
		List<Role> roles=us.findAllRoles();
		String json=null;
		try {
			json = objectMapper.writeValueAsString(roles);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/login.do")
	public String login(){
		return "/index";
	}
	
	@RequestMapping("/logout.do")
	public String logout(){
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			subject.logout();
			return "redirect:/login.html";
		}
		return null;
	}
	
	@RequestMapping("/personInfo.do")
	public String personInfo(){
		
		return "/user/personInfo";
	}
	
	@RequestMapping("/updateInfo.do")
	public String updateInfo(){
		
		return "/user/editInfo";
	}
	
	@RequestMapping("/tolist.do")
	public String list(){
		return "/user/list";
	}
	
	@RequestMapping("/firstPage.do")
	@ResponseBody
	public String firstPage(Model model){
		int total=us.findRecords();
		int endIndex=total<3?total:3;
		
		List<User> users=us.pageList(0,endIndex);
		
		if(users!=null&&users.size()>0){
			for(User user:users){
				List<Role> list = us.findRolesByUserId(user.getId());
				user.setRoles(list);
			}
		}
		String json=null;
		try {
			String jsonUsers = objectMapper.writeValueAsString(users);
			json="{\"total\":"+total+",\"rows\":"+jsonUsers+"}";
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	@RequestMapping("/userList.do")
	@ResponseBody
	public String list(Model model,int beginIndex,int endIndex){
		int total=us.findRecords();
		List<User> users=us.pageList(beginIndex,endIndex);
		if(users!=null&&users.size()>0){
			for(User user:users){
				List<Role> list = us.findRolesByUserId(user.getId());
				user.setRoles(list);
			}
		}
		String json=null;
		try {
			String jsonUsers = objectMapper.writeValueAsString(users);
			json="\"total\":"+total+",\"rows\":"+jsonUsers;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping("/downloadUserInfo.do")
	public String downloadUserInfo(HttpServletResponse response){
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("编号");cell.setCellStyle(cellStyle);
		cell = row.createCell(1); 
		cell.setCellValue("用户名");cell.setCellStyle(cellStyle);
		cell = row.createCell(2); 
		cell.setCellValue("密码");cell.setCellStyle(cellStyle);
		cell = row.createCell(3); 
		cell.setCellValue("身份");cell.setCellStyle(cellStyle);
		
		List<User> users=us.findAllUsers();
		if(users!=null&&users.size()>0){
			for(User user:users){
				List<Role> list = us.findRolesByUserId(user.getId());
				user.setRoles(list);
			}
		}
		
		if(users!=null&&users.size()>0){
			for(int i=0;i<users.size();i++){
				HSSFRow row2 = sheet.createRow(i+1);
				HSSFCell cell2 = row2.createCell(0);
				cell2.setCellValue(users.get(i).getId());cell2.setCellStyle(cellStyle);
				cell2 = row2.createCell(1);
				cell2.setCellValue(users.get(i).getUsername());cell2.setCellStyle(cellStyle);
				cell2 = row2.createCell(2);
				cell2.setCellValue(users.get(i).getPassword());cell2.setCellStyle(cellStyle);
				cell2 = row2.createCell(3);
				String roleName=null;
				List<Role> roles = users.get(i).getRoles();
				for(int y=0;y<roles.size();y++){
					if(y==0){
						roleName=roles.get(y).getRole_name();
					}else{
						roleName+=","+roles.get(y).getRole_name();
					}
				}
				cell2.setCellValue(roleName);
				cell2.setCellStyle(cellStyle);
			}
		}
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		try {
			workbook.write(os);
			
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			String fileName="用户信息";
			response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+".xls,");
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] array = os.toByteArray();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
		
		byte[] b=new byte[1024];
		int len=0;
		ServletOutputStream out=null;
		try {
			out = response.getOutputStream();
			while((len=inputStream.read(b))!=-1){
				out.write(b, 0, len);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
