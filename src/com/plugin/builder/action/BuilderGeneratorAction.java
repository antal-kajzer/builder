package com.plugin.builder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

public class BuilderGeneratorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final PsiFile file = anActionEvent.getData(DataKeys.PSI_FILE);
        final Editor editor = anActionEvent.getData(DataKeys.EDITOR);

        if (isEditorOrFileNull(file, editor)) {
            return;
        }

        final int offset = editor.getCaretModel().getOffset();
        final PsiElement element = file.findElementAt(offset);
        final PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);

        new WriteCommandAction.Simple(clazz.getProject(), clazz.getContainingFile()) {
            @Override
            protected void run() {
                final BuilderCodeGenerator generator = new BuilderCodeGenerator(clazz);
                generator.generateBuilder();
            }
        }.execute();
    }

    private boolean isEditorOrFileNull(PsiFile file, Editor editor) {
        return file == null || editor == null;
    }
}
