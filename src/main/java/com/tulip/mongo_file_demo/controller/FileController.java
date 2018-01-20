package com.tulip.mongo_file_demo.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.tulip.mongo_file_demo.domain.File;
import com.tulip.mongo_file_demo.service.FileService;
import com.tulip.mongo_file_demo.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.spi.http.HttpHandler;
@Slf4j
@Controller
@CrossOrigin(origins = "*",maxAge = 3600)
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "/")
    public String index(Model model){
        //show newest 20 data
        model.addAttribute("files",fileService.listFileByPage(0,20));
        return "index";
    }

    @GetMapping("files/{pageIndex}/{pageSize}")
    @ResponseBody
    public List<File> listFilesByPage(@PathVariable("pageIndex") int pageIndex,@PathVariable("pageSize") int pageSize){
        return fileService.listFileByPage(pageIndex,pageSize);
    }


    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Object> severFile(@PathVariable("id") String id){
        log.info("severFile");
        File file = fileService.getFileById(id);

        if(file != null){
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; fileName=\""+file.getName()+"\"")
                    .header(HttpHeaders.CONTENT_TYPE,"application/octet-stream")
                    .header("Connection","close")
                    .body( file.getConent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not found");
        }
    }

    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileonline(@PathVariable("id") String id){
        File file = fileService.getFileById(id);

        if(file != null){
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,"fileName=\""+file.getName()+"\"")
                    .header(HttpHeaders.CONTENT_TYPE,file.getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH,file.getSize()+"")
                    .header("Connection","close")
                    .body(file.getConent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not found");
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object>  handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes){
        File returnFile = null;
        try{
            File f = new File(file.getOriginalFilename(),file.getContentType(),file.getSize(),file.getBytes());
            f.setUploadDate(new Date());
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            returnFile = fileService.saveFile(f);
            String path = "//" + serverAddress + ":" + serverPort + "/view/" + returnFile.getId();
            return ResponseEntity.status(HttpStatus.OK).body(path);

        }catch (IOException|NoSuchAlgorithmException ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@PathVariable("id") String id){

        try{
            fileService.removeFile(id);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE Success!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
