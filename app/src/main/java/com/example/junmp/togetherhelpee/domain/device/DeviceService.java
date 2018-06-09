package com.example.junmp.togetherhelpee.domain.device;

import com.example.junmp.togetherhelpee.common.util.network.RetrofitBuilder;

import java.io.IOException;

public class DeviceService {
    private DeviceRepository deviceRepository =  RetrofitBuilder.builder().create(DeviceRepository.class);

    public void save(String deviceId , String pushToken , String phoneNumber) {
        try {

            deviceRepository.save(deviceId , pushToken , phoneNumber).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
