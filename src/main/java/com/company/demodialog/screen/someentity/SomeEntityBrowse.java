package com.company.demodialog.screen.someentity;

import com.company.demodialog.entity.SomeEntity;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("dd_SomeEntity.browse")
@UiDescriptor("some-entity-browse.xml")
@LookupComponent("someEntitiesTable")
public class SomeEntityBrowse extends StandardLookup<SomeEntity> {
}