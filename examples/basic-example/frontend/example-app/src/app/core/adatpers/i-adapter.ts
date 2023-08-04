/* eslint-disable @typescript-eslint/no-explicit-any */
export interface IAdapter<T, R = T> {
  adaptToModel(resp: any): T;
  adaptFromModel(data: Partial<R>): any;
}
