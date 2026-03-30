package com.mega.greentrade.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatDAO {

    private final JdbcTemplate jdbcTemplate;

    public int getOrCreateChatroom(int buyer, int sellproduct) {
        String selectSql = "SELECT chatroom FROM chatroom WHERE buyer=? AND sellproduct=?";
        List<Integer> result = jdbcTemplate.query(selectSql,
                (rs, rowNum) -> rs.getInt("chatroom"), buyer, sellproduct);

        if (!result.isEmpty()) {
            return result.get(0);
        }

        jdbcTemplate.update(
            "INSERT INTO chatroom (chatroom, buyer, sellproduct) VALUES(chat_room_seq.NEXTVAL, ?, ?)",
            buyer, sellproduct);
        return -1;
    }
}
