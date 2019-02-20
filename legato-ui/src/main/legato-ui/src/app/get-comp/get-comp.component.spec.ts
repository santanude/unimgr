import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCompComponent } from './get-comp.component';

describe('GetCompComponent', () => {
  let component: GetCompComponent;
  let fixture: ComponentFixture<GetCompComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetCompComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetCompComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
