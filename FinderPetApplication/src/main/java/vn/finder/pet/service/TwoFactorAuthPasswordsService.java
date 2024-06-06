package vn.finder.pet.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.common.PasswordCommon;
import vn.finder.pet.dao.UsersDAO;
import vn.finder.pet.common.RandomCommon;
import vn.finder.pet.entity.Users;


@Service
public class TwoFactorAuthPasswordsService {
    private MailService mailService;
    private HttpSession session;
    private UsersDAO usersDAO;

    @Autowired
    public TwoFactorAuthPasswordsService(HttpSession session, MailService mailService, UsersDAO usersDAO) {
        this.session = session;
        this.mailService = mailService;
        this.usersDAO = usersDAO;
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
     * Thực hiện kiểm tra password với password cũ
     * nếu khác thì sẽ đổi password
     * nếu sai thì không thực hiện đổi password
     *
     * @param password mật khẩu người muốn đổi
     * @return true nếu khác password cũ, false nếu giống password cũ
     */
    public boolean checkTwoFactorAuthPassword(String password) {
        Users user = usersDAO.findById(session.getAttribute("emailUs").toString()).get();
        System.out.println(user.toString());

        if (password.isEmpty()) {
            return false;
        }

        if (password.equals(user.getPassword())) {
            return false;
        } else {
            System.out.println("mật khẩu khác mk cũ");
            if (PasswordCommon.validatePatternPassword(password)) {
                //Đổi mật khẩu trong database của người dùng
                user.setPassword(password);
                usersDAO.save(user);
                //Xoá mật khẩu dùng một lần
                session.removeAttribute("one-time-passwords");
                return true;
            }
            return false;
        }
    }
}