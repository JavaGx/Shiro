package cn.gx.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSend {

	private MimeMessage mimeMessage;//邮件对象
	private Session session;//邮件会话对象
	private Properties prop=new Properties();//系统属性
	private Multipart multipart;
	private String email;
	
	public MailSend(String email) {
		super();
		this.email = email;
	}

	public void sendMail(){
		prop.setProperty("mail.transport.protocol","smtp");
		prop.setProperty("mail.host","smtp.qq.com");
		prop.setProperty("mail.smtp.auth","true");//请求身份验证
		session = Session.getDefaultInstance(prop,new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("2546359158","01smtp");
			}
		} );
		
		mimeMessage=new MimeMessage(session);//邮件对象
		
		
		try {
			InternetAddress fromAddress = new InternetAddress("2546359158@qq.com");
			InternetAddress toAddress = new InternetAddress(email);
			
			mimeMessage.setFrom(fromAddress);//设置发送者邮件地址
			
			mimeMessage.setRecipient(RecipientType.TO, toAddress);
			
			mimeMessage.setSubject("java邮件");//设置邮件主题
			
			multipart=new MimeMultipart();
			
			MimeBodyPart bodyPart = new MimeBodyPart();
			
			bodyPart.setContent("欢迎您注册我们的网站!", "text/html;charset=utf-8");
			
			multipart.addBodyPart(bodyPart);
			
			mimeMessage.setContent(multipart);
			
			Transport.send(mimeMessage);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
}
