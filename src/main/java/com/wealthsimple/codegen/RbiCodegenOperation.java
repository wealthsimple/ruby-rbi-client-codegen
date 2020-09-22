package com.wealthsimple.codegen;

import com.wealthsimple.codegen.RbiCodegenResponse;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenResponse;

public class RbiCodegenOperation extends CodegenOperation {
    public String rbiReturnType() {
        for (CodegenResponse response: responses) {
            if (response.isDefault) {
                return ((RbiCodegenResponse) response).rbiDataType();
            }
        }
        return null;
    }
}
