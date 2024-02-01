package org.example.parent.service;

import org.example.parent.dto.ParentDto;
import org.example.parent.entity.Parent;

import org.example.parent_medic.entity.ParentMedicFacility;
import org.example.parent.entity.ParentParam;
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
    ServiceData<ParentMedicFacility> connect(ParentParam param, String uniqueId);
}
