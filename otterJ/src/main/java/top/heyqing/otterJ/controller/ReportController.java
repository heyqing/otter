package top.heyqing.otterJ.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.heyqing.otterJ.common.R;

@Tag(name = "测试报告管理", description = "查询和管理测试报告")
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Operation(summary = "查询测试报告详情", description = "根据ID查询测试报告详细信息")
    @GetMapping("/{id}")
    public R<?> getReportById(@PathVariable Long id) {
        // TODO: 查询报告详情
        return null;
    }

    @Operation(summary = "分页查询测试报告", description = "分页查询所有测试报告")
    @GetMapping("")
    public R<?> listReports(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        // TODO: 分页查询
        return null;
    }
} 