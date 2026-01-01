import { NavLink } from 'react-router-dom';
import './Sidebar.css';

interface SidebarLink {
  to: string;
  label: string;
  icon?: string;
}

interface SidebarProps {
  isOpen: boolean;
  onClose: () => void;
  links: SidebarLink[];
  onLogout?: () => void;
}

export const Sidebar = ({
  isOpen,
  onClose,
  links,
  onLogout,
}: SidebarProps) => {
  return (
    <>
      {/* Overlay for mobile */}
      {isOpen && <div className="sidebar-overlay" onClick={onClose} />}
      
      <aside className={`sidebar ${isOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <button className="sidebar-close" onClick={onClose} aria-label="Close menu">
            ✕
          </button>
        </div>
        
        <nav className="sidebar-nav">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
              onClick={onClose}
            >
              {link.icon && <span className="sidebar-icon">{link.icon}</span>}
              <span>{link.label}</span>
            </NavLink>
          ))}
        </nav>
        
        {onLogout && (
          <div className="sidebar-footer">
            <button className="btn ghost sidebar-logout" onClick={onLogout}>
              Logout
            </button>
          </div>
        )}
      </aside>
    </>
  );
};
