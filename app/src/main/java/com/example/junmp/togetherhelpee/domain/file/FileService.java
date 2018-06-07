package com.example.junmp.togetherhelpee.domain.file;

import com.example.junmp.togetherhelpee.common.util.network.RetrofitBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;

public class FileService {
    private FileRepository fileRepository = RetrofitBuilder.builder().create(FileRepository.class);

    public UploadFile upload(File file) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("file"), file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        try {
            return fileRepository.uploadImage(uploadFile).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
