import { useState, type ChangeEvent, type FormEvent } from 'react';
import { Link } from 'react-router-dom';
import './SignUpPage.css';
import { Input, Button } from '../../components/common';

type UserRole = 'user' | 'hostelOwner';

interface SignUpFormData {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  role: UserRole;
}

export const SignUpPage = () => {
  const [formData, setFormData] = useState<SignUpFormData>({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'user',
  });

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>): void => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    // TODO: Implement sign up logic with backend
    console.log('Sign up:', formData);
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1>Sign Up</h1>
        <p>Create your HostelScout account</p>
        
        <form onSubmit={handleSubmit}>
          <Input
            id="name"
            name="name"
            type="text"
            label="Full Name"
            placeholder="Enter your full name"
            value={formData.name}
            onChange={handleChange}
            required
          />
          
          <Input
            id="email"
            name="email"
            type="email"
            label="Email"
            placeholder="Enter your email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          
          <div className="input-group">
            <label htmlFor="role">I want to</label>
            <select 
              id="role" 
              name="role" 
              value={formData.role} 
              onChange={handleChange}
              className="input"
            >
              <option value="user">Find a Hostel/PG</option>
              <option value="hostelOwner">List my Hostel/PG</option>
            </select>
          </div>
          
          <Input
            id="password"
            name="password"
            type="password"
            label="Password"
            placeholder="Create a password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          
          <Input
            id="confirmPassword"
            name="confirmPassword"
            type="password"
            label="Confirm Password"
            placeholder="Confirm your password"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
          />
          
          <Button type="submit" variant="primary">Sign Up</Button>
        </form>
        
        <p className="auth-footer">
          Already have an account? <Link to="/auth/signin">Sign In</Link>
        </p>
      </div>
    </div>
  );
};
