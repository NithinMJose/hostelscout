import { useState, type ChangeEvent, type FormEvent } from 'react';
import './SignInPage.css';
import { Link, useNavigate } from 'react-router-dom';
import { Input, Button } from '../../components/common';

interface SignInFormData {
  email: string;
  password: string;
}

interface LoginResponse {
  token: string;
  admin: {
    adminStatus: string;
    statusChangedAt: string;
    username: string;
    email: string;
    createdAt: string;
    updatedAt: string;
    role: 'ADMIN' | 'HOSTEL_OWNER' | 'HOSTEL_RESIDENT';
  };
}

export const SignInPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<SignInFormData>({
    email: '',
    password: '',
  });
  const [error, setError] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e: ChangeEvent<HTMLInputElement>): void => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError(''); // Clear error on input change
  };

  const getRedirectPath = (role: string): string => {
    switch (role) {
      case 'ADMIN':
        return '/admin/dashboard';
      case 'HOSTEL_OWNER':
        return '/owner/dashboard';
      case 'HOSTEL_RESIDENT':
        return '/user/dashboard';
      default:
        return '/';
    }
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    const payload = {
      username: formData.email,
      password: formData.password,
    };

    (async () => {
      try {
        const res = await fetch('http://localhost:8080/api/auth/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        });

        const text = await res.text();
        let data: unknown = text;
        try { data = JSON.parse(text); } catch (_) { /* keep text */ }

        if (!res.ok) {
          const errorMsg = typeof data === 'object' && data !== null && 'message' in data
            ? String((data as { message: string }).message)
            : 'Login failed. Please check your credentials.';
          setError(errorMsg);
          setIsLoading(false);
          return;
        }

        // Successful login
        const loginData = data as LoginResponse;
        
        // Store token and user info
        localStorage.setItem('token', loginData.token);
        localStorage.setItem('user', JSON.stringify(loginData.admin));

        // Redirect based on role
        const redirectPath = getRedirectPath(loginData.role);
        navigate(redirectPath);
      } catch (err) {
        console.error('Network error during login', err);
        setError('Network error. Please try again.');
        setIsLoading(false);
      }
    })();
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1>Sign In</h1>
        <p>Welcome back to HostelScout</p>
        
        {error && <div className="auth-error">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <Input
            id="email"
            name="email"
            type="text"
            label="Username or Email"
            placeholder="Enter your username or email"
            value={formData.email}
            onChange={handleChange}
            required
            disabled={isLoading}
          />
          
          <Input
            id="password"
            name="password"
            type="password"
            label="Password"
            placeholder="Enter your password"
            value={formData.password}
            onChange={handleChange}
            required
            disabled={isLoading}
          />
          
          <Button type="submit" variant="primary" disabled={isLoading}>
            {isLoading ? 'Signing in...' : 'Sign In'}
          </Button>
        </form>
        
        <p className="auth-footer">
          Don't have an account? <Link to="/auth/signup">Sign Up</Link>
        </p>
      </div>
    </div>
  );
};
