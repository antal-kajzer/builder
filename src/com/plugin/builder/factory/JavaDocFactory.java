package com.plugin.builder.factory;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;

/**
 * Factory class to create java docs.
 *
 * @author Antal_Kajzer
 */
public class JavaDocFactory {

    public static final String SPLIT_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
    private final PsiElementFactory elementFactory;
    private final PsiClass psiClass;

    public JavaDocFactory(final PsiClass psiClass, final PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
        this.psiClass = psiClass;
    }

    public PsiDocComment createBuilderClassJavaDoc() {
        final StringBuilder comment = createStringBuilder();
        comment.append("/**\n")
                .append(" * Builder for instance of type {@link ")
                .append(psiClass.getQualifiedName())
                .append("}.")
                .append("\n")
                .append(" */\n");
        return elementFactory.createDocCommentFromText(comment.toString());
    }

    @NotNull
    private StringBuilder createStringBuilder() {
        return new StringBuilder();
    }

    public PsiDocComment createCopyBuilderConstructorJavaDoc(final PsiMethod method) {
        final StringBuilder comment = createStringBuilder();

        comment.append("/**\n")
                .append(" * Copy builder constructor. ")
                .append("\n")
                .append(" *\n")
                .append(" * @param ")
                .append(method.getParameterList().getParameters()[0].getName())
                .append(" ")
                .append(arrayToString(splitNameByCamelCase(method)))
                .append(" \n")
                .append(" * @return {@link Builder}")
                .append("\n */\n");

        return elementFactory.createDocCommentFromText(comment.toString());
    }

    public PsiDocComment createMethodJavaDoc(final PsiMethod method) {
        final StringBuilder comment = createStringBuilder();
        if (method.getName().equals("build")) {
            comment.append("/**\n")
                    .append(" * Creates a new instance of type {@link ")
                    .append(psiClass.getQualifiedName())
                    .append("}\n*/");
        }else {
            comment.append("/**\n")
                    .append(" * Sets ")
                    .append(arrayToString(splitNameByCamelCase(method)))
                    .append(".")
                    .append("\n")
                    .append("\n")
                    .append(" * @param ")
                    .append(method.getParameterList().getParameters()[0].getName())
                    .append(" ")
                    .append(arrayToString(splitNameByCamelCase(method)))
                    .append(" \n")
                    .append(" * @return {@link Builder}")
                    .append("\n */\n");
        }
        return elementFactory.createDocCommentFromText(comment.toString());
    }

    @NotNull
    private String[] splitNameByCamelCase(PsiMethod method) {
        return method.getParameterList().getParameters()[0].getName().split(SPLIT_REGEX);
    }

    private String arrayToString(String[] array) {

        String result = "";

        for(String str : array) {
            result += str.toLowerCase() + " ";
        }

        return result.substring(0, result.length()-1);
    }
}
