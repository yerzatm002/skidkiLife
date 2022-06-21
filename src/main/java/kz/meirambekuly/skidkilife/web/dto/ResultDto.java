package kz.meirambekuly.skidkilife.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel(value = "ResultDTO", description = "Dto for all responses")
public class ResultDto<T> {
//    @ApiModelProperty(value = "Http Status Id", required = true)
    @Builder.Default
    private int HttpStatus = 200;
//    @ApiModelProperty(value = "Is request success", required = true)
    private boolean isSuccess;
//    @ApiModelProperty(value = "Message if error occurs")
    private String errorMessage;
//    @ApiModelProperty(value = "Responding data")
    private T data;
}
