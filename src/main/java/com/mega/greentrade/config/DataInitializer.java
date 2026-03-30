package com.mega.greentrade.config;

import com.mega.greentrade.entity.Address;
import com.mega.greentrade.entity.Faq;
import com.mega.greentrade.entity.Product;
import com.mega.greentrade.entity.User;
import com.mega.greentrade.repository.AddressRepository;
import com.mega.greentrade.repository.FaqRepository;
import com.mega.greentrade.repository.ProductRepository;
import com.mega.greentrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FaqRepository faqRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        // 테스트 유저 생성
        User admin = createUser("admin", "admin1234", "관리자", "010-0000-0000",
                "admin@greentrade.com", "그린관리자", "img/defaultprofile.png", "manager",
                "서울시 강남구 테헤란로 123", "101호", "06234", 100, 5);

        User user1 = createUser("user1", "user1234", "홍길동", "010-1111-2222",
                "user1@greentrade.com", "친환경러버", "img/defaultprofile.png", "user",
                "서울시 마포구 합정동 456", "202호", "04045", 50, 3);

        User user2 = createUser("user2", "user1234", "김환경", "010-3333-4444",
                "user2@greentrade.com", "에코킹", "img/defaultprofile.png", "user",
                "경기도 성남시 분당구 789", "303호", "13590", 30, 1);

        userRepository.save(admin);
        userRepository.save(user1);
        userRepository.save(user2);

        // 기본 배송지 생성
        addressRepository.save(createAddress(admin.getUser_name(), admin.getUser_call(),
                admin.getAddress1(), admin.getAddress2(), admin.getPostnum(), admin.getUser_id()));
        addressRepository.save(createAddress(user1.getUser_name(), user1.getUser_call(),
                user1.getAddress1(), user1.getAddress2(), user1.getPostnum(), user1.getUser_id()));
        addressRepository.save(createAddress(user2.getUser_name(), user2.getUser_call(),
                user2.getAddress1(), user2.getAddress2(), user2.getPostnum(), user2.getUser_id()));

        // 테스트 상품 생성 (user1 기준, userno는 DB 시퀀스에 따라 달라지므로 저장 후 참조)
        Date today = Date.valueOf(LocalDate.now());

        Product p1 = new Product();
        p1.setTitle("친환경 텀블러 (스탠리 듀프)")
        ;
        p1.setPrice("15000");
        p1.setLikenum(3);
        p1.setProductStatus("최상");
        p1.setProductDetail("한 번만 사용한 친환경 텀블러입니다. 스크래치 없음.");
        p1.setPaymethod("직거래/택배");
        p1.setTrademethod("직거래");
        p1.setSellstatus("판매중");
        p1.setAdddate(today);
        p1.setEditdate(today);
        p1.setViewcount(10);
        p1.setUserno(user1.getUserno());
        p1.setImage("img/defaultproduct.png");
        p1.setTradelocation("서울시 마포구");

        Product p2 = new Product();
        p2.setTitle("재활용 가방 (에코백)");
        p2.setPrice("8000");
        p2.setLikenum(1);
        p2.setProductStatus("상");
        p2.setProductDetail("친환경 소재로 만든 에코백입니다. 실생활에 편리해요.");
        p2.setPaymethod("택배");
        p2.setTrademethod("택배");
        p2.setSellstatus("판매중");
        p2.setAdddate(today);
        p2.setEditdate(today);
        p2.setViewcount(5);
        p2.setUserno(user1.getUserno());
        p2.setImage("img/defaultproduct.png");
        p2.setTradelocation("서울시 마포구");

        Product p3 = new Product();
        p3.setTitle("유기농 비누 세트");
        p3.setPrice("12000");
        p3.setLikenum(2);
        p3.setProductStatus("최상");
        p3.setProductDetail("천연재료로 만든 유기농 비누 3개 세트입니다.");
        p3.setPaymethod("택배");
        p3.setTrademethod("택배");
        p3.setSellstatus("판매중");
        p3.setAdddate(today);
        p3.setEditdate(today);
        p3.setViewcount(8);
        p3.setUserno(user2.getUserno());
        p3.setImage("img/defaultproduct.png");
        p3.setTradelocation("경기도 성남시");

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);

        // FAQ 생성
        Faq faq1 = new Faq();
        faq1.setFaqtitle("회원가입은 어떻게 하나요?");
        faq1.setFaqcontent("상단 메뉴의 '회원가입' 버튼을 클릭하여 정보를 입력하시면 가입할 수 있습니다.");
        faq1.setFaqdate(today);
        faq1.setFaqcate("이용안내");

        Faq faq2 = new Faq();
        faq2.setFaqtitle("비밀번호를 잊어버렸어요.");
        faq2.setFaqcontent("로그인 페이지의 '비밀번호 찾기'를 이용해 이메일로 임시 비밀번호를 받을 수 있습니다.");
        faq2.setFaqdate(today);
        faq2.setFaqcate("계정");

        Faq faq3 = new Faq();
        faq3.setFaqtitle("그린스코어는 무엇인가요?");
        faq3.setFaqcontent("그린스코어는 친환경 거래 활동에 따라 적립되는 점수입니다. 판매 완료 시 점수가 올라갑니다.");
        faq3.setFaqdate(today);
        faq3.setFaqcate("이용안내");

        Faq faq4 = new Faq();
        faq4.setFaqtitle("거래 취소는 어떻게 하나요?");
        faq4.setFaqcontent("결제 완료 전 채팅을 통해 판매자와 협의하시거나, 고객센터로 문의해 주세요.");
        faq4.setFaqdate(today);
        faq4.setFaqcate("거래");

        Faq faq5 = new Faq();
        faq5.setFaqtitle("상품 신고는 어떻게 하나요?");
        faq5.setFaqcontent("상품 상세 페이지 하단의 '신고하기' 버튼을 이용해 신고할 수 있습니다.");
        faq5.setFaqdate(today);
        faq5.setFaqcate("신고");

        faqRepository.save(faq1);
        faqRepository.save(faq2);
        faqRepository.save(faq3);
        faqRepository.save(faq4);
        faqRepository.save(faq5);
    }

    private User createUser(String userId, String rawPassword, String name, String call,
                             String email, String nickname, String imgurl, String staff,
                             String address1, String address2, String postnum,
                             int greenscore, int sellcount) {
        User user = new User();
        user.setUser_id(userId);
        user.setUser_password(passwordEncoder.encode(rawPassword));
        user.setUser_name(name);
        user.setUser_call(call);
        user.setEmail(email);
        user.setNickname(nickname);
        user.setImgurl(imgurl);
        user.setStaff(staff);
        user.setAddress1(address1);
        user.setAddress2(address2);
        user.setPostnum(postnum);
        user.setGreenscore(greenscore);
        user.setSellcount(sellcount);
        return user;
    }

    private Address createAddress(String name, String phone, String address1,
                                   String address2, String postnum, String userid) {
        Address address = new Address();
        address.setReceivername(name);
        address.setPhone(phone);
        address.setAddress1(address1);
        address.setAddress2(address2);
        address.setPostnum(postnum);
        address.setUserid(userid);
        return address;
    }
}
