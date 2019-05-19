package com.tje.baseballgame;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.tje.baseballgame.adapters.ChatAdapter;
import com.tje.baseballgame.databinding.ActivityMainBinding;
import com.tje.baseballgame.datas.Chat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding act;

    // 컴퓨터가 생성한 세자리 난수를 3개로 분할하여 담을 배열 선언 ex) 741 => 7, 4, 1
    int[] computerExamArray = new int[3];

    // 채팅 형태로 보여주기 위한 리스트뷰
    List<Chat> chatList = new ArrayList<>();
    // 채팅 아이템과 뷰를 붙여주는 어댑터
    ChatAdapter mChatAdapter;

    /* 사용자의 입력 횟수를 출력해주기 위한 변수 선언 */
    int userAnswerCount = 0;
    String userAnswerCountTxt; // setOnClickListener 메소드는 int 값을 리턴할 수 없기 때문에 입력 횟수를 String 으로

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        // 입력버튼 클릭 이벤트
        act.inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 채팅 리스트에 새로운 채팅을 추가한다.(조건: 유저와 컴퓨터 중 유저의 채팅일 경우, 입력란에 입력된 텍스트를 문자열로 변환하여 인자로 가져온다.)
                chatList.add(new Chat(true, act.userInputEdt.getText().toString()));
                // 채팅 어댑터를 새로고침 한다.
                mChatAdapter.notifyDataSetChanged();

                // checkStrikeAndBalls() 메소드를 실행
                checkStrikeAndBalls();

                userAnswerCount++;
                userAnswerCountTxt = Integer.toString(userAnswerCount);

            }
        });
    }

    // 사용자가 입력한 숫자가 스트라이크인지 볼인지 판별하는 메소드
    void checkStrikeAndBalls() {

        // 사용자가 입력한 숫자를 담을, 인자가 3개인 배열 선언
        int[] userInputArray = new int[3];
        // 낱개의 숫자 3자리를 하나의 세자리 숫자로 치환하여 변수에 담는다.
        int number = Integer.parseInt(act.userInputEdt.getText().toString());

        // 배열의 0번째 자리에는 세자리 수를 100으로 나눠 몫을 담는다. => 100의 자리가 어떤 숫자인지 알 수 있다.
        userInputArray[0] = number / 100;
        // 배열의 1번째 자리에는 세자리 수를 10으로 나눈 몫을 10으로 다시 나누어 나머지를 담는다 . => 10의 자리가 어떤 숫자인지 알 수 있다.
        userInputArray[1] = number / 10 % 10;
        // 배열의 2번째 자리에는 세자리 수를 10으로 나눈 나머지를 담는다. => 1의 자리가 어떤 숫자인지 알 수 있다.
        userInputArray[2] = number % 10;

        // 스트라익과 볼의 개수를 담을 변수를 선언하고 0으로 초기화한다.
        int strikeCount = 0;
        int ballCount = 0;

        // 사용자가 입력한 세자리의 숫자를 각 1자리마다 돌면서 판단을 실행한다.
        for (int i = 0; i < 3; i++) {
            // 컴퓨터가 사용자가 입력한 각 숫자가 정답인 숫자와 맞는지 각자리를 한번씩 반복하여 돌며 판단한다.
            for (int j = 0; j < 3; j++) {
                // 사용자가 입력한 숫자와 컴퓨터가 입력한 숫자가 맞는지 맞춰보는 분기문
                if (userInputArray[i] == computerExamArray[j]) {
                    // 자릿수까지 맞는다면
                    if (i == j) {
                        // 스트라이크를 1 더한다.
                        strikeCount++;
                    }
                    // 자릿수는 틀리지만 해당되는 숫자가 있으면 볼을 1 더한다.
                    else {
                        ballCount++;
                    }
                }
            }
        }

        // 최종적으로 확정된 스트라이크 카운트를 저장하는 변수 선언
        final int strikeFinalCount = strikeCount;
        // 최종적으로 확정된 볼 카운트를 저장하는 변수 선언
        final int ballFinalCount = ballCount;

        // 스트라이크가 3 이면
        if (strikeCount == 3) {
            // 채팅 리스트에 항목을 더한다.(채팅리스트 중 사용자가 아니라 컴퓨터의 채팅 목록에 더함. 사용자가 3스트라이크를 달성할 때 까지의 버튼 클릭 횟수를 보여준다.)
            chatList.add(new Chat(false, String.format("정답입니다! 답변 횟수는 %s회 입니다.", userAnswerCountTxt)));
            // 챗어댑터를 새로고침 한다.
            mChatAdapter.notifyDataSetChanged();
            // 채팅이 누적되면 리스트가 밀려올라갈 수 있도록 하는 조치
            // 리스트뷰가 원하는 위치로 스크롤 될 수 있도록 하는 메소드를 붙여준다.(메소드의 내용물은 전체 채팅 목록의 숫자에서 1을 뺀 값을 넣는다.)
            act.messageListView.smoothScrollToPosition(chatList.size() - 1); // 10개가 쌓였으면 -1만큼 가라
        }
        // 스트라이크가 3이 아닐 경우
        else {
            // 강제로 실행시간 시간지연시킨다. 이 코드는 백그라운드 쓰레드와 ui 쓰레드를 함께 구현하기 위해 Runnable 메소드를 사용한다. (사용자 경험을 위해서)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 채팅 리스트에 항목을 더한다. (컴퓨터 채팅 목록에 더함. "S '스트라이크 수', B '볼의 수'" 형식으로 보여준다.)
                    chatList.add(new Chat(false, String.format("%dS, %dB 입니다.", strikeFinalCount, ballFinalCount)));
                    // 챗어댑터를 새로고침 한다.
                    mChatAdapter.notifyDataSetChanged();
                    // 채팅이 누적되면 리스트가 밀려올라갈 수 있도록 하는 조치
                    // 리스트뷰가 원하는 위치로 스크롤 될 수 있도록 하는 메소드를 붙여준다.(메소드의 내용물은 전체 채팅 목록의 숫자에서 1을 뺀 값을 넣는다.)
                    act.messageListView.smoothScrollToPosition(chatList.size() - 1); // 10개가 쌓였으면 -1만큼 가라
                }
            }, 500); // 딜레이 시간은 0.5초로 설정

        }

    }

    // 선언한 메소드들에 값을 대입하는 메소드
    @Override
    public void setValues() {
        // makeExam() 메소드를 실행
        makeExam();

        // 챗어댑터를 생성하여 멤버변수로 선언한다.
        mChatAdapter = new ChatAdapter(mContext, chatList);
        // 메세지리스트뷰에 어댑터를 붙여서 화면에 보여준다.
        act.messageListView.setAdapter(mChatAdapter);

    }

    // 문제를 제출하는 메소드
    void makeExam() {
        // 값이 참인 경우 지속적으로 반복하는 무한 반복문
        while (true) {
            // 0초과 1이하 의 난수를 발생시키는 자바 메소드를 호출하여 값을 생성하고 이 수를 세자리 수로 만들기 위해서 899를 곱하고 여기에 100을 더한다. => 0 ~ 999
            int randomNumber = (int) (Math.random() * 899 + 100); // Ex. 747
            // 임시로 값을 저장할, 인자가 3개인 배열을 선언
            int[] tempNumber = new int[3];

            // 배열의 0번째 자리에 생성한 난수를 100으로 나누어 저장 => 100의 자리 저장
            tempNumber[0] = randomNumber / 100;
            // 배열의 1번째 자리에 생성한 난수를 10으로 나누고 다시 10으로 나눠서 나머지를 저장 => 10의 자리 저장
            tempNumber[1] = randomNumber / 10 % 10;
            // 배열의 2번째 자리에 생성한 난수를 10으로 나눈 나머지를 저장 => 1의 자리 저장
            tempNumber[2] = randomNumber % 10;

            // 자리별 숫자가 중복되지 않기 위해서 추가하는 로직. bool 변수를 선언.
            boolean isDuplOk = true;
            // 만약 중복되는 숫자가 하나라도 있다면 false
            if (tempNumber[0] == tempNumber[1] || tempNumber[1] == tempNumber[2] || tempNumber[0] == tempNumber[2]) {
                isDuplOk = false;
            }

            // 숫자에 0이 들어있지 않도록 걸러내는 로직. bool 변수를 선언
            boolean isZeroOk = true;
            // 만약 0 이 1개라도 있다면 false
            if (tempNumber[0] == 0 || tempNumber[1] == 0 || tempNumber[2] == 0) {
                isZeroOk = false;
            }

            // 숫자 중복이 없고 (and) 숫자에 0이 들어있지 않다면 컴퓨터가 가질 배열값에 위 조건을 통과하여 저장한 임시값을 대입한다.
            if (isDuplOk && isZeroOk) {
                computerExamArray[0] = tempNumber[0];
                computerExamArray[1] = tempNumber[1];
                computerExamArray[2] = tempNumber[2];

                // 정답 숫자를 확인해보기 위한 로그
                Log.d("정답 숫자", randomNumber + " 입니다.");

                // 모든 조건을 만족하여 프로세스가 끝나면 반복문을 빠져나간다.
                break;
            }

        }


    }


    @Override
    public void bindViews() {

        act = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }
}







