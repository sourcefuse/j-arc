import {TestBed} from '@angular/core/testing';
import {NameIdAdapter} from './name-id-adapter.service';

describe('NameIdAdapterService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [NameIdAdapter],
    }),
  );

  it('should be created', () => {
    const service: NameIdAdapter = TestBed.inject(NameIdAdapter);
    expect(service).toBeTruthy();
  });
});
