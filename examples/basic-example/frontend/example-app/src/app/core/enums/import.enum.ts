export const enum ImportType {
  Jira = 'jira',
  Smartsheet = 'smartsheet',
  Excel = 'excel',
  CSV = 'csv',
  MPP = 'mpp',
}

export const IMPORT_NAME_MAP = {
  [ImportType.Jira]: 'Jira',
  [ImportType.Smartsheet]: 'Smartsheet',
  [ImportType.Excel]: 'Excel/CSV',
  [ImportType.CSV]: 'Excel/CSV',
  [ImportType.MPP]: 'Mpp',
};
