package com.twb.intellij.openwithplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFileSystemItem;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OpenWithAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        // Using the event, evaluate the context, and enable or disable the action.
        Project project = event.getProject();
        boolean visible = project != null;
        event.getPresentation().setEnabledAndVisible(visible);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
            if (nav instanceof PsiFileSystemItem) {
                PsiFileSystemItem psiFileItem = (PsiFileSystemItem) nav;
                VirtualFile virtualFile = psiFileItem.getVirtualFile();
                String filePath = virtualFile.getPath();
                Runtime rt = Runtime.getRuntime();
                String command = String.format("subl %s", filePath);
                try {
                    Process exec = rt.exec(command);
                    exec.waitFor();
                } catch (IOException | InterruptedException e) {
                    Messages.showMessageDialog(e.getMessage(), "Open With Exception", Messages.getErrorIcon());
                }
            }
        }
    }
}
