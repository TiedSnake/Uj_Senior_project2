package com.haircut.frontend;

class UserDataModel2 {
    private String name, viewAction, blockAction, deleteAction;

    public UserDataModel2(String name, String viewAction, String blockAction, String deleteAction) {
        this.name = name;
        this.viewAction = viewAction;
        this.blockAction = blockAction;
        this.deleteAction = deleteAction;
    }

    // Getters
    public String getName() { return name; }
    public String getViewAction() { return viewAction; }
    public String getBlockAction() { return blockAction; }
    public String getDeleteAction() { return deleteAction; }
}
