export declare type HttpObserve = 'body' | 'events' | 'response';

export declare type ResponseType = 'arraybuffer' | 'blob' | 'json' | 'text';

export interface CachedData {
    date: Date; // last accessed Date
    id: string;
  }
  