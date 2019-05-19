package com.tje.baseballgame.datas;

// 채팅 아이템 데이터 모델
public class Chat {

    // 사용자의 채팅인지 컴퓨터의 채팅인지 구분할 bool 변수
    public boolean userSaid;
    // 메세지 내용을 담을 문자열 변수
    public String message;

    // 챗 아이템의 생성자
    public Chat(boolean userSaid, String message) {
        this.userSaid = userSaid;
        this.message = message;
    }
}
