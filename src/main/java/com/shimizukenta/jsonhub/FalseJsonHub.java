package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

public class FalseJsonHub extends AbstractJsonHub {

	private static final long serialVersionUID = 3644504159696333609L;

	protected FalseJsonHub() {
		super();
	}
	
	@Override
	public JsonHubType type() {
		return JsonHubType.FALSE;
	}
	
	@Override
	public Optional<Boolean> optionalBoolean() {
		return Optional.of(Boolean.FALSE);
	}
	
	@Override
	public void toJson(Writer writer) throws IOException {
		writer.write(JsonLiteral.FALSE.toString());
	}
	
	@Override
	public String toString() {
		return Boolean.FALSE.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o instanceof FalseJsonHub);
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}
	
}
