import { Outlet, Link, useLocation } from 'react-router-dom';
import './RootLayout.css';
import { Navbar } from '../common';

export const RootLayout = () => {
  const location = useLocation();
  
  // Determine which nav items to show based on current route
  const isAdmin: boolean = location.pathname.startsWith('/admin');
  const isOwner: boolean = location.pathname.startsWith('/owner');
  const isUser: boolean = location.pathname.startsWith('/user');
  const isAuth: boolean = location.pathname.startsWith('/auth');
  const isLoggedIn: boolean = isAdmin || isOwner || isUser;

  const getBrandText = (): string => {
    if (isAdmin) return 'HostelScout Admin';
    if (isOwner) return 'HostelScout Owner';
    return 'HostelScout';
  };

  const getBrandLink = (): string => {
    if (isAdmin) return '/admin';
    if (isOwner) return '/owner';
    if (isUser) return '/user';
    return '/';
  };

  const handleLogout = (): void => {
    // TODO: Implement logout logic
    window.location.href = '/auth/signin';
  };

  const renderNavItems = () => {
    // Guest nav
    if (!isLoggedIn && !isAuth) {
      return (
        <>
          <Link to="/hostels" className="nav-link">Browse Hostels</Link>
          <Link to="/auth/signin" className="nav-link">Sign In</Link>
          <Link to="/auth/signup" className="btn primary">Sign Up</Link>
        </>
      );
    }
    
    // Auth pages - minimal nav
    if (isAuth) {
      return <Link to="/" className="nav-link">← Back to Home</Link>;
    }
    
    // User nav
    if (isUser) {
      return (
        <>
          <Link to="/user/hostels" className="nav-link">Browse Hostels</Link>
          <Link to="/user/bookings" className="nav-link">My Bookings</Link>
          <Link to="/user/profile" className="nav-link">Profile</Link>
          <button onClick={handleLogout} className="btn ghost">Logout</button>
        </>
      );
    }
    
    // Admin nav
    if (isAdmin) {
      return (
        <>
          <Link to="/admin/settings" className="nav-link hide-mobile">Settings</Link>
          <button onClick={handleLogout} className="btn ghost">Logout</button>
        </>
      );
    }
    
    // Owner nav
    if (isOwner) {
      return (
        <>
          <Link to="/owner/profile" className="nav-link hide-mobile">Profile</Link>
          <button onClick={handleLogout} className="btn ghost">Logout</button>
        </>
      );
    }
    
    return null;
  };

  return (
    <div className="app-root">
      <Navbar 
        brandText={getBrandText()} 
        brandLink={getBrandLink()}
        showMenuButton={isAdmin || isOwner}
      >
        {renderNavItems()}
      </Navbar>
      
      <Outlet />
    </div>
  );
};
