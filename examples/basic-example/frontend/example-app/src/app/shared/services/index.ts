import { AuditLogService } from "./audit-log.service";
import { AuthService } from "./auth.service";
import { RoleService } from "./role.service";
import { UserService } from "./user.service";

export const SharedServices = [
    AuthService,
    UserService,
    RoleService,
    AuditLogService
]

export * from "./audit-log.service";
export * from "./auth.service";
export * from "./role.service";
export * from "./user.service";