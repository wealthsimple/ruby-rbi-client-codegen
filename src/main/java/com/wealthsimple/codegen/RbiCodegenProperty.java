package com.wealthsimple.codegen;

import io.swagger.codegen.CodegenProperty;

public class RbiCodegenProperty extends CodegenProperty {
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
        } else if (isListContainer) {
            return "T::Array[" + ((RbiCodegenProperty)items).rbiDataType() + "]";
        } else if (isMapContainer) {
            return "T::Hash[String, " + ((RbiCodegenProperty)items).rbiDataType() + "]";
        } else {
            StringBuilder sb = new StringBuilder("");
            if (!isPrimitiveType) {
                sb.append(internalClassPrefix + "::");
            }
            if (datatype.equals("Object")) {
                sb.append("BasicObject");
            } else {
                sb.append(datatype);
            }

            return sb.toString();
        }
    }

    @Override
    public CodegenProperty clone() {
        RbiCodegenProperty cp = (RbiCodegenProperty) super.clone();
        cp.internalClassPrefix = this.internalClassPrefix;

        return cp;
    }
}
