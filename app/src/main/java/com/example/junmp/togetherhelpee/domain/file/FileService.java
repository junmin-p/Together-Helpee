package com.example.junmp.togetherhelpee.domain.file;

import android.util.Log;

import com.example.junmp.togetherhelpee.common.util.network.RetrofitBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileService {
    private FileRepository fileRepository = RetrofitBuilder.builder().create(FileRepository.class);

    public String uplaod(File file) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("file"), file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("userfile", file.getName(), reqFile);
        try {
            return fileRepository.uploadImage(uploadFile).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
