/*
 *  Copyright 2012 Cedric Gatay
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.srmvision.tools.wicketdebug.idea.ApplicationComponent;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.ClassUtil;

/**
 * Utility class allowing to open the specified class in the editor.
 * It handles anonymous classes by looking up classes by JVMName.
 *
 * @author cgatay
 */
public class ClassNavigator {

    public static void scheduleClassOpening(final String className) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                Project[] projects = ProjectManager.getInstance().getOpenProjects();

                for (Project project : projects) {
                    final PsiClass targetPsiClass = ClassUtil.findPsiClassByJVMName(PsiManager.getInstance(project),
                                                                           className);
                    if (targetPsiClass != null) {
                        navigate(project, targetPsiClass);
                        return;
                    }
                }
            }
        });
    }

    private static void navigate(Project project, final PsiClass psiClass) {
        if (psiClass != null){
            psiClass.navigate(true);
            WindowManager.getInstance().suggestParentWindow(project).toFront();
        }
    }

}