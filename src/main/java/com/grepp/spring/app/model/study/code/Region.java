package com.grepp.spring.app.model.study.code;

public enum Region {
    SEOUL("서울"),
    INCHEON("인천"),
    GYEONGGI("경기"),
    DAEJEON("대전"),
    GANGWON("강원"),
    SEJONG("세종"),
    CHUNGBUK("충북"),
    ONLINE("온라인");

    private final String koreanName;

    Region(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
