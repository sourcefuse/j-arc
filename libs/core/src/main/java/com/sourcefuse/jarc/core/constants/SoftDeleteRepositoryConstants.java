package com.sourcefuse.jarc.core.constants;

public class SoftDeleteRepositoryConstants {

  private SoftDeleteRepositoryConstants() {
    throw new IllegalStateException("Utility class");
  }

  public static final String DELETED_FIELD = "deleted";
  public static final String ID_MUST_NOT_BE_NULL =
    "The given id must not be null!";
  public static final String IDS_MUST_NOT_BE_NULL =
    "The given ids must not be null!";
  public static final String ENTITY_MUST_NOT_BE_NULL =
    "The entity must not be null!";
  public static final String ENTITIES_MUST_NOT_BE_NULL =
    "Entities must not be null!";
}
