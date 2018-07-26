package com.plugin.builder.factory;

import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;

/**
 * Factory class to create builder methods.
 *
 * @author Antal_Kajzer
 */
public class BuilderMethodFactory {
    private static final String builderClassName = "Builder";
    private static final String builderParamName = builderClassName.toLowerCase();

    private final MethodNameFactory methodNameFactory;
    private final PsiElementFactory elementFactory;
    private final List<PsiField> fields;
    private final PsiClass psiClass;
    private final JavaDocFactory javaDocFactory;

    public BuilderMethodFactory(final PsiElementFactory psiElementFactory, final PsiClass psiClass, List<PsiField> fields, final JavaDocFactory javaDocFactory) {
        methodNameFactory = new MethodNameFactory();
        this.fields = fields;
        this.psiClass = psiClass;
        this.elementFactory = psiElementFactory;
        this.javaDocFactory = javaDocFactory;
    }

    public List<PsiMethod> create(final PsiType builderType) {
        final List<PsiMethod> methods = new ArrayList<>();
        for (final PsiField field : fields) {
            final PsiMethod method = elementFactory.createMethod("with" + methodNameFactory.create(field), builderType);
            final PsiParameter param = elementFactory.createParameter(field.getName(), field.getType());
            param.getModifierList().setModifierProperty(PsiModifier.FINAL, true);
            method.getParameterList().add(param);
            method.getBody().add(elementFactory.createStatementFromText("this." + field.getName() + "=" + field.getName() + ";", method));
            method.getBody().add(elementFactory.createStatementFromText("return this;", method));
            methods.add(method);
        }

        final PsiMethod buildMethod = elementFactory.createMethod("build", elementFactory.createType(psiClass));
        buildMethod.getBody().add(elementFactory.createStatementFromText("return new " + psiClass.getName() + "(this);", buildMethod));
        methods.add(buildMethod);
        return methods;
    }

    public PsiMethod createConstructor(final PsiClass builderClass) {
        final PsiMethod constructor = elementFactory.createConstructor();
        constructor.getParameterList().add(elementFactory.createParameter(builderParamName, elementFactory.createType(builderClass)));
        constructor.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        for (final PsiField field : fields) {
            constructor.getBody().add(elementFactory.createStatementFromText(field.getName() + "=builder." + field.getName() + ";", null));
        }

        return constructor;
    }

    public PsiMethod createCopyBuilderConstructor(final PsiClass builtClass) {
        final PsiMethod copyBuilderConstructor = elementFactory.createConstructor();

        String parameterName = builtClass.getName();
        char c[] = parameterName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        parameterName = new String(c);

        copyBuilderConstructor.getParameterList().add(elementFactory.createParameter(parameterName, elementFactory.createType(builtClass)));

        for (final PsiField field : fields) {
            copyBuilderConstructor.getBody().add(elementFactory.createStatementFromText(field.getName() + " = " + parameterName + "." + field.getName() + ";\n", copyBuilderConstructor));
        }

        return copyBuilderConstructor;
    }
}
