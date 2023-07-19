import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkExpireComponent } from './link-expire.component';

describe('LinkExpireComponent', () => {
  let component: LinkExpireComponent;
  let fixture: ComponentFixture<LinkExpireComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LinkExpireComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LinkExpireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
