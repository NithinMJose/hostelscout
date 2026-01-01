import type { ReactNode } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

interface NavbarProps {
  brandText?: string;
  brandLink?: string;
  showMenuButton?: boolean;
  children?: ReactNode;
}

export const Navbar = ({
  brandText = 'HostelScout',
  brandLink = '/',
  showMenuButton = false,
  children,
}: NavbarProps) => {
  const handleMenuToggle = (): void => {
    window.dispatchEvent(new CustomEvent('toggle-sidebar'));
  };

  return (
    <header className="navbar">
      <div className="navbar-left">
        {showMenuButton && (
          <button 
            className="menu-toggle" 
            onClick={handleMenuToggle}
            aria-label="Toggle menu"
          >
            <span className="menu-icon">☰</span>
          </button>
        )}
        <Link to={brandLink} className="navbar-brand">
          {brandText}
        </Link>
      </div>
      <nav className="navbar-nav">
        {children}
      </nav>
    </header>
  );
};
