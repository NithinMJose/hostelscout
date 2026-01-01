import { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import './HostelOwnerLayout.css';
import { Sidebar } from '../common';

interface SidebarLink {
  to: string;
  label: string;
  icon: string;
}

const ownerLinks: SidebarLink[] = [
  { to: '/owner/dashboard', label: 'Dashboard', icon: '📊' },
  { to: '/owner/hostels', label: 'My Hostels', icon: '🏢' },
  { to: '/owner/hostels/add', label: 'Add Hostel', icon: '➕' },
  { to: '/owner/bookings', label: 'Bookings', icon: '📅' },
  { to: '/owner/profile', label: 'Profile', icon: '👤' },
];

export const HostelOwnerLayout = () => {
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
        links={ownerLinks}
        onLogout={handleLogout}
      />
      
      <main className="dashboard-main">
        <Outlet />
      </main>
    </div>
  );
};
