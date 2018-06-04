package com.example.junmp.togetherhelpee.domain.user;

public class UserService {

    /**
     * 회원 가입 여부를 서버에서 조회한 이후 boolean 값을 리턴한다.
     * @return true , false
     */
    public boolean isRegistered() {
        // TODO 박준민 구현할것.
        return true;
    }

    public boolean isNotRegistered() {
        return isRegistered() == false;
    }

    /**
     * 로그인 여부를  서버에서 조회한 이후 boolean 값을 리턴한다.
     * @return
     */
    public boolean isLogged() {
        //TODO 박준민 구현할것
        /**
         * 조건 1. 가입 여부를 체크한다 ( 가입하지 않았다면 false )
         * 조건 2. 가입 했고 Login 했다면 true 아니라면 false
         *
         */
        return false;
    }

    /**
     * 로그인 한 유저 정보를 가져올것
     * @return
     */
    public User getLoggedUser(String deviceId) {
        return new User();
    }
}
