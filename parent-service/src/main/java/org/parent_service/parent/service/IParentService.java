package org.parent_service.parent.service;

import org.parent_service.parent.dto.ParentDto;
import org.parent_service.parent.entity.Parent;
import org.parent_service.parent.entity.ParentParam;
import org.parent_medic.pm.entity.ParentMedicFacility;
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
