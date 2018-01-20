package com.tulip.mongo_file_demo.service;
import com.tulip.mongo_file_demo.domain.File;
import com.tulip.mongo_file_demo.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;



@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileRepository fileRepository;

    @Override
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileRepository.delete(id);
    }

    @Override
    public File getFileById(String id) {
        return fileRepository.findOne(id);
    }

    @Override
    public List<File> listFileByPage(int pageIndex, int pageSize) {

        Page<File> page = null;
        List<File> list = null;

        Sort sort = new Sort(Direction.DESC,"uploadDate");
        Pageable pageable = new PageRequest(pageIndex,pageSize,sort);

        page = fileRepository.findAll(pageable);
        list = page.getContent();

        return list;
    }
}
