package com.plugin.builder.factory;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.PsiField;

/**
 * Factory class to create builder method names.
 *
 * @author Antal_Kajzer
 */
public class MethodNameFactory {

    @NotNull
    public String create(PsiField field) {
        return field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1, field.getName().length());
    }
}
