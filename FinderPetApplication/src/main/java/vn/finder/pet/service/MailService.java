package vn.finder.pet.service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {
	private JavaMailSender mailsender;
	private OtpService otp;

	@Value("${spring.mail.username}")
	private String send;

	@Autowired
	public MailService(JavaMailSender mailsender, OtpService otp) {
		this.mailsender = mailsender;
		this.otp = otp;
	}

	public void sendmail(String mail,String subject, String text) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(mail);
		ml.setSubject(subject);
		ml.setText(text);
		mailsender.send(ml);
	}

	public String getSend() {
		return send;
	}

	public void sendmail(String mail, String subject, String text,String toMail) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(mail);
		ml.setSubject(subject);
		ml.setText(text);
		ml.setTo(toMail);
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
