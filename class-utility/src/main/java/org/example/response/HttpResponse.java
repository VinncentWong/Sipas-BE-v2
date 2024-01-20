package org.example.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ContextConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder(access = AccessLevel.PRIVATE)
@Setter
@Getter
@Slf4j
public class HttpResponse {

    private HttpMessage metadata;

    private Object data;

    private Pagination pagination;

    @Builder
    @Setter
    @Getter
    static class HttpMessage{
        private String title;
        private String message;
        private Meta meta;
    }

    @Builder
    @Setter
    @Getter
    static class Meta{
        private String path;
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private LocalDateTime timestamp;
        private Long status;
        private String timeElapsed;
        private Object data;
    }

    @Builder
    @Setter
    @Getter
    public static class Pagination{
        private Long currentPage;
        private Long currentElements;
        private Long totalPage;
        private Long totalElements;
    }

    public static HttpResponse sendSuccessResponse(
            Context context,
            HttpStatus statusCode,
            String message,
            Object data,
            Pagination pagination,
            Object metadata
    ){
        var initialTime = context.<LocalDateTime>get(ContextConstant.TIME_START);
        var now = LocalDateTime.now();

        log.info("initialTime: {}, now: {}", initialTime, now);

        var differenceMinute = ChronoUnit.MINUTES.between(now, initialTime);
        var differenceSecond = differenceMinute != 0 ? differenceMinute % 60 : ChronoUnit.SECONDS.between(now, initialTime);

        log.info("differenceMinute: {}, differenceSecond: {}", differenceMinute, differenceSecond);

        return HttpResponse
                .builder()
                .metadata(
                        HttpMessage
                                .builder()
                                .title("Request berhasil")
                                .message(message)
                                .meta(
                                        Meta
                                                .builder()
                                                .path(
                                                        context.get(ContextConstant.REQUEST_PATH)
                                                )
                                                .data(metadata)
                                                .status((long)statusCode.value())
                                                .timestamp(LocalDateTime.now())
                                                .timeElapsed(String.format("%dm %ds", differenceMinute, differenceSecond))
                                                .build()
                                )
                                .build()
                )
                .data(data)
                .pagination(pagination)
                .build();
    }

    public static HttpResponse sendErrorResponse(
            String message
    ){
        return HttpResponse
                .builder()
                .metadata(
                        HttpMessage
                                .builder()
                                .title("Terjadi kesalahan pada saat mengolah request")
                                .message(message)
                                .build()
                )
                .build();
    }
}
