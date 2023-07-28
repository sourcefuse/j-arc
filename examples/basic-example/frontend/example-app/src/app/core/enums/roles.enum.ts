export enum RoleType {
  Admin = 0,
  Others = 2,
  SuperAdmin = 10,
}

export const DisallowedRoles = [RoleType.Others];

export interface RoleTypeMapValue {
  permissionKey: string;
  value: RoleType;
}
export const RoleTypeMap: {
  [key: number]: RoleTypeMapValue;
} = {
  [RoleType.Admin]: {
    permissionKey: 'PlatformAdmin',
    value: RoleType.Admin,
  },
};
