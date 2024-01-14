package org.example.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.constant.GatewayConstant;
import org.springframework.http.HttpStatusCode;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder(access = AccessLevel.PRIVATE)
public class HttpResponse {

    private HttpMessage message;

    private Object data;

    private Meta metadata;

    private Pagination pagination;

    @Builder
    static class HttpMessage{
        private String title;
        private String message;
    }

    @Builder
    static class Meta{
        private String path;
        private LocalDateTime timestamp;
        private Long status;
        private String timeElapsed;
        private Object data;
    }

    @Builder
    public static class Pagination{
        private Long currentPage;
        private Long currentElements;
        private Long totalPage;
        private Long totalElements;
    }

    public static HttpResponse sendSuccessResponse(
            Context context,
            HttpStatusCode statusCode,
            String message,
            Object data,
            Pagination pagination,
            Object metadata
    ){
        var initialTime = context.<LocalDateTime>get(GatewayConstant.TIME_START);
        var now = LocalDateTime.now();

        var differenceMinute = ChronoUnit.MINUTES.between(now, initialTime);
        var differenceSecond = differenceMinute % 60;
        return HttpResponse
                .builder()
                .message(
                        HttpMessage
                                .builder()
                                .title("Request berhasil")
                                .message(message)
                                .build()
                )
                .data(data)
                .metadata(
                        Meta
                                .builder()
                                .path(
                                        context.get(GatewayConstant.REQUEST_PATH)
                                )
                                .data(metadata)
                                .status((long)statusCode.value())
                                .timestamp(LocalDateTime.now())
                                .timeElapsed(String.format("%dm %ds", differenceMinute, differenceSecond))
                                .build()
                )
                .pagination(pagination)
                .build();
    }

    public static HttpResponse sendErrorResponse(
            Context context,
            HttpStatusCode statusCode,
            String message
    ){
        var initialTime = context.<LocalDateTime>get(GatewayConstant.TIME_START);
        var now = LocalDateTime.now();

        var differenceMinute = ChronoUnit.MINUTES.between(now, initialTime);
        var differenceSecond = differenceMinute % 60;
        return HttpResponse
                .builder()
                .message(
                        HttpMessage
                                .builder()
                                .title("Terjadi kesalahan pada saat mengolah request")
                                .message(message)
                                .build()
                )
                .metadata(
                        Meta
                                .builder()
                                .path(
                                        context.get(GatewayConstant.REQUEST_PATH)
                                )
                                .status((long)statusCode.value())
                                .timestamp(LocalDateTime.now())
                                .timeElapsed(String.format("%dm %ds", differenceMinute, differenceSecond))
                                .build()
                )
                .build();
    }
}
