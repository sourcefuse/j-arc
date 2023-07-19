import {AnyAdapter} from './any-adapter.service';
import {CountAdapter} from './count-adapter.service';
import {NameIdAdapter} from './name-id-adapter.service';

export const Adapters = [AnyAdapter, CountAdapter, NameIdAdapter];
export * from './any-adapter.service';
export * from './count-adapter.service';
export * from './i-adapter';
export * from './name-id-adapter.service';
