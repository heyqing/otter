package top.heyqing.otterJ.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一响应对象
 * @param <T> 数据类型
 */
@Schema(description = "统一响应对象")
public class R<T> {
    @Schema(description = "响应码，0=成功，其他=失败")
    private int code;
    @Schema(description = "响应消息")
    private String msg;
    @Schema(description = "响应数据")
    private T data;

    public R() {}
    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static <T> R<T> ok(T data) {
        return new R<>(0, "success", data);
    }
    public static <T> R<T> ok() {
        return new R<>(0, "success", null);
    }
    public static <T> R<T> fail(String msg) {
        return new R<>(-1, msg, null);
    }
    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, msg, null);
    }
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
} 