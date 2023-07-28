export enum NotificationEvents {
  WorkspaceInvitation = 'workspace_invitation',
  AddBoardMember = 'add_board_member',
  RemoveBoardMember = 'remove_board_member',
  DeleteBoard = 'delete_board',
  Editboard = 'edit_board',
  AddTaskMember = 'add_task_member',
  RemoveTaskMember = 'remove_task_member',
  EditingTask = 'editing_task',
  DeletingTask = 'deleting_task',
  BulkTaskPatch = 'bulk_task_patch',
  AddTask = 'add_task',
  MakesOasisAdmin = 'makes_admin_role',

  GroupRenamed = 'group_renamed',
  GroupDeleted = 'group_deleted',
  GroupImportAborted = 'group_import_aborted',
  GroupMetaDataUpdated = 'group_metadata_updated',
  GroupAdded = 'group_added',
  ItemDuplicated = 'item_duplicated',

  WorkspaceDelete = 'workspace_delete',
  WorkspaceUpdate = 'workspace_update',
  workspaceColumnAdd = 'workspace_column_add',

  ColumnAdd = 'column_add',
  ColumnDuplicate = 'column_duplicate',
  ColumnUpdate = 'column_update',
  ColumnDelete = 'column_delete',
  BulkBoardColumnPatch = 'bulk_board_column_patch',

  MentionedUserInReply = 'mention_user_in_reply',
  MentionedUserInUpdate = 'mention_user_in_update',
  RepliedOnUpdate = 'replied_on_update',

  TaskFileAdded = 'task_file_added',
  TaskFileDeleted = 'task_file_deleted',

  BoardMemberUpdate = 'board_member_update',
  WorkspaceMemberUpdate = 'workspace_member_update',

  MessageCreated = 'message_created',
  MessageUpdated = 'message_updated',
  MessageDeleted = 'message_deleted',

  TaskPermissionUpdated = 'task_permission_updated',
  BoardColumnPermissionUpdated = 'column_permission_updated',

  UnsubscribeUserFromBoard = 'unsubscribe_user_from_board',
  AssignsUserAnItem = 'assigns_user_an_item',

  FilterGroupRenamed = 'filter_group_renamed',
  FilterGroupDeleted = 'filter_group_deleted',
  FilterGroupAdded = 'filter_group_added',
  FilterItemDuplicated = 'filter_item_duplicated',
  FilterColumnAdd = 'filter_column_add',
  FilterColumnUpdate = 'filter_column_update',
  FilterColumnDelete = 'filter_column_delete',
  FilterColumnCount = 'filter_column_count',
  FilterBoardMemberAdded = 'filter_board_member_added',
  FilterBoardMemberRemoved = 'filter_board_member_removed',
  ProofFileUploaded = 'proof_file_uploaded',

  ExportFileFinish = 'export_file_finish',
  ExportFileError = 'export_file_error',

  BatchImportCompleted = 'batch_import_completed',
  EntityImportCompleted = 'entity_import_completed',
  EntityImportWarning = 'entity_import_warning',

  BoardSyncCompleted = 'board_sync_completed',
  WorkspaceMemberDelete = 'workspace_member_delete',
  MoveBoard = 'move_board',
  TaskLinksUpdated = 'task_links_update',

  AddUserGroup = 'add_user_group',
  UpdateUserGroup = 'update_user_group',
  RemoveUserGroup = 'remove_user_group',
  EditGroupUser = 'edit_group_user',
  DeleteGroupUser = 'delete_group_user',
  ActivityLogsExport = 'activity_log_export',
  DependencyError = 'dependency_error',

  TimelineMigrationEvent = 'timeline_migration_event',
  TaskAllocationStateUpdate = 'task_allocation_change_update',
}

export enum ToastrType {
  Success = 'success',
  Info = 'info',
  Error = 'error',
  Warning = 'warn',
}

export const EventToastrTypeMap = {
  [NotificationEvents.DependencyError]: ToastrType.Error,
  [NotificationEvents.ExportFileError]: ToastrType.Error,
  [NotificationEvents.EntityImportWarning]: ToastrType.Warning,
};

export enum ItemDuplicateType {
  Group = 'Group',
  Task = 'Task',
}
