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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@UiController("dd_SomeEntity.edit")
@UiDescriptor("some-entity-edit.xml")
@EditedEntityContainer("someEntityDc")
public class SomeEntityEdit extends StandardEditor<SomeEntity> {
    public static final Logger log = LoggerFactory.getLogger(SomeEntityEdit.class);


    @Override
    protected void preventUnsavedChanges(BeforeCloseEvent event) {
        AtomicInteger counter = new AtomicInteger();

        CloseAction action = event.getCloseAction();
        if (action instanceof ChangeTrackerCloseAction && ((ChangeTrackerCloseAction)action).isCheckForUnsavedChanges() && this.hasUnsavedChanges()) {
            ScreenValidation screenValidation = (ScreenValidation)this.getApplicationContext().getBean(ScreenValidation.class);
            UnknownOperationResult result = new UnknownOperationResult();
            if (((UiScreenProperties)this.getApplicationContext().getBean(UiScreenProperties.class)).isUseSaveConfirmation()) {
                int resultCount = counter.incrementAndGet();

                ScreenValidation.SaveChangesDialogResult var10000 = screenValidation.showSaveConfirmationDialog(this, action).onCommit(() -> {
                    result.resume(this.closeWithCommit());
                    counter.decrementAndGet();
                }).onDiscard(() -> {
                    result.resume(this.closeWithDiscard());
                    counter.decrementAndGet();
                });
                Objects.requireNonNull(result);
                var10000.onCancel(() -> {
                    result.fail();
                    counter.decrementAndGet();
                });
                log.info("TRX: count= {}", counter);
            } else {
                ScreenValidation.UnsavedChangesDialogResult var5 = screenValidation.showUnsavedChangesDialog(this, action).onDiscard(() -> {
                    result.resume(this.closeWithDiscard());
                });
                Objects.requireNonNull(result);
                var5.onCancel(result::fail);
            }

            event.preventWindowClose(result);
        }
    }
}