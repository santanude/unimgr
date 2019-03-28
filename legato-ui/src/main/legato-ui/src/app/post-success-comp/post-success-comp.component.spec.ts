import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PostSuccessCompComponent } from './post-success-comp.component';

describe('PostSuccessCompComponent', () => {
  let component: PostSuccessCompComponent;
  let fixture: ComponentFixture<PostSuccessCompComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PostSuccessCompComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PostSuccessCompComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
