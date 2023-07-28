import {CurrentUserAdapter} from './current-user-adapter.service';
import {LoginAdapter} from './login-adapter.service';
import {SignUpAdapter} from './signup-adapter.service';

export const Adapters = [
  CurrentUserAdapter,
  LoginAdapter,
  SignUpAdapter,
];

export * from './current-user-adapter.service';
export * from './login-adapter.service';
export * from './signup-adapter.service';
