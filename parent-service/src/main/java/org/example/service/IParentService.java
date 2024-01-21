package org.example.service;

import org.example.dto.ParentDto;
import org.example.entity.Parent;
import org.example.entity.ParentParam;
import org.example.response.HttpResponse;
import org.example.response.ServiceData;

import java.util.List;

public interface IParentService {
    ServiceData<Parent> save(ParentDto.Create dto);
    ServiceData<Parent> login(ParentDto.Login dto);
    ServiceData<Parent> get(ParentParam param);
    ServiceData<List<Parent>> getList(ParentParam param);
    ServiceData<Parent> update(ParentParam param, Parent parent);
    ServiceData<Parent> delete(ParentParam param);
    ServiceData<Parent> activate(ParentParam param);
}
