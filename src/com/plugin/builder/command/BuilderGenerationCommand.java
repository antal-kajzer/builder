package com.plugin.builder.command;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.plugin.builder.generator.BuilderGenerator;

/**
 * BuilderGenerationCommand wrapper class to store action related operations.
 *
 * @author Antal_Kajzer
 */
public class BuilderGenerationCommand extends WriteCommandAction.Simple {

    private PsiClass psiClass;

    public static BuilderGenerationCommand createInstance(PsiClass psiClass, Project project, PsiFile... files) {
        return new BuilderGenerationCommand(psiClass, project, files);
    }

    protected BuilderGenerationCommand(PsiClass psiClass, Project project, PsiFile... files) {
        super(project, files);
        this.psiClass = psiClass;
    }

    @Override
    protected void run() {
        final BuilderGenerator generator = new BuilderGenerator(psiClass);
        generator.generateBuilder();
    }
}
