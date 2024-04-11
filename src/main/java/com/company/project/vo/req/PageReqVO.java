package com.company.project.vo.req;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PageReqVO
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class PageReqVO {
    @ExcelIgnore
    @ApiModelProperty(value = "第几页")
    @TableField(exist = false)
    private int page=1;
    @ExcelIgnore
    @ApiModelProperty(value = "分页数量")
    @TableField(exist = false)
    private int limit=10;
}
