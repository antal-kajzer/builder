package com.plugin.builder;

import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;

/**
 * Provider to provide class' final fields.
 *
 * @author Antal_Kajzer
 */
public class FinalFieldProvider {

    /**
     * Provides clazz' final fields.
     * @param clazz clazz which field want to provide
     * @return provided fields
     */
    public List<PsiField> provide(final PsiClass clazz) {
        final List<PsiField> fields = new ArrayList<>();
        for (final PsiField field : clazz.getAllFields()) {
            if (field.getModifierList().hasExplicitModifier(PsiModifier.FINAL)) {
                fields.add(field);
            }
        }
        return fields;
    }
}
