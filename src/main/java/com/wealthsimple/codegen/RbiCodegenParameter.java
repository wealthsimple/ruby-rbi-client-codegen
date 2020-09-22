package com.wealthsimple.codegen;

import io.swagger.codegen.CodegenParameter;

public class RbiCodegenParameter extends CodegenParameter {
    public String internalClassPrefix;

    public String rbiDataType() {
        if (isString || isBinary || isByteArray || isUuid) {
            return "String";
        } else if (isInteger || isLong) {
            return "Integer";
        } else if (isFloat || isDouble || isNumber) {
            return "Float";
        } else if (isDate) {
            return "Date";
        } else if (isDateTime) {
            return "DateTime";
        } else if (isBoolean) {
            return "T::Boolean";
        } else if (isFile) {
            return "File";
        } else if (isContainer) {
            return ((RbiCodegenProperty)items).rbiDataType();
        } else {
            StringBuilder sb = new StringBuilder("");
            if (!isPrimitiveType) {
                sb.append(internalClassPrefix + "::");
            }
            sb.append(dataType);
            return sb.toString();
        }
    }
}
