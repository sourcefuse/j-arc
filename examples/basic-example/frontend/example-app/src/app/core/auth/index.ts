import { AuthGuard } from "./auth.guard";
import { LoggedInGuard } from "./logged-in.guard";

export * from "./auth.guard";
export * from "./logged-in.guard";

export const Gaurds = [
    AuthGuard,
    LoggedInGuard
]
