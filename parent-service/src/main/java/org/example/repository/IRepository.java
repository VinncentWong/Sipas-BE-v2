package org.example.repository;

import org.example.entity.Parent;
import org.example.entity.ParentParam;
import org.example.response.RepositoryData;

import java.util.List;

public interface IRepository {
    RepositoryData<Parent> get(ParentParam param);
    RepositoryData<List<Parent>> getList(ParentParam param);
    RepositoryData<Parent> update(ParentParam param, Parent parent);
}
