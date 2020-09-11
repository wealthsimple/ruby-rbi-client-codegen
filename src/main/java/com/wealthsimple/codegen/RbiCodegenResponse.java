package com.wealthsimple.codegen;

import com.wealthsimple.codegen.RbiCodegenProperty;
import io.swagger.codegen.CodegenResponse;

public class RbiCodegenResponse extends CodegenResponse {
    public RbiCodegenProperty responseProperty;

    public String rbiDataType() {
        if (responseProperty != null) {
            return responseProperty.rbiDataType();
        } else {
            return null;
        }
    }
}
