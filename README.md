<H1>Together-Helpee</H1>
<H3>장애인과 노약자 분들을 위한 자원봉사 매칭 서비스 TOGETHER </H3>
<p>도움을 받으시는 어르신들을 위한 앱입니다.<br>
This is an application for the elderly who is helped.</p>
<p><a href="http://lim-bo.com/">메인 소개 사이트로 가시려면 클릭해주세요.</a></p>
<p align="center" width=500 height=500> <img src="https://github.com/jun-m-park/Elderly/blob/master/logo.gif"/> </p>
<p>도움을 주는 봉사자 앱으로 가시려면 <a href="https://github.com/Kwangpyo/Together_Helper">https://github.com/Kwangpyo/Together_Helper</a></p>
<p>봉사정보를 관리하는 웹 페이지로 가시려면 <a href="https://github.com/kobowon/Together-Admin">https://github.com/kobowon/Together-Admin</a></p>

<H2>1. 내용설명</H2>
<H2>얼굴인식을 통한 자동로그인과 음성인식을 통한 도움요청</H2>
  <H3>얼굴인식</H3>
    <p>Google Vision API를 통한 얼굴 인식</p><br>
      <p>얼굴이 인식되면 별다른 손동작 없이 로그인 가능</p>
  <H3>음성인식</H3>
    <p>Google Speech API를 통한 음성 인식</p><br>
      <p>마이크를 통해 도움을 자연어로 요청</p><br>
        <p>자체 알고리즘(TF-IDF기반 클러스터링 이용)을 통해 요청 내용 파악 후 DB 전송.</p>
<H2>FCM을 통한 푸시알림전송</H2>
  <H3>FCM</H3>
    <p>구글 firebase서버를 통한 FCM구성</p><br>
      <p>도움요청시 자동으로 반경 1KM 자원봉사자 앱에 푸시알림 전송</p><br>
        <p>긴급요청 필요시 푸시알림을 통해 즉시 수락 가능</p>
<H2>2. Flow</H2>
<H3>어플리케이션 메뉴얼</H3>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/flow.png"></img>
<H3>Use Case</H3>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app1.png"></img>
<p>이 화면은 메인화면이며 마이크 버튼을 누르고 도움 받을 날짜를 말할 수 있다. 마이크 버튼을 누르고 말한 내용 중 날짜가 존재한다면 아래에 다른 기능들이 활성화되며, 그 전에는 마이크 버튼만 누를 수 있도록 되어있다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app2.png"></img>
<p>이 화면은 마이크를 누르고 날짜를 말한 이후의 화면이다. 말한 내용은 “오늘”이라고 말하여 당일 날짜가 출력되었으며 이 날짜가 맞다면 “네” 버튼을 누를 수 있다. “아니요” 버튼을 누르면 음성입력을 다시 할 수 있으며 “네” 버튼을 누르면 다음 화면으로 넘어간다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app3.png"></img>
<p>이 화면은 날짜를 입력한 이후의 화면이다. 날짜 입력과 마찬가지의 방식으로 이루어지며 시간을 입력하도록 하였다. 시간을 “오후 11시 반”이라고 말한 화면이다. “뒤로” 버튼을 누르면 다시 날짜를 입력할 수 있고 “네” 버튼을 누르면 다음화면으로 넘어간다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app4.png"></img>
<p>이 화면은 시간을 입력한 이후의 화면이다. 시간 입력과 마찬가지의 방식으로 이루어지며 목적을 입력하도록 하였다. 목적을 “제주도까지 데려다 주세요”라고 말한 화면이다. “뒤로” 버튼을 누르면 다시 시간을 입력할 수 있고 “네” 버튼을 누르면 다음화면으로 넘어간다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app5.png"></img>
<p>이 화면은 도움 받을 내용을 모두 입력한 다음 화면이다. 요청사항으로 말한 내용이 화면에 출력되고 이 내용은 모두 디비에 저장된 상태이다. 이를 도와주겠다고 자원하는 봉사자가 나타나기 전까지 이 화면은 유지되며 “요청 취소” 버튼을 통해 현재 요청한 도움을 취소하고 새로운 도움을 요청할 수 있다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app6.png"></img>
<p>이 화면은 봉사자 어플리케이션에서 신청한 직후 화면이다. 사진처럼 푸시 알림 형태로 알림이 뜨고 “확인” 버튼을 누르면 어플리케이션이 시작되고 상대방을 확인할 수 있다. “취소” 버튼을 누르면 단순히 푸시알림 메세지를 끄고 핸드폰으로 하던 작업을 이어갈 수 있다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app7.png"></img>
<p>이 화면은 봉사시간 1시간 전 화면이다. 봉사가 시작되기 1시간 전부터 상대방의 위치를 사진과 같이 볼 수 있으며 상대방을 만났다면 하단의 “정소영학생을 만났다면 눌러주세요” 버튼을 누르면 된다. 학생 이름은 자동으로 출력되며 봉사를 미리 시작하고 싶은 경우에도 눌러도 무방하다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app8.png"></img>
<p>이 화면은 봉사자가 출발하여 봉사자 어플리케이션에서 “출발” 버튼을 누른 직후 화면이다. 봉사자가 출발을 했는지 안했는지 여부를 알 수 있도록 푸시 알림을 받는 형태로 알아볼 수 있다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app9.png"></img>
<p>이 화면은 봉사 시작 1시간 전 화면에서 “학생을 만났다면 눌러주세요” 버튼을 누르고 난 다음 화면이다. 이 화면에서 봉사를 시작할 수 있으며 버튼을 누른 순간부터 봉사자의 위치나, 시간이 기록되어 증빙 자료로 사용된다. “시작하기” 버튼을 누르면 봉사 중인 상태로 간주하고 어떠한 기능도 시행되지 않으며 봉사자 어플리케이션에서 “종료하기” 버튼을 눌러야만 봉사가 종료된다.</p>
<img src="https://github.com/jun-m-park/Together-Helpee/blob/master/mockup/app10.png"></img>
<p>이 화면은 봉사자 어플리케이션에서 “봉사 종료” 버튼을 누른 직후의 화면이다. 어르신용 어플리케이션에 푸시알림이 뜨고 “확인” 버튼을 누르면 어플리케이션이 실행된다. 봉사가 종료되었음으로 봉사 시작 후부터의 시간이 기록되어 봉사자에게 인증시간을 부여하여 준다.</p>

