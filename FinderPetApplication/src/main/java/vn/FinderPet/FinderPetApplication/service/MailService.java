package vn.FinderPet.FinderPetApplication.service;

import java.util.concurrent.ExecutionException;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Template;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;




@Service	
public class MailService {
	@Autowired
	private JavaMailSender mailsender;
	@Autowired
	OtpService otp;	

	@Value("${spring.mail.username}")
	private String send;
	
	public void sendmail(String mail,String subject, String text) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(mail);
		ml.setSubject(subject);
		ml.setText(text);
		mailsender.send(ml);
	}

	public void MaxacNhan(String mailto) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(send);
		ml.setSubject("Mã Xác Nhận");
		try {
			ml.setText("Mã Xác nhận Đăng Ký: "+otp.generateOTP(mailto)+"\n"
			+"Vui lòng nhập Mã Xác Thực để Hoàn Thành Quá Trình Đăng Ký Hiệu Lực Trong Vòng 1 Phút");
		} catch (ExecutionException e) {
			e.printStackTrace();
		}	
		ml.setTo(mailto);
		System.out.println(ml.getText());
		mailsender.send(ml);
	}
	
	public void ResetPassWord(String mailto) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(send);
		ml.setSubject("Mật Khẩu Mới Của Bạn");
		ml.setText("Mã Xác nhận Đăng Ký: "+1+"\n"
		+"Vui Đăng Nhập Và đổi Lại Mật Khẩu Theo Đường link"+"\n"+"https://www.youtube.com/watch?v=ihY0Hjv2fK0");	
		ml.setTo(mailto);
		mailsender.send(ml);
	}
	
	
}
