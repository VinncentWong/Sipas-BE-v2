package org.example.service;

import org.example.dto.ParentDto;
import org.example.entity.Parent;
import org.example.response.HttpResponse;

public interface IParentService {
    Parent save(ParentDto.Create dto);
    HttpResponse login(ParentDto.Login dto);
}
