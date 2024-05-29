package vn.finder.pet.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.common.RandomCommon;

@Service
public class TwoFactorAuthPasswordsService {
    private MailService mailService;
    private HttpSession session;

    @Autowired
    public TwoFactorAuthPasswordsService(HttpSession session, MailService mailService) {
        this.session = session;
        this.mailService = mailService;
    }

    /**
     * Gửi passwords dùng 1 lần đến email
     * @param email email nhận passwords
     */
    public void sendOneTimePasswords(String email) {
        String oneTimePasswords = RandomCommon.randomString(6).toString();
        session.setAttribute("one-time-passwords",oneTimePasswords);
        mailService.sendmail(mailService.getSend(),
                "Password",
                "Password ngẫu nhiên là:" + oneTimePasswords,
                email);
    }

    /**
     * Xác thực mật khẩu dùng một lần
     * @param otp mật khẩu dùng một lần
     * @return true nếu mật khẩu đúng, ngược lại false
     */
    public boolean validateOneTimePassword(String otp) {
        return otp.equals(session.getAttribute("one-time-passwords"));
    }

    /**
     * Thực hiện TwoFactorAuthPassword và kiểm tra password với password cũ
     * @param password mật khẩu người muốn đổi
     */
    public void checkTwoFactorAuthPassword(String password){
        session.removeAttribute("one-time-passwords");
        //Đổi mật khẩu trong database của người dùng

    }
}