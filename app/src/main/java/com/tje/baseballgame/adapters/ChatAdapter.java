package com.tje.baseballgame.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tje.baseballgame.R;
import com.tje.baseballgame.datas.Chat;

import java.util.List;

// 챗 아이템을 인자로 갖는 배열어댑터를 상속받은 챗어댑터
public class ChatAdapter extends ArrayAdapter<Chat> {

    // 사용위치 멤버변수
    Context mContext;
    // 챗아이템을 인자로 갖는 리스트 멤버변수
    List<Chat> mList;
    // xml 을 뷰로 만들어줄 인플레이터 변수
    LayoutInflater inf;

    // 챗어댑터 생성자
    public ChatAdapter(Context context, List<Chat> list) {
        super(context, R.layout.chat_list_item, list);

        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);

    }

    // 어댑터의 데이터를 어떻게 보여줄 지 정의
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 아이템을 담아서 보여줄 뷰(뷰의 배열)를 변수로 선언
        View row = convertView;

        // 컨버트뷰가 비어 있다면 아이템을 채워서 뷰로 만들어라.
        if (row == null) {
            row = inf.inflate(R.layout.chat_list_item, null);
        }

        // 챗아이템 리스트에서 각 위치의 아이템을 챗자료형의 data 변수에 담는다.
        Chat data = mList.get(position);

        // 프레임레이아웃형의 변수에 컨버트뷰의 사용자메세지 프레임레이아웃을 가져와 담는다.
        FrameLayout userMsgFrameLayout = row.findViewById(R.id.userMsgFrameLayout);
        // 프레임레이아웃형의 변수에 컨버트뷰의 컴퓨터메세지 프레임레이아웃을 가져와 담는다.
        FrameLayout computerMsgFrameLayout = row.findViewById(R.id.computerMsgFrameLayout);
        // 텍스트뷰형 변수에 컨버트뷰의 사용자메세지 텍스트뷰 레이아웃을 가져와 담는다.
        TextView userMessageTxt = row.findViewById(R.id.userMessageTxt);
        // 텍스트뷰형 변수에 컨버트뷰의 컴퓨터메세지 텍스트뷰 레이아웃을 가져와 담는다.
        TextView computerMessageTxt = row.findViewById(R.id.computerMessageTxt);

        // 데이터가 사용자가 한 말이라면(데이터모델에서 bool 로 선언했음) 사용자메세지 프레임레이아웃은 보이고, 컴퓨터메세지 크레임레이아웃은 안보이게 해라.
        // 그리고 사용자메세지텍스트부분에 데이터에 담아온 메세지내용를 담는다.
        if (data.userSaid) {
            userMsgFrameLayout.setVisibility(View.VISIBLE);
            computerMsgFrameLayout.setVisibility(View.GONE);

            userMessageTxt.setText(data.message);

        }
        // 아니면 사용자부분을 안 보이게하고, 컴퓨터 부분을 보이게 해라.
        // 그리고 컴퓨터메세지 부분에 입력된 메세지를 담는다.
        else {
            userMsgFrameLayout.setVisibility(View.GONE);
            computerMsgFrameLayout.setVisibility(View.VISIBLE);

            computerMessageTxt.setText(data.message);

        }

        // 작업을 마친 컨버트뷰를 리턴한다.
        return row;
    }
}