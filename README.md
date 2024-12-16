Bug description: https://github.com/jmix-framework/jmix/issues/3567

Steps to reproduce:
1. Create entity, save, reopen view, change any field
2. Select network throttling in dev tools (e.g. 3G)
3. Try to close window without saving
4. If variable `bPreventDuplicate` = `false` - many dialogs will be shown, if `bPreventDuplicate` = `true` - only one dialog will be shown

Record: https://disk.yandex.ru/i/pvxfWGl1zCXZ3A