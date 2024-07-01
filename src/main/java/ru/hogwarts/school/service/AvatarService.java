package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.util.List;
import java.awt.*;
import java.io.IOException;

public interface AvatarService {

    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    Avatar getAvatarByStudent(Long id);

    List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize);
}