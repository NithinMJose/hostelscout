import { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import './AdminLayout.css';
import { Sidebar } from '../common';

interface SidebarLink {
  to: string;
  label: string;
  icon: string;
}

const adminLinks: SidebarLink[] = [
  { to: '/admin/dashboard', label: 'Dashboard', icon: '📊' },
  { to: '/admin/users', label: 'Users', icon: '👥' },
  { to: '/admin/hostel-owners', label: 'Hostel Owners', icon: '🏠' },
  { to: '/admin/hostels', label: 'Hostels', icon: '🏢' },
  { to: '/admin/settings', label: 'Settings', icon: '⚙️' },
];

export const AdminLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState<boolean>(false);
  const navigate = useNavigate();

  const handleLogout = (): void => {
    navigate('/auth/signin');
  };

  // Listen for toggle event from navbar
  useEffect(() => {
    const handleToggle = (): void => setSidebarOpen(prev => !prev);
    window.addEventListener('toggle-sidebar', handleToggle);
    return () => window.removeEventListener('toggle-sidebar', handleToggle);
  }, []);

  const closeSidebar = (): void => setSidebarOpen(false);

  return (
    <div className="dashboard-content">
      <Sidebar
        isOpen={sidebarOpen}
        onClose={closeSidebar}
        links={adminLinks}
        onLogout={handleLogout}
      />
      
      <main className="dashboard-main">
        <Outlet />
      </main>
    </div>
  );
};
