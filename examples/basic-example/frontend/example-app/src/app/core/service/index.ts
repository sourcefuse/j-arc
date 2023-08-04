import { ApiService } from './api.service';
import { DexieService } from './dexie.service';

export const CoreServices = [ApiService, DexieService];

export * from './api.service';
export * from './dexie.service';
