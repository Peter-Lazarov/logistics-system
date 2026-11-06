import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, Subject } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { Register } from './register';

describe('Register', () => {
  let component: Register;
  let fixture: ComponentFixture<Register>;
  let authService: { register: ReturnType<typeof vi.fn> };
  let router: { navigate: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    authService = { register: vi.fn().mockReturnValue(of({ message: 'Registered' })) };
    router = { navigate: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [Register],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Register);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('keeps registration disabled until all fields are valid', () => {
    fixture.detectChanges();

    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBe(true);

    component.form.setValue({
      email: 'user@gmail.com',
      password: 'test',
      name: 'John Smith',
      phone: '+359888123456',
    });
    fixture.detectChanges();

    expect(button.disabled).toBe(false);
  });

  it('reports whitespace-only values as required after touch', () => {
    component.form.setValue({ email: '   ', password: '   ', name: '   ', phone: '   ' });
    component.form.markAllAsTouched();

    expect(component.fieldError('email')).toBe('Email is required');
    expect(component.fieldError('password')).toBe('Password is required');
    expect(component.fieldError('name')).toBe('Full name is required');
    expect(component.fieldError('phone')).toBe('Phone number is required');
  });

  it('marks an invalid field red only after it loses focus', () => {
    fixture.detectChanges();
    const email: HTMLInputElement = fixture.nativeElement.querySelector('#email');
    email.value = 'invalid-email';
    email.dispatchEvent(new Event('input'));
    email.dispatchEvent(new FocusEvent('focus'));
    fixture.detectChanges();

    expect(component.form.controls['email'].invalid).toBe(true);
    expect(component.form.controls['email'].touched).toBe(false);

    email.dispatchEvent(new FocusEvent('blur'));
    fixture.detectChanges();

    expect(component.form.controls['email'].touched).toBe(true);
    expect(component.fieldError('email')).toBe('Invalid email format');
  });

  it('uses the specified format and length messages', () => {
    component.form.controls['email'].setValue('test');
    component.form.controls['password'].setValue('123');
    component.form.controls['name'].setValue('John123');
    component.form.controls['phone'].setValue('123abc45');
    component.form.markAllAsTouched();

    expect(component.fieldError('email')).toBe('Invalid email format');
    expect(component.fieldError('password')).toBe('Password must be between 4 and 50 characters');
    expect(component.fieldError('name')).toBe('Full name contains invalid characters');
    expect(component.fieldError('phone')).toBe('Invalid phone number format');
  });

  it('rejects values above each maximum length', () => {
    component.form.setValue({
      email: `${'a'.repeat(90)}@gmail.com`,
      password: 'p'.repeat(51),
      name: 'J'.repeat(101),
      phone: '1'.repeat(21),
    });

    expect(component.form.invalid).toBe(true);
  });

  it('shows the password hint only while the password field is focused', () => {
    fixture.detectChanges();
    const password: HTMLInputElement = fixture.nativeElement.querySelector('#password');

    expect(fixture.nativeElement.querySelector('#password-hint')).toBeNull();
    password.dispatchEvent(new FocusEvent('focus'));
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#password-hint')).not.toBeNull();

    password.dispatchEvent(new FocusEvent('blur'));
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#password-hint')).toBeNull();
  });

  it('trims fields and collapses full-name spaces before sending', () => {
    component.form.setValue({
      email: '  user@gmail.com  ',
      password: ' test ',
      name: '  John   Smith  ',
      phone: ' +1 (555) 123-4567 ',
    });

    component.register();

    expect(authService.register).toHaveBeenCalledWith({
      email: 'user@gmail.com',
      password: 'test',
      name: 'John Smith',
      phone: '+1 (555) 123-4567',
    });
  });

  it('prevents duplicate submissions while the request is pending', () => {
    const response = new Subject<{ message: string }>();
    authService.register.mockReturnValue(response);
    component.form.setValue({
      email: 'user@gmail.com',
      password: 'test',
      name: 'John Smith',
      phone: '0888123456',
    });

    component.register();
    component.register();
    fixture.detectChanges();

    expect(authService.register).toHaveBeenCalledTimes(1);
    expect(component.isSubmitting).toBe(true);
    expect(fixture.nativeElement.querySelector('.submitting-message')?.textContent.trim())
      .toBe('Your account is being created...');
    response.next({ message: 'Registered' });
    response.complete();
    fixture.detectChanges();
    expect(component.isSubmitting).toBe(false);
    expect(fixture.nativeElement.querySelector('.submitting-message')).toBeNull();
  });
});
