package com.tulip.mongo_file_demo.service;


import com.tulip.mongo_file_demo.domain.File;

import java.util.List;

public interface FileService {

    File saveFile(File file);

    void removeFile(String id);

    File getFileById(String id);

    List<File> listFileByPage(int pageIndex,int pageSize);

}
