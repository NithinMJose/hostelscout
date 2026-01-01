import type { InputHTMLAttributes } from 'react';
import './Input.css';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

export const Input = ({ 
  label, 
  error, 
  id,
  className = '',
  ...props 
}: InputProps) => {
  return (
    <div className="input-group">
      {label && <label htmlFor={id}>{label}</label>}
      <input id={id} className={`input ${error ? 'error' : ''} ${className}`} {...props} />
      {error && <span className="error-text">{error}</span>}
    </div>
  );
};
