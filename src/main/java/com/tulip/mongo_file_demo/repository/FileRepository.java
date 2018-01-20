package com.tulip.mongo_file_demo.repository;

import com.tulip.mongo_file_demo.domain.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File,String>{
}
