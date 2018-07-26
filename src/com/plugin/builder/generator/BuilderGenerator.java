package com.plugin.builder.generator;

import java.util.List;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.plugin.builder.FinalFieldProvider;
import com.plugin.builder.factory.BuilderFieldFactory;
import com.plugin.builder.factory.BuilderMethodFactory;
import com.plugin.builder.factory.JavaDocFactory;

/**
 * Builder generator.
 *
 * @author Antal_Kajzer
 */
public class BuilderGenerator {
    private static final String builderClassName = "Builder";

    private final PsiClass clazz;
    private final PsiElementFactory elementFactory;
    private final List<PsiField> fields;
    private final FinalFieldProvider finalFieldProvider;
    private final BuilderMethodFactory builderMethodFactory;
    private final JavaDocFactory javaDocFactory;
    private final BuilderFieldFactory builderFieldFactory;

    public BuilderGenerator(final PsiClass clazz) {
        this.clazz = clazz;
        elementFactory = JavaPsiFacade.getElementFactory(clazz.getProject());
        finalFieldProvider = new FinalFieldProvider();
        fields = finalFieldProvider.provide(clazz);
        javaDocFactory = new JavaDocFactory(this.clazz, elementFactory);
        builderMethodFactory = new BuilderMethodFactory(elementFactory, this.clazz, fields, javaDocFactory);
        this.builderFieldFactory = new BuilderFieldFactory(fields, elementFactory);
    }

    public void removeBuilder() {
        for (final PsiClass innerClass : clazz.getAllInnerClasses()) {
            if (innerClass.getName().equals(builderClassName)) {
                this.removeConstructor();
                innerClass.delete();
                return;
            }
        }
    }

    private void removeConstructor() {
        for (final PsiMethod constructor : this.clazz.getConstructors()) {
            constructor.delete();
        }
    }

    public void generateBuilder() {
        this.removeBuilder();
        final PsiClass builderClass = createBuilderClass();
        builderClass.addBefore(javaDocFactory.createBuilderClassJavaDoc(), builderClass.getFirstChild());

        final PsiMethod constructor = builderMethodFactory.createConstructor(builderClass);
        this.clazz.add(constructor);

        for (final PsiMethod method : builderClass.getMethods()) {
            if(!method.isConstructor()) {
                method.addBefore(javaDocFactory.createMethodJavaDoc(method), method.getFirstChild());
            }
            else if(method.isConstructor()) {
                method.addBefore(javaDocFactory.createCopyBuilderConstructorJavaDoc(method), method.getFirstChild());
            }

        }

        this.clazz.add(builderClass);
    }

    private PsiClass createBuilderClass() {
        final PsiClass builderClass = elementFactory.createClass(this.builderClassName);
        builderClass.getModifierList().setModifierProperty(PsiModifier.STATIC, true);
        builderClass.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);

        for (final PsiField field : builderFieldFactory.createBuilderFields()) {
            builderClass.add(field);
        }

        final PsiMethod copyBuilderConstructor = builderMethodFactory.createCopyBuilderConstructor(clazz);
        builderClass.add(copyBuilderConstructor);

        for (final PsiMethod method : builderMethodFactory.create(elementFactory.createType(builderClass))) {
            builderClass.add(method);
        }

        return builderClass;
    }

}