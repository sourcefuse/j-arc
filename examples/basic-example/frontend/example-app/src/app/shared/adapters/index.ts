import { CurrentUserAdapter } from './current-user-adapter.service';
import { LoginAdapter } from './login-adapter.service';
import { RolesAdapter } from './roles-adapter.service';
import { SignUpAdapter } from './signup-adapter.service';
import { UserDtoAdapter } from './user-dto-adapter.service';

export const Adapters = [
  CurrentUserAdapter,
  LoginAdapter,
  SignUpAdapter,
  UserDtoAdapter,
  RolesAdapter,
];

export * from './current-user-adapter.service';
export * from './login-adapter.service';
export * from './signup-adapter.service';
export * from './user-dto-adapter.service';
export * from './roles-adapter.service';
