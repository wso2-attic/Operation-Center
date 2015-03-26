package org.wso2.oc.data;

public class Command {
    private String commandName;

	public Command(String commandName) {
		this.commandName = commandName;
	}

	public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
}
