package com.mega.greentrade.service;
import com.mega.greentrade.dto.JoinDTO;
import com.mega.greentrade.dto.UserDTO;
import com.mega.greentrade.entity.User;
import com.mega.greentrade.repository.UserRepository;

import com.mega.greentrade.entity.Address;
import com.mega.greentrade.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public boolean isDuplicateId(String userId) {
        return userRepository.existsByUser_id(userId);
    }

    public void join(JoinDTO dto) {
        dto.setStaff("user");
        if (dto.getImgurl() == null || dto.getImgurl().isEmpty()) {
            dto.setImgurl("img/defaultprofile.png");
        }

        User user = new User();
        user.setUser_name(dto.getUser_name());
        user.setUser_call(dto.getUser_call());
        user.setUser_id(dto.getUser_id());
        user.setUser_password(passwordEncoder.encode(dto.getUser_password()));
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setImgurl(dto.getImgurl());
        user.setStaff(dto.getStaff());
        user.setAddress1(dto.getAddress1());
        user.setAddress2(dto.getAddress2());
        user.setPostnum(dto.getPostnum());
        user.setGreenscore(0);
        user.setSellcount(0);
        userRepository.save(user);

        Address address = new Address();
        address.setReceivername(dto.getUser_name());
        address.setPhone(dto.getUser_call());
        address.setAddress1(dto.getAddress1());
        address.setAddress2(dto.getAddress2());
        address.setPostnum(dto.getPostnum());
        address.setUserid(dto.getUser_id());
        addressRepository.save(address);
    }

    public UserDTO findUserByEmailAndCall(String email, String userCall) {
        return userRepository.findByEmailAndUser_call(email, userCall)
                .map(UserDTO::from).orElse(null);
    }

    public boolean sendTempPassword(String userId) {
        return userRepository.findByUser_id(userId).map(user -> {
            String tempPassword = UUID.randomUUID().toString().substring(0, 8);
            userRepository.updatePassword(userId, passwordEncoder.encode(tempPassword));

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("[GreenTrade] 임시 비밀번호 안내");
            message.setText("임시 비밀번호: " + tempPassword + "\n로그인 후 비밀번호를 변경해주세요.");
            mailSender.send(message);
            return true;
        }).orElse(false);
    }

    public int updatePassword(String userId, String newPassword) {
        return userRepository.updatePassword(userId, newPassword);
    }

    public UserDTO getUserById(String userId) {
        return userRepository.findByUser_id(userId).map(UserDTO::from).orElse(null);
    }

    public UserDTO getUserByUserno(int userno) {
        return userRepository.findById(userno).map(UserDTO::from).orElse(null);
    }

    public UserDTO getSellerInfo(int userno) {
        return userRepository.findSellerInfo(userno).map(p -> {
            UserDTO dto = new UserDTO();
            dto.setUserno(p.getUserno());
            dto.setNickname(p.getNickname());
            dto.setImgurl(p.getImgurl());
            dto.setGreenscore(p.getGreenscore());
            dto.setReviewCount(p.getReviewCount());
            return dto;
        }).orElseGet(() -> getUserByUserno(userno));
    }

    public void deleteUser(int userno) {
        userRepository.deleteById(userno);
    }
}
