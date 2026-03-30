package com.mega.greentrade.service;

import com.mega.greentrade.entity.BuyList;
import com.mega.greentrade.repository.BuyListRepository;
import com.mega.greentrade.repository.LogRepository;
import com.mega.greentrade.entity.TradeLog;
import com.mega.greentrade.dto.ProductDTO;
import com.mega.greentrade.repository.ProductRepository;
import com.mega.greentrade.entity.SellList;
import com.mega.greentrade.repository.SellListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PayService {

    private final ProductRepository productRepository;
    private final LogRepository logRepository;
    private final BuyListRepository buyListRepository;
    private final SellListRepository sellListRepository;

    @Transactional
    public void paySuccess(ProductDTO productDto, int buyUserno) {
        // 판매 로그 → 거래완료 처리
        logRepository.markSellComplete(productDto.getProductno(), buyUserno);

        // 구매 로그 생성
        TradeLog buyLog = new TradeLog();
        buyLog.setTradestartdate(Date.valueOf(LocalDate.now()));
        buyLog.setTradesuccessdate(Date.valueOf(LocalDate.now()));
        buyLog.setTradetype("구매");
        buyLog.setTrademethod("택배거래");
        buyLog.setTradestatus("거래완료");
        buyLog.setProductno(productDto.getProductno());
        buyLog.setBuyuserno(buyUserno);
        buyLog.setSelluserno(productDto.getUserno());
        logRepository.save(buyLog);

        // 상품 판매완료 처리
        productRepository.markSold(productDto.getProductno());

        // 구매 목록 저장
        BuyList buyList = new BuyList();
        buyList.setBuylistdate(Date.valueOf(LocalDate.now()));
        buyList.setIsreview("no");
        buyList.setProductno(productDto.getProductno());
        buyList.setBuyuserno(buyUserno);
        buyList.setSelluserno(productDto.getUserno());
        buyListRepository.save(buyList);

        // 판매 목록 저장
        SellList sellList = new SellList();
        sellList.setSelllistdate(Date.valueOf(LocalDate.now()));
        sellList.setProductno(productDto.getProductno());
        sellList.setBuyuserno(buyUserno);
        sellList.setSelluserno(productDto.getUserno());
        sellListRepository.save(sellList);
    }
}
