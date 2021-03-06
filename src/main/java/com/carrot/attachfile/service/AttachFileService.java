package com.carrot.attachfile.service;

import com.carrot.attachfile.component.FileService;
import com.carrot.attachfile.domain.AttachFile;
import com.carrot.attachfile.repository.AttachFileRepository;
import com.carrot.attachfile.exception.AttachFileStorageException;
import com.carrot.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class AttachFileService {

    private static final Logger log = LoggerFactory.getLogger(AttachFileService.class);

    @Autowired
    public AttachFileService(
            @Qualifier("fileSystemFileServiceImpl") FileService fileService,
            AttachFileRepository attachFileRepository
    ) {
        this.fileService = fileService;
        this.attachFileRepository = attachFileRepository;
    }

    private final FileService fileService;

    private final AttachFileRepository attachFileRepository;


    @Value("${attachFileLocation}")
    private String attachFileLocation;

    @Value("${user.home}")
    private String uploadDir;


    public void simpleFileUpload(MultipartFile multipartFile) {
        Path serverPath = Paths.get(
                uploadDir +
                        File.separator +
                        StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        log.info("path : {} \n {}",serverPath.toString(),serverPath.toAbsolutePath());
        try {
            Files.copy(multipartFile.getInputStream(), serverPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("fail to store file : name={}, exception={}",
                      multipartFile.getOriginalFilename(),
                      e.getMessage());
            throw new AttachFileStorageException("fail to store file");
        }
    }

    //@note : ?????? ???????????? ????????? ?????? ????????? >> Transactional ??? DB Conn ??? ?????? ?????? ????????? >> DB Conn pool ??? ????????? ????????? ???????????? ??????
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveAttach(MultipartFile uploadedFile, Article article) {

        String originalName = uploadedFile.getOriginalFilename();
        String validName = "";

        try {
            if (!StringUtils.hasLength(originalName)) {
                validName = fileService.uploadFile(attachFileLocation, originalName, uploadedFile.getBytes());
            }
        } catch (Exception e) {
            throw new AttachFileStorageException("saveAttach : ????????? ??????" + e.getMessage());
        }

        //
        AttachFile attachFileEntity = AttachFile
                .builder()
                .originalFileName(originalName)
                .verifiedFileName(validName)
                .article(article)
                .build();
        attachFileRepository.save(attachFileEntity);
    }


    public void updateAttach(Long attachFileId, MultipartFile itemImgFile) throws Exception {

        if (!itemImgFile.isEmpty()) { // ????????? ????????? ????????? ??????
            AttachFile saved = attachFileRepository
                    .findById(attachFileId)
                    .orElseThrow(EntityNotFoundException::new);

            if (!StringUtils.hasLength(saved.getOriginalFileName())) {
                // ????????? ?????????????????? ????????? ????????????
                fileService.deleteFile(attachFileLocation + "/" + saved.getVerifiedFileName());
            }

            String originalName = itemImgFile.getOriginalFilename();
            String validName = fileService.uploadFile(attachFileLocation, originalName, itemImgFile.getBytes());
            saved.updateAttachFile(originalName, validName);
        }
    }
}
