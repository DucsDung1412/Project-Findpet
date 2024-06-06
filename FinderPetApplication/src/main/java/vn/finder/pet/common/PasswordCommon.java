package vn.finder.pet.common;

public class PasswordCommon {
    private static final String pattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+=-])(?=.*\\d).{8,20}$";
    /**
     * Dùng để kiểm tra pattern Password
     *
     * @param password password cần kiểm tra
     * @return true nếu đúng định dạng, false nếu sai định dạng
     */
    public static boolean validatePatternPassword(String password) {
        if (password.matches(pattern)) {
            System.out.println("đúng định dạng password");
            return true;
        } else {
            System.out.println("sai định dạng password");
            return false;
        }
    }
}

