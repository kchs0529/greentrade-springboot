package com.mega.greentrade.user;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final JavaMailSender mailSender;

    public boolean isDuplicateId(String userId) {
        return userDAO.existsById(userId);
    }

    public int join(JoinDTO dto) {
        dto.setStaff("user");
        if (dto.getImgurl() == null || dto.getImgurl().isEmpty()) {
            dto.setImgurl("img/defaultprofile.png");
        }
        int result = userDAO.insertUser(dto);
        if (result > 0) {
            userDAO.insertAddress(dto);
        }
        return result;
    }

    public UserDTO findUserByEmailAndCall(String email, String userCall) {
        return userDAO.findByEmailAndCall(email, userCall);
    }

    public boolean sendTempPassword(String userId) {
        UserDTO user = userDAO.findById(userId);
        if (user == null) return false;

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        userDAO.updatePassword(userId, tempPassword);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("[GreenTrade] 임시 비밀번호 안내");
        message.setText("임시 비밀번호: " + tempPassword + "\n로그인 후 비밀번호를 변경해주세요.");
        mailSender.send(message);
        return true;
    }

    public int updatePassword(String userId, String newPassword) {
        return userDAO.updatePassword(userId, newPassword);
    }

    public UserDTO getUserById(String userId) {
        return userDAO.findById(userId);
    }

    public UserDTO getUserByUserno(int userno) {
        return userDAO.findByUserno(userno);
    }

    public UserDTO getSellerInfo(int userno) {
        return userDAO.getSellerInfo(userno);
    }
}
