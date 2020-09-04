<H1>Together-Helpee
<H3>장애인과 노약자 분들을 위한 자원봉사 매칭 서비스 TOGETHER
<p>도움을 받으시는 어르신들을 위한 앱입니다.
This is an application for the elderly who is helped.</p>
<p><a href="http://lim-bo.com/">메인 소개 사이트로 가시려면 클릭해주세요.</a></p>
<p align="center" width=500 height=500> <img src="https://github.com/jun-m-park/Elderly/blob/master/logo.gif"/> </p>
<p>도움을 주는 봉사자 앱으로 가시려면 <a href="https://github.com/Kwangpyo/Together_Helper">https://github.com/Kwangpyo/Together_Helper</a></p>
<p>봉사정보를 관리하는 웹 페이지로 가시려면 <a href="https://github.com/kobowon/Together-Admin">https://github.com/kobowon/Together-Admin</a></p>

<H2>얼굴인식을 통한 자동로그인과 음성인식을 통한 도움요청
  <H3>얼굴인식
    <p>Google Vision API를 통한 얼굴 인식
      <p>얼굴이 인식되면 별다른 손동작 없이 로그인 가능
  <H3>음성인식
    <p>Google Speech API를 통한 음성 인식
      <p>마이크를 통해 도움을 자연어로 요청
        <p>자체 알고리즘(TF-IDF기반 클러스터링 이용)을 통해 요청 내용 파악 후 DB 전송.
<H2>FCM을 통한 푸시알림전송
  <H3>FCM
    <p>구글 firebase서버를 통한 FCM구성
      <p>도움요청시 자동으로 반경 1KM 자원봉사자 앱에 푸시알림 전송
        <p>긴급요청 필요시 푸시알림을 통해 즉시 수락 가능
  <H3>Together-Helper
    <p>FCM을 통해 푸시알림을 제공받고 수락 및 거절
      <p>본인을 어필할 프로필 구성
        <p>자신의 자원봉사 이력 포털 사이트에 입력가능
