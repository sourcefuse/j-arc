import { TestBed } from '@angular/core/testing';

import { CountAdapter } from './count-adapter.service';

describe('CountAdapterService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [CountAdapter],
    }),
  );

  it('should be created', () => {
    const service: CountAdapter = TestBed.inject(CountAdapter);
    expect(service).toBeTruthy();
  });
});
