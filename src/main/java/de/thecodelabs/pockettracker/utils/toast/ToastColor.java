package de.thecodelabs.pockettracker.utils.toast;

public enum ToastColor
{
	PRIMARY("alert-primary"),
	SECONDARY("alert-secondary"),
	SUCCESS("alert-success"),
	DANGER("alert-danger"),
	WARNING("alert-warning"),
	INFO("alert-info"),
	LIGHT("alert-light"),
	DARK("alert-dark");

	private final String styleClass;

	ToastColor(String styleClass)
	{
		this.styleClass = styleClass;
	}

	public String getStyleClass()
	{
		return styleClass;
	}
}
