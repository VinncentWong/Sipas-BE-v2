package org.parent_medic.pm.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dubbo.springboot.*;
import org.parent_medic.pm.repository.IRepository;
import org.example.response.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@DubboService
@Slf4j
public class ParentMedicService
    extends DubboParentMedicTriple.ParentMedicImplBase {

    @Autowired
    private IRepository repository;

    @Override
    public ParentMedicFacility save(ParentMedicFacility request) {
        var parentMedicReq = org.parent_medic.pm.entity.ParentMedicFacility
                .builder()
                .fkMedicId(request.getFkMedicId())
                .fkParentId(request.getFkParentId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("accept parentMedicReq: {}", parentMedicReq);

        var repoData = this.repository
                .save(parentMedicReq)
                .block();

        if(repoData != null){
            var data = repoData.getData();

            var respBuilder =  ParentMedicFacility
                    .newBuilder()
                    .setId(data.getId())
                    .setFkMedicId(data.getFkMedicId())
                    .setFkParentId(data.getFkParentId());

            var createdAt = data.getCreatedAt();
            var updatedAt = data.getUpdatedAt();

            if(createdAt != null){
                var instantCreatedAt = createdAt.toInstant(ZoneOffset.UTC);
                respBuilder
                        .setCreatedAt(
                                Timestamp
                                        .newBuilder()
                                        .setSeconds(instantCreatedAt.getEpochSecond())
                                        .setNanos(instantCreatedAt.getNano())
                                        .build()
                        );
            }

            if(updatedAt != null){
                var instantUpdatedAt = updatedAt.toInstant(ZoneOffset.UTC);
                respBuilder
                        .setUpdatedAt(
                                Timestamp
                                        .newBuilder()
                                        .setSeconds(instantUpdatedAt.getEpochSecond())
                                        .setNanos(instantUpdatedAt.getNano())
                                        .build()
                        );
            }

            log.info("returning data: {}", respBuilder.build());

            return respBuilder.build();
        }

        return null;
    }

    @Override
    public ParentMedicFacility get(ParentMedicFacilityParam request) {
        var reqParam = org.parent_medic.pm.entity.ParentMedicFacilityParam
                .builder()
                .id(request.getId())
                .pgParam(
                        HttpResponse.PaginationParam
                                .builder()
                                .limit((int)request.getPgParam().getLimit())
                                .offset((int)request.getPgParam().getOffset())
                                .build()
                )
                .fkParentId(request.getFkParentId())
                .fkMedicId(request.getFkMedicId())
                .fkParentIds(request.getFkParentIdsList())
                .fkMedicIds(request.getFkMedicIdsList())
                .build();

        log.info("get reqParam: {}", reqParam);

        var repoData = this.repository
                .get(reqParam)
                .block();

        if(repoData != null){
            var data = repoData.getData();

            var resp = convertEntityIntoPbMessage(data);

            log.info("returning data: {}", resp);

            return resp;
        }

        return null;
    }

    @Override
    public ListParentMedicFacility getList(ParentMedicFacilityParam request) {
        var reqParam = org.parent_medic.pm.entity.ParentMedicFacilityParam
                .builder()
                .id(request.getId())
                .pgParam(
                        HttpResponse.PaginationParam
                                .builder()
                                .limit((int)request.getPgParam().getLimit())
                                .offset((int)request.getPgParam().getOffset())
                                .build()
                )
                .fkParentId(request.getFkParentId())
                .fkMedicId(request.getFkMedicId())
                .fkParentIds(request.getFkParentIdsList())
                .fkMedicIds(request.getFkMedicIdsList())
                .build();

        log.info("getList reqParam: {}", reqParam);

        var repoData = this.repository
                .getList(reqParam)
                .block();

        if(repoData != null){
            var respData = repoData.getData()
                    .stream()
                    .map(this::convertEntityIntoPbMessage)
                    .toList();

            log.info("returning data: {}", respData);

            var pg = repoData.getPg();

            return ListParentMedicFacility
                    .newBuilder()
                    .addAllParentMedicFacilities(respData)
                    .setPg(
                            Pagination
                                    .newBuilder()
                                    .setCurrentPage(pg.getCurrentPage())
                                    .setCurrentElement(pg.getCurrentElements())
                                    .setTotalPage(pg.getTotalPage())
                                    .setTotalElement(pg.getTotalElements())
                                    .build()
                    )
                    .build();
        }

        return null;
    }

    private ParentMedicFacility convertEntityIntoPbMessage(org.parent_medic.pm.entity.ParentMedicFacility data){
        var respBuilder =  ParentMedicFacility
                .newBuilder()
                .setId(data.getId())
                .setFkParentId(data.getFkParentId())
                .setFkMedicId(data.getFkMedicId());

        var createdAt = data.getCreatedAt();
        var updatedAt = data.getUpdatedAt();
        var deletedAt = data.getDeletedAt();

        if(createdAt != null){
            var instantCreatedAt = createdAt.toInstant(ZoneOffset.UTC);
            respBuilder
                    .setCreatedAt(
                            Timestamp
                                    .newBuilder()
                                    .setSeconds(instantCreatedAt.getEpochSecond())
                                    .setNanos(instantCreatedAt.getNano())
                                    .build()
                    );
        }

        if(updatedAt != null){
            var instantUpdatedAt = updatedAt.toInstant(ZoneOffset.UTC);
            respBuilder
                    .setUpdatedAt(
                            Timestamp
                                    .newBuilder()
                                    .setSeconds(instantUpdatedAt.getEpochSecond())
                                    .setNanos(instantUpdatedAt.getNano())
                                    .build()
                    );
        }

        if(deletedAt != null){
            var instantDeletedAt = deletedAt.toInstant(ZoneOffset.UTC);
            respBuilder
                    .setDeletedAt(
                            Timestamp
                                    .newBuilder()
                                    .setSeconds(instantDeletedAt.getEpochSecond())
                                    .setNanos(instantDeletedAt.getNano())
                                    .build()
                    );
        }

        return respBuilder.build();
    }
}
