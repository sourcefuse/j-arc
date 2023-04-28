package com.sourcefuse.jarc.services.auditservice.constants;

public class Constants {

	public static enum AuditActions {
	    SAVE("SAVE"),
	    UPDATE("UPDATE"),
	    SAVE_ALL("SAVE_ALL"),
	    SAVE_AND_FLUSH("SAVE_AND_FLUSH"),
	    SAVE_ALL_AND_FLUSH("SAVE_ALL_AND_FLUSH"),
	    DELETE("DELETE"),
	    DELETE_BY_ID("DELETE_BY_ID"),
	    DELETE_ALL("DELETE_ALL"),
	    DELETE_ALL_BY_ID("DELETE_ALL_BY_ID"),
	    DELETE_ALL_IN_BATCH("DELETE_ALL_IN_BATCH"),
	    DELETE_ALL_BY_ID_IN_BATCH("DELETE_ALL_BY_ID_IN_BATCH"),
	    DELETE_ALL_BY_ENTITY("DELETE_ALL_BY_ENTITY"),
	    DELETE_BY_ENTITY("DELETE_BY_ENTITY");
	    
	    private final String name;

	    private AuditActions(String value) {
	        this.name = value;
	    }

	    public String value() {
	        return this.name;
	    }

	    @Override
	    public String toString() {
	        return name;
	    }
	}
}
