package com.company.demodialog.screen.someentity;

import com.company.demodialog.entity.SomeEntity;
import io.jmix.ui.UiScreenProperties;
import io.jmix.ui.screen.ChangeTrackerCloseAction;
import io.jmix.ui.screen.CloseAction;
import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.ScreenValidation;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import io.jmix.ui.util.UnknownOperationResult;

import java.util.concurrent.atomic.AtomicBoolean;

@UiController("dd_SomeEntity.edit")
@UiDescriptor("some-entity-edit.xml")
@EditedEntityContainer("someEntityDc")
public class SomeEntityEdit extends StandardEditor<SomeEntity> {

    protected final AtomicBoolean bDialogShown = new AtomicBoolean(false);
    protected final boolean bPreventDuplicate = false;

    @Override
    protected void preventUnsavedChanges(BeforeCloseEvent event) {
        CloseAction action = event.getCloseAction();

        if (action instanceof ChangeTrackerCloseAction
            && ((ChangeTrackerCloseAction) action).isCheckForUnsavedChanges()
            && hasUnsavedChanges()) {

            UnknownOperationResult result = new UnknownOperationResult();

            if (!bPreventDuplicate || bDialogShown.compareAndSet(false, true)) {
                ScreenValidation screenValidation = getApplicationContext().getBean(ScreenValidation.class);

                if (getApplicationContext().getBean(UiScreenProperties.class).isUseSaveConfirmation()) {

                    screenValidation.showSaveConfirmationDialog(this, action)
                            .onCommit(() -> {
                                result.resume(closeWithCommit());
                                bDialogShown.set(false);
                            })
                            .onDiscard(() -> {
                                result.resume(closeWithDiscard());
                                bDialogShown.set(false);
                            })
                            .onCancel(() -> {
                                result.fail();
                                bDialogShown.set(false);
                            });
                } else {
                    screenValidation.showUnsavedChangesDialog(this, action)
                            .onDiscard(() -> {
                                result.resume(closeWithDiscard());
                                bDialogShown.set(false);
                            })
                            .onCancel(() -> {
                                result.fail();
                                bDialogShown.set(false);
                            });
                }
            }
            event.preventWindowClose(result);
        }
    }
}