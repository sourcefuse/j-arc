import { TestBed } from '@angular/core/testing';

import { AnyAdapter } from './any-adapter.service';

describe('AnyAdapterService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [AnyAdapter],
    }),
  );

  it('should be created', () => {
    const service: AnyAdapter = TestBed.inject(AnyAdapter);
    expect(service).toBeTruthy();
  });
});
