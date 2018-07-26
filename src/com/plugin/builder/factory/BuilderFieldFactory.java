package com.plugin.builder.factory;

import java.util.ArrayList;
import java.util.List;

import com.intellij.ide.hierarchy.call.CallHierarchyNodeDescriptor;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;

/**
 * Factory class to builder fields.
 *
 * @author Antal_Kajzer
 */
public class BuilderFieldFactory {

    private final PsiElementFactory psiElementFactory;
    private final List<PsiField> fields;

    public BuilderFieldFactory(final List<PsiField> fields, final PsiElementFactory psiElementFactory) {
        this.psiElementFactory = psiElementFactory;
        this.fields = fields;
    }

    public List<PsiField> createBuilderFields() {
        final List<PsiField> builderFields = new ArrayList<>();
        for (final PsiField field : fields) {
            final PsiField builderField = psiElementFactory.createField(field.getName(), field.getType());
            builderField.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
            builderFields.add(builderField);
        }
        return builderFields;
    }
}
