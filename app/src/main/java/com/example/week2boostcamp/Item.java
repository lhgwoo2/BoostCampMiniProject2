package com.example.week2boostcamp;

import java.util.Date;

/**
 * Created by 현기 on 2017-07-14.
 */

public class Item {

    String contents;           // 내용
    int imgId;              // 이미지 id값
    String title;              // 제목
    boolean checked;        // 선택되었는지 변수
    int popularCount;      // 인기순 정렬 사용 변수
    Date latestCount;          // 거리순 정렬 사용 변수
    int distanceCount;      // 최근순 정렬 사용 변수
}