<H2>Stack</H2>
<li>Android</li>
<li>Slack + Git + Trello</li>
<li>외부 API : Google Vision Api + Google Speech Api + FCM</li>
<li>MySQL</li>
<H2>협력</H2>
<H3>Back-end</H3>
<p>Back-End는 Express Framework 상의 Node.js, Mysql 로 구성된다.  Nginx로 Web 서버를 구성하고 Node.js로 Was 서버를 구성하였다. Nginx proxy를 사용하여 Web 서버로 들어온 요청은 모두 Was 서버에서 처리하도록 구현했다.</p>
<H3>App:Together-Helper</H3>
<p>학생들이 사용하는 Helper 어플리케이션은 안드로이드를 통해 구성되고 학생들은 해당 어플리케이션을 통해 봉사신청, 봉사예약, 봉사매칭, 노인과 실제로 매칭, 봉사 종료, 피드백 등의 기능을 수행할 수 있다. 봉사신청 시, Google map API를 사용하여 실제 지도 상에 등록된 봉사를 신청할 수 있고, 노인이 사용하는 Helpee APP과 마찬가지로 봉사 시작 전에 노인과 실제로 만나기 위해, 서버 상에 현재 위치를 주기적으로 전송함으로써 지도 상에서 서로의 위치를 확인할 수 있다. 봉사를 마친 후에는 텍스트 형식으로 서버에 데이터를 전송한다.</p>
<H3>Web:관리자페이지</H3>
<p>관리자가 사용하는 Web 어플리케이션은 HTML, CSS, JAVASCRIPT, EJS를 통해 구성되고 관리자는 봉사 승인, 봉사 현황 분석, 사용자 관리, 봉사 관리 등의 봉사의 전반적인 부분을 웹을 통해 관리한다. 특히 실질적으로 노인과 학생이 봉사를 했는 지를 판단해야 하기 때문에 봉사 중일 때의 학생과 노인의 위치를 서버에서 가져오고 이를 Google Maps API를 통해 지도상에 나타낸다. 그리고 봉사 현황을 분석하기 위해 데이터 베이스 상에서 여러 종류의 데이터를 가져온 후 종합하여 이를 차트 상에 보기 좋게 표현하였고, 이를 통해 Together 봉사 서비스의 수요와 공급을 알 수 있도록 구현하였다.</p>
